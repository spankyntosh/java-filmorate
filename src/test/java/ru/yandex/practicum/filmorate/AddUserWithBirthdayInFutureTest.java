package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AddUserWithBirthdayInFutureTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Попытка создания пользователя с датой рождения в будущем")
    public void addUserWithBirthdayInFuture() throws Exception {
        String body = readFile("src/test/resources/users/addUserWithBirthdayInFuture.json");
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body);
        this.mockMvc.perform(builder)
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.errors[0]", is("Дата дня рождения не может быть в будущем или сегодняшним днём")));
    }
}
