package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.customvalidators.NoWhitespaceValidation;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {

    int id;
    @Email(message = "Неверно указан формат электронной почты")
    String email;
    @NotBlank(message = "логин пользователя не должен быть пустым")
    @NoWhitespaceValidation(message = "в логине не должно содержаться пробелов")
    String login;
    String name;
    @Past(message = "Дата дня рождения не может быть в будущем или сегодняшним днём")
    LocalDate birthday;

}
