package ru.yandex.practicum.filmorate.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OnExceptionResponse {

    private String message;

    public OnExceptionResponse() {
    }

    public OnExceptionResponse(String message) {
        this.message = message;
    }
}
