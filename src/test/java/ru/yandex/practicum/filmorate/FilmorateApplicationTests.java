package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Order(1)
	@DisplayName("Добавление нового фильма")
	public void addNewFilm() throws Exception {
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
	@Order(2)
	@DisplayName("Обновление информации по добавленному фильму")
	public void updateFilm() throws Exception {
		String body = readFile("src/test/resources/films/updateFilm.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.put("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(content().json(body));
	}

	@Test
	@Order(3)
	@DisplayName("Получение всех имеющихся фильмов в коллекции")
	public void getFilms() throws Exception {
		RequestBuilder builder = MockMvcRequestBuilders
				.get("/films");
		this.mockMvc.perform(builder)
				.andExpect(status().isOk());
	}

	@Test
	@Order(4)
	@DisplayName("Попытка обновления информации по фильму с некорректным id")
	public void updateFilmWithIncorrectId() throws Exception {
		String body = readFile("src/test/resources/films/updateFilmWithIncorrectId.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.put("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(404))
				.andExpect(jsonPath("$.message", is("Попытка обновить информацию по фильму с несуществующим id фильма")));
	}

	@Test
	@Order(5)
	@DisplayName("Попытка добавить новый фильм с пустым значением в имени")
	public void addFilmWithEmptyName() throws Exception {
		String body = readFile("src/test/resources/films/addFilmWithEmptyName.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.errors[0]", is("Название фильма не должно быть пустым")));
	}

	@Test
	@Order(6)
	@DisplayName("Попытка добавить новый фильм со значением null в поле 'name'")
	public void addFilmWithNullName() throws Exception {
		String body = readFile("src/test/resources/films/addFilmWithNullName.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.errors[0]", is("Название фильма не должно быть пустым")));
	}


	@Test
	@Order(7)
	@DisplayName("Попытка добавит новый фильм с длиной описания больше 200 символов")
	public void addFilmWithDescriptionLengthMoreThan200() throws Exception {
		String body = readFile("src/test/resources/films/addFilmWihtDescriptionLengthMoreThan200.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.errors[0]", is("Размер описания должен быть не больше 200 символов")));
	}

	@Test
	@Order(8)
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

	@Test
	@Order(9)
	@DisplayName("Попытка добавит новый фильм с отрицательным значением продолжительности")
	public void addFilmWithNegativeDuration() throws Exception {
		String body = readFile("src/test/resources/films/addFilmWithNegativeDuration.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/films")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.errors[0]", is("продолжительность фильма не должна быть отрицательной или нулевой")));
	}

	@Test
	@Order(10)
	@DisplayName("Добавление нового пользователя")
	public void addNewUser() throws Exception {
		String body = readFile("src/test/resources/users/addUser.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(content().json(body));
	}

	@Test
	@Order(11)
	@DisplayName("Обновление информации по существующему пользователю")
	public void updateUserInfo() throws Exception {
		String body = readFile("src/test/resources/users/updateUser.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.put("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(content().json(body));
	}

	@Test
	@Order(12)
	@DisplayName("Получение списка всех пользователей")
	public void getUsers() throws Exception {
		RequestBuilder builder = MockMvcRequestBuilders
				.get("/users");
		this.mockMvc.perform(builder)
				.andExpect(status().isOk());
	}

	@Test
	@Order(13)
	@DisplayName("Обновление информации по пользователю с несуществующим id")
	public void updateUserInfoWithIncorrectId() throws Exception {
		String body = readFile("src/test/resources/users/updateUserWithIncorrectId.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.put("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(404))
				.andExpect(jsonPath("$.message", is("Попытка обновить информацию по пользователю с несуществующим id")));
	}

	@Test
	@Order(14)
	@DisplayName("Попытка создания пользователя с некорректными email")
	public void addUserWithIncorrectEmail() throws Exception {
		String body = readFile("src/test/resources/users/addUserWithIncorrectEmail.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.errors[0]", is("Неверно указан формат электронной почты")));
	}

	@Test
	@Order(15)
	@DisplayName("Попытка создания пользователя с пустым полем 'login'")
	public void addUserWithEmptyLogin() throws Exception {
		String body = readFile("src/test/resources/users/addUserWithEmptyLogin.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.errors[0]", is("логин пользователя не должен быть пустым")));
	}

	@Test
	@Order(16)
	@DisplayName("Попытка создания пользователя с пробелами в логине")
	public void addUserWithWhitespaceInLogin() throws Exception {
		String body = readFile("src/test/resources/users/addUserWithWhitespaceInLogin.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().is(400))
				.andExpect(jsonPath("$.errors[0]", is("в логине не должно содержаться пробелов")));
	}

	@Test
	@Order(17)
	@DisplayName("Создание пользователя с пустым полем 'name'")
	public void addUserWithEmptyName() throws Exception {
		String body = readFile("src/test/resources/users/addUserWIthEmptyName.json");
		RequestBuilder builder = MockMvcRequestBuilders
				.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body);
		this.mockMvc.perform(builder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("dobby_free_elf")));
	}

	@Test
	@Order(18)
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



	private String readFile(String path) {
		StringBuilder stringBuilder = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line;
			String lineSeparator = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(lineSeparator);
			}
		} catch (Exception e) {

		}
		return stringBuilder.toString();
	}
}
