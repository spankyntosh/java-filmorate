package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Review {
    private Long reviewId;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer filmId;

    @NotNull(message = "Поле Отзыв обязательно для заполнения")
    @NotBlank(message = "Текст отзыва не может быть пустым")
    private String content;

    @NotNull
    private Boolean isPositive;

    private Long useful;
}