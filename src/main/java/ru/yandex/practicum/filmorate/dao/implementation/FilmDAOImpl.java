package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Component
public class FilmDAOImpl implements FilmDAO {

    private final FilmGenreDAO filmGenreDAO;
    private final MpaFilmDAO mpaFilmDAO;
    private final LikeDAO likeDAO;
    private final DirectorDAO directorDAO;
    private final JdbcTemplate jdbcTemplate;
    private final FilmDirectorDAO filmDirectorDAO;

    @Autowired
    public FilmDAOImpl(JdbcTemplate jdbcTemplate
            , FilmGenreDAO filmGenreDAO
            , MpaFilmDAO mpaFilmDAO
            , LikeDAO likeDAO
            , DirectorDAO directorDAO
            , FilmDirectorDAO filmDirectorDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDAO = filmGenreDAO;
        this.mpaFilmDAO = mpaFilmDAO;
        this.likeDAO = likeDAO;
        this.directorDAO = directorDAO;
        this.filmDirectorDAO  = filmDirectorDAO;
    }

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
            filmGenreDAO.addFilmGenreRecord(film.getId(), film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        if (!isNull(film.getDirectors())) {
            for (Director director : film.getDirectors()) {
                if (!directorDAO.isDirectorExists(director.getId())) {
                    throw new EntityNotFoundException(String.format("Режиссёр с идентификатором %d не найден", director.getId()));
                }
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
        String statement = "SELECT * "
                + "FROM films "
                + "WHERE id = ?";

        List<Film> filmList = jdbcTemplate.query(statement, new FilmMapper(), filmId);
        return !filmList.isEmpty();
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
                    .collect(Collectors.toList()));
            film.getGenres().stream().forEach(genre -> filmGenreDAO.addFilmGenreRecord(film.getId(), List.of(genre)));
        }


        filmDirectorDAO.deleteRecords(film.getId());
        if (!isNull(film.getDirectors())) {
            for (Director director : film.getDirectors()) {
                if (!directorDAO.isDirectorExists(director.getId())) {
                    throw new EntityNotFoundException(String.format("Режиссёр с идентификатором %d не найден", director.getId()));
                }
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
    public boolean isFilmAlreadyHaveLikeFromUser(Integer filmId, Integer userId) {
        String statement = "SELECT user_id "
                + "FROM likes "
                + "WHERE film_id = ?";

        ResultSetExtractor<List<Integer>> extractor = rs -> {
            List<Integer> list = new ArrayList<>();
            while (rs.next()) {
                list.add(rs.getInt("user_id"));
            }
            return list;
        };
        List<Integer> idList = jdbcTemplate.query(statement, extractor, filmId);
        return idList.contains(userId);

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
