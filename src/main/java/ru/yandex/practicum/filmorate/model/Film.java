package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.customvalidators.AfterSpecificDateValidation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    int id;
    @NotBlank(message = "Название фильма не должно быть пустым")
    String name;
    @Size(min = 10, max = 200, message = "Размер описания не больше 200 символов")
    String description;
    @AfterSpecificDateValidation(message = "Введена дата до 28 декабря 1895 года")
    LocalDate releaseDate;
    @Positive(message = "продолжительность фильма не должна быть отрицательной или нулевой")
    double duration;


}
