package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("login", this.login);
        map.put("name", this.name);
        map.put("birthday", this.birthday);
        map.put("email", this.email);
        return map;
    }
}
