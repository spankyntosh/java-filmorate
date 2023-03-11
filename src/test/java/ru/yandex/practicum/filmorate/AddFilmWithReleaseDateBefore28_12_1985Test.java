package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.Utils.readFile;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class AddFilmWithReleaseDateBefore28_12_1985Test {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Попытка добавит новый фильм с датой релиза раньше 28 декабря 1895")
    public void addFilmWithReleaseDateBefore28_12_1985() throws Exception {
        String body = readFile("src/test/resources/films/addFilmWithIncorrectReleaseDate.json");
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        this.mockMvc.perform(builder)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors[0]", is("Введена дата релиза фильма до 28 декабря 1895 года")));
    }
}
