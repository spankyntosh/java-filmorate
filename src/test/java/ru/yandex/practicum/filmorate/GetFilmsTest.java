package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.practicum.filmorate.Utils.readFile;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class GetFilmsTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() throws Exception {
        String body = readFile("src/test/resources/films/addFilm.json");
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        this.mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    @Disabled
    @DisplayName("Получение всех имеющихся фильмов в коллекции")
    public void getFilms() throws Exception {
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/films");
        this.mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

}
