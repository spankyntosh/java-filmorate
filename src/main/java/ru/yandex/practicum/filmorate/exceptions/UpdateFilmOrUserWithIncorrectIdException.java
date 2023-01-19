package ru.yandex.practicum.filmorate.exceptions;

public class UpdateFilmOrUserWithIncorrectIdException extends RuntimeException {
    public UpdateFilmOrUserWithIncorrectIdException(String message) {
        super(message);
    }
}
