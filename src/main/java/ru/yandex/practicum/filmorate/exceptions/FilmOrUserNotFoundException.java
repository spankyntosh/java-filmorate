package ru.yandex.practicum.filmorate.exceptions;

public class FilmOrUserNotFoundException extends RuntimeException {
    public FilmOrUserNotFoundException(String message) {
        super(message);
    }
}
