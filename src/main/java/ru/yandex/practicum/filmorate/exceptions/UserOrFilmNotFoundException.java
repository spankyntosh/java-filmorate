package ru.yandex.practicum.filmorate.exceptions;

public class UserOrFilmNotFoundException extends RuntimeException {
    public UserOrFilmNotFoundException(String message) {
        super(message);
    }
}
