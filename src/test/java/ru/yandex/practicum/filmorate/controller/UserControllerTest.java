package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Вызов метода GET: получение всех пользователей")
    void getUser() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        String response = "[{\"id\":1,\"email\":\"mail_mail3@mail.ru\",\"login\":\"login3\"," +
                "\"name\":\"name3\",\"birthday\":\"2002-03-24\",\"friends\":[]},{\"id\":2," +
                "\"email\":\"mail_mail@mail.ru\",\"login\":\"login\",\"name\":\"login\"," +
                "\"birthday\":\"2002-10-24\",\"friends\":[]}]";

        assertEquals(response, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Создание пользователя")
    void createUser() throws Exception {

        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login1\",\n" +
                                "  \"name\": \"Name name\",\n" +
                                "  \"email\": \"mail_mail@mail.ru\",\n" +
                                "  \"birthday\": \"2002-12-24\"\n" +
                                "}"))
                .andExpect(status().isOk());

        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login2\",\n" +
                                "  \"name\": \"Name name2\",\n" +
                                "  \"email\": \"mail_mail2@mail.ru\",\n" +
                                "  \"birthday\": \"2002-02-24\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUser() throws Exception {

        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"login2\",\n" +
                                "  \"name\": \"Name name2\",\n" +
                                "  \"email\": \"mail_mail2@mail.ru\",\n" +
                                "  \"birthday\": \"2002-02-24\"\n" +
                                "}"))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform((put("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"id\" : \"1\",\n" +
                                " \"login\": \"login3\",\n" +
                                " \"name\": \"name3\",\n" +
                                " \"email\": \"mail_mail3@mail.ru\",\n" +
                                " \"birthday\": \"2002-03-24\"\n" +
                                "}"))
                .andReturn();

        String response = "{\"id\":1,\"email\":\"mail_mail3@mail.ru\",\"login\":\"login3\"," +
                "\"name\":\"name3\",\"birthday\":\"2002-03-24\",\"friends\":[]}";

        assertEquals(response, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Обновление пользователя с неверным id")
    void updateUserNotValidId() throws Exception {

        mockMvc.perform((put("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"id\" : \"80\",\n" +
                                " \"login\": \"login3\",\n" +
                                " \"name\": \"Name name3\",\n" +
                                " \"email\": \"mail_mail3@mail.ru\",\n" +
                                " \"birthday\": \"2002-03-24\"\n" +
                                "}"))
                .andExpect(result -> assertEquals("Пользователь не найден для обновления.",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Создание пользователя без логина")
    void createUserWithoutLogin() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": ,\n" +
                                "  \"name\": \"Name name2\",\n" +
                                "  \"email\": \"mail_mail2@mail.ru\",\n" +
                                "  \"birthday\": \"2002-02-24\"\n" +
                                "}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Создание пользователя с пробелом в логине")
    void createUserWithSpaceInTheLogin() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login 3\",\n" +
                                " \"name\": \"Name name3\",\n" +
                                " \"email\": \"mail_mail3@mail.ru\",\n" +
                                " \"birthday\": \"2002-03-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя с символом в логине")
    void createUserWithSymbolInLogin() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login!!!\",\n" +
                                " \"name\": \"Name name3\",\n" +
                                " \"email\": \"mail_mail3@mail.ru\",\n" +
                                " \"birthday\": \"2002-03-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    void createUserWithoutName() throws Exception {
        mockMvc.perform((post("/users")).contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"\",\n" +
                                " \"email\": \"mail_mail@mail.ru\",\n" +
                                " \"birthday\": \"2002-10-24\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Создание пользователя без email")
    void createUserNotEmail() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"Name\",\n" +
                                " \"email\": ,\n" +
                                " \"birthday\": \"2002-10-24\"\n" +
                                "}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Создание пользователя с некорректным email")
    void createUserNotValidEmail() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"Name name\",\n" +
                                " \"email\": \"фыыфяяя?mail.ru\",\n" +
                                " \"birthday\": \"2002-10-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя без указанного дня рождения")
    void createUserNotBirthday() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"Name name\",\n" +
                                " \"email\": \"mail_mail@mail.ru\",\n" +
                                " \"birthday\": \n" +
                                "}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Создание пользователя c неверной датой рождения")
    void createUserWithAnIncorrectBirthday() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"Name name\",\n" +
                                " \"email\": \"mail_mail@mail.ru\",\n" +
                                " \"birthday\": \"2202-10-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }
}