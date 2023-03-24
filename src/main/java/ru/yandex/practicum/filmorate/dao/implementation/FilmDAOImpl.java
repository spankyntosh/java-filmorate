package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class FilmDAOImpl implements FilmDAO {

    private final FilmGenreDAO filmGenreDAO;
    private final MpaFilmDAO mpaFilmDAO;
    private final LikeDAO likeDAO;
    private final DirectorDAO directorDAO;
    private final JdbcTemplate jdbcTemplate;
    private final FilmDirectorDAO filmDirectorDAO;

    @Override
    public Collection<Film> getFilms() {

        String statement = "SELECT * "
                + "FROM films";

        return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO));
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String statement = "SELECT * "
                + "FROM films "
                + "WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(statement, new FilmMapper(), filmId);

        film.setMpa(mpaFilmDAO.getMpaByFilmId(filmId));
        film.setGenres(filmGenreDAO.getFilmGenres(filmId));
        film.setDirectors(filmDirectorDAO.getFilmDirectors(filmId));
        return film;
    }

    @Override
    public Collection<Film> getDirectorAllFilms(Integer directorId, String sortBy) {

        String statement = null;
        if (sortBy.contentEquals("year")) {
            statement = "SELECT * "
                    + "FROM films AS f "
                    + "LEFT JOIN films_directors AS fd ON f.id = fd.film_id "
                    + "WHERE fd.director_id = ?"
                    + "ORDER BY release_date";
        } else {
            statement = "SELECT f.* "
                    + "FROM films as f "
                    + "LEFT JOIN films_directors AS fd ON f.id = fd.film_id "
                    + "LEFT JOIN likes AS l ON f.id = l.film_id "
                    + "WHERE fd.director_id = ?"
                    + "GROUP BY f.id "
                    + "ORDER BY COUNT(l.user_id) DESC";
        }

        return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), directorId);
    }

    @Override
    public Film addFilmInfo(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films").usingGeneratedKeyColumns("id");
        int filmId = insert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(filmId);
        if (!isNull(film.getMpa())) {
            mpaFilmDAO.addMpaFilmRecord(film.getId(), film.getMpa().getId());
        }
        if (!isNull(film.getGenres())) {
            filmGenreDAO.addFilmGenreRecord(film.getId(), film.getGenres().stream().distinct().collect(toList()));
        }
        if (!isNull(film.getDirectors())) {
            List<Integer> directorIDs = film.getDirectors().stream().map(Director::getId).collect(toList());
            if (!directorDAO.isAllDirectorsExists(directorIDs, film.getDirectors().size())) {
                throw new EntityNotFoundException("Не найдена информация об одном или нескольких режиссёрах ");
            }
            filmDirectorDAO.addRecords(new ArrayList<>(film.getDirectors()), film.getId());

        }
        return film;
    }

    @Override
    public void delete(Integer id) {
        if (!isFilmExists(id)) {
            throw new EntityNotFoundException("Попытка удалить фильм с несуществующим id фильма");
        }
        String deleteQuery = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(deleteQuery, id);
    }

    @Override
    public boolean isFilmExists(Integer filmId) {
        String statement = "SELECT EXISTS (SELECT * "
                + "FROM films "
                + "WHERE id = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(statement, Boolean.TYPE, filmId));
    }

    @Override
    public Film updateFilmInfo(Film film) {
        String statement = "UPDATE films "
                + "SET name = ?, description = ?, duration = ?, release_date = ? WHERE id = ?";

        jdbcTemplate.update(statement
                , film.getName()
                , film.getDescription()
                , film.getDuration()
                , film.getReleaseDate()
                , film.getId()
        );

        mpaFilmDAO.deleteMpaFilmRecord(film.getId());
        if (film.getMpa() != null) {
            mpaFilmDAO.addMpaFilmRecord(film.getId(), film.getMpa().getId());
        }

        filmGenreDAO.deleteRecordsByFilmId(film.getId());
        Optional<Collection<Genre>> optionalCollection = Optional.ofNullable(film.getGenres());
        if (optionalCollection.isPresent()) {
            film.setGenres(film.getGenres().stream().sorted((o1, o2) -> {
                        if (o1.getId() > o2.getId()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    })
                    .distinct()
                    .collect(toList()));
            filmGenreDAO.addFilmGenreRecords(film.getGenres(), film.getId());
        }


        filmDirectorDAO.deleteRecords(film.getId());
        if (!isNull(film.getDirectors())) {
            List<Integer> directorIDs = film.getDirectors().stream().map(Director::getId).collect(toList());
            if (!directorDAO.isAllDirectorsExists(directorIDs, film.getDirectors().size())) {
                throw new EntityNotFoundException("Не найдена информация об одном или нескольких режиссёрах");
            }
            filmDirectorDAO.addRecords(new ArrayList<>(film.getDirectors()), film.getId());
        }

        if (isNull(film.getGenres())) {
            film.setGenres(new ArrayList<>());
        }
        if (isNull(film.getDirectors())) {
            film.setDirectors(new ArrayList<>());
        }
        return film;
    }

    @Override
    public Collection<Film> search(String query) {
        String searchQuery =
                "SELECT f.* " +
                        "FROM films f " +
                        "LEFT JOIN films_directors fd ON fd.film_id = f.id " +
                        "LEFT JOIN directors d ON fd.director_id = d.id " +
                        "LEFT JOIN likes l ON f.id = l.film_id " +
                        "WHERE UPPER(d.director_name) LIKE UPPER(?) OR UPPER(f.name) LIKE UPPER(?) " +
                        "GROUP BY f.id " +
                        "ORDER BY COUNT(l.USER_ID) DESC";
        return jdbcTemplate.query(searchQuery, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), "%" + query + "%", "%" + query + "%");
    }

    @Override
    public Collection<Film> searchByTitle(String query) {
        String searchQuery =
                "SELECT * " +
                        "FROM FILMS f " +
                        "LEFT JOIN likes l ON f.id = l.film_id " +
                        "WHERE UPPER(NAME) LIKE UPPER(?) " +
                        "GROUP BY f.id " +
                        "ORDER BY COUNT(l.user_id)";
        return jdbcTemplate.query(searchQuery, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), "%" + query + "%");
    }

    @Override
    public Collection<Film> searchByDirector(String query) {
        String searchQuery =
                "SELECT f.* " +
                        "FROM films f " +
                        "LEFT JOIN films_directors fd ON fd.film_id = f.id " +
                        "LEFT JOIN directors d ON fd.director_id = d.id " +
                        "LEFT JOIN likes l ON f.id = l.film_id " +
                        "WHERE UPPER(d.director_name) LIKE UPPER(?) " +
                        "GROUP BY f.id " +
                        "ORDER BY COUNT(l.USER_ID) desc";
        return jdbcTemplate.query(searchQuery, new FilmMapper(mpaFilmDAO, filmGenreDAO, filmDirectorDAO), "%" + query + "%");
    }

    @Override
    public boolean isFilmAlreadyHaveLikeFromUser(Integer filmId, Integer userId) {
        String statement = "SELECT EXISTS (SELECT user_id "
                + "FROM likes "
                + "WHERE film_id = ? AND user_id = ?)";


        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(statement, Boolean.TYPE, filmId, userId));

    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        likeDAO.addRecord(filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        likeDAO.deleteRecord(filmId, userId);
    }

}
