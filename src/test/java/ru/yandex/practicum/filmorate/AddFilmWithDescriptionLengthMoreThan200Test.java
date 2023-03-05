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
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class AddFilmWithDescriptionLengthMoreThan200Test {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Попытка добавит новый фильм с длиной описания больше 200 символов")
    public void addFilmWithDescriptionLengthMoreThan200() throws Exception {
        String body = readFile("src/test/resources/films/addFilmWithDescriptionLengthMoreThan200.json");
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        this.mockMvc.perform(builder)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors[0]", is("Размер описания должен быть не больше 200 символов")));
    }
}
