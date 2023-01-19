package ru.yandex.practicum.filmorate.exceptions;

public class UserOrFilmAlreadyExistException extends RuntimeException {

    public UserOrFilmAlreadyExistException(String message) {
        super(message);
    }
}
