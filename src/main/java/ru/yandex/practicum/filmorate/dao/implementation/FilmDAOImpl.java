package ru.yandex.practicum.filmorate.dao.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.FilmGenreDAO;
import ru.yandex.practicum.filmorate.dao.LikeDAO;
import ru.yandex.practicum.filmorate.dao.MpaFilmDAO;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Component
public class FilmDAOImpl implements FilmDAO {

    private final FilmGenreDAO filmGenreDAO;
    private final MpaFilmDAO mpaFilmDAO;
    private final LikeDAO likeDAO;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDAOImpl(JdbcTemplate jdbcTemplate, FilmGenreDAO filmGenreDAO, MpaFilmDAO mpaFilmDAO, LikeDAO likeDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmGenreDAO = filmGenreDAO;
        this.mpaFilmDAO = mpaFilmDAO;
        this.likeDAO = likeDAO;
    }

    @Override
    public Collection<Film> getFilms() {

        String statement = "SELECT * "
                         + "FROM films";

        return jdbcTemplate.query(statement, new FilmMapper(mpaFilmDAO, filmGenreDAO));
    }

    @Override
    public Film getFilmById(Integer filmId) {
        String statement = "SELECT * "
                         + "FROM films "
                         + "WHERE id = ?";
        Film film = jdbcTemplate.queryForObject(statement, new FilmMapper(), filmId);

        film.setMpa(mpaFilmDAO.getMpaByFilmId(filmId));
        film.setGenres(filmGenreDAO.getFilmGenres(filmId));
        return film;
    }

    @Override
    public Film addFilmInfo(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("films").usingGeneratedKeyColumns("id");
        int filmId = insert.executeAndReturnKey(film.toMap()).intValue();
        film.setId(filmId);
        if (film.getMpa() != null) {
            mpaFilmDAO.addMpaFilmRecord(film.getId(), film.getMpa().getId());
        }
        if (film.getGenres() != null) {
            filmGenreDAO.addFilmGenreRecord(film.getId(), film.getGenres().stream().distinct().collect(Collectors.toList()));
        }
        return film;
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
        List<Integer> idList = jdbcTemplate.query(statement,extractor, filmId);
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
