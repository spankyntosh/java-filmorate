package ru.yandex.practicum.filmorate.exceptions;

public class RequiredBodyFieldAbsenceException extends RuntimeException {

    public RequiredBodyFieldAbsenceException(String message) {
        super(message);
    }
}
