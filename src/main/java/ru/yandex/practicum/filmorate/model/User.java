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
    @Email
    String email;
    @NotBlank
    @NoWhitespaceValidation
    String login;
    String name;
    @Past
    LocalDate birthday;


}
