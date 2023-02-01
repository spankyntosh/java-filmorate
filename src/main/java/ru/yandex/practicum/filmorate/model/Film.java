package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.customvalidators.AfterSpecificDateValidation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {

    private Set<Integer> likes;
    private Integer id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    private String name;
    @Size(min = 10, max = 200, message = "Размер описания должен быть не больше 200 символов")
    private String description;
    @AfterSpecificDateValidation(message = "Введена дата релиза фильма до 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма не должна быть отрицательной или нулевой")
    private double duration;

}
