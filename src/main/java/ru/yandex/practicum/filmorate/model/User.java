package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {

    private int id;
    @Email(message = "Неверно указан формат электронной почты")
    private String email;
    @NotBlank(message = "логин пользователя не должен быть пустым")
    @Pattern(regexp = "\\S*", message = "в логине не должно содержаться пробелов")
    private String login;
    private String name;
    @Past(message = "Дата дня рождения не может быть в будущем или сегодняшним днём")
    private LocalDate birthday;
    private Set<Integer> friends;

}
