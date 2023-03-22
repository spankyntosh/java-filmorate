package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@JsonFormat(shape = STRING)
public enum Operation {

    REMOVE,
    ADD,
    UPDATE
}
