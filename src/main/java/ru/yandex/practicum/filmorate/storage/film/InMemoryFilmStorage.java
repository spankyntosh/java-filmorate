package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int idCounter = 1;
     final Map<Integer, Film> filmsCollection = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return filmsCollection.values();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(idCounter);
        idCounter++;
        filmsCollection.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilmInfo(Film film) {
        filmsCollection.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean ifFilmExists(Film film) {
        return filmsCollection.containsKey(film.getId());
    }

}
