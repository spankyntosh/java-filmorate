package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int idCounter = 1;
     final Map<Integer, Film> filmsCollection = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return filmsCollection.values();
    }

    @Override
    public Film getFilmById(Integer filmId) {
        return filmsCollection.get(filmId);
    }

    @Override
    public Collection<Film> getPopularFilms(Integer count) {
        return filmsCollection.values().stream()
                .sorted((o1, o2) -> {
                    int comparison = Integer.compare(o1.getLikes().size(), o2.getLikes().size());
                    return comparison;
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(idCounter);
        film.setLikes(new HashSet<>());
        idCounter++;
        filmsCollection.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilmInfo(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(filmsCollection.get(film.getId()).getLikes());
        }
        filmsCollection.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean isFilmExists(Integer filmId) {
        return filmsCollection.containsKey(filmId);
    }

    @Override
    public boolean isFilmAlreadyHaveLikeFromUser(Integer filmId, Integer userId) {
        return filmsCollection.get(filmId).getLikes().contains(userId);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        filmsCollection.get(filmId).getLikes().add(userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        filmsCollection.get(filmId).getLikes().remove(userId);
    }

}
