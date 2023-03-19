package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.customvalidators.AfterSpecificDateValidation;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
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
    private MPA mpa;
    private Collection<Genre> genres;
    private Collection<Director> directors;

    public Film(Integer id, String name, String description, LocalDate releaseDate, double duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        map.put("description", this.description);
        map.put("release_date", this.releaseDate);
        map.put("duration", this.duration);
        return map;
    }

}
