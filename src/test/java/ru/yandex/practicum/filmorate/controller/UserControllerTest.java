package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Вызов метода GET: получение всех пользователей")
    void getUserTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        String response = "[{\"id\":1,\"email\":\"mail_mail3@mail.ru\",\"login\":\"login3\"," +
                "\"name\":\"Name name3\",\"birthday\":\"2002-03-24\"},{\"id\":2,\"email\":\"mail_mail@mail.ru\"," +
                "\"login\":\"login1\",\"name\":\"Name name\",\"birthday\":\"2002-12-24\"}," +
                "{\"id\":3,\"email\":\"mail_mail2@mail.ru\",\"login\":\"login2\"," +
                "\"name\":\"Name name2\",\"birthday\":\"2002-02-24\"}]";

        assertEquals(response, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Создание пользователя")
    void creatUserTest() throws Exception {

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
    void updateUserTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform((put("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"id\" : \"1\",\n" +
                                " \"login\": \"login3\",\n" +
                                " \"name\": \"Name name3\",\n" +
                                " \"email\": \"mail_mail3@mail.ru\",\n" +
                                " \"birthday\": \"2002-03-24\"\n" +
                                "}"))
                .andReturn();

        String response = "{\"id\":1,\"email\":\"mail_mail3@mail.ru\",\"login\":\"login3\"," +
                "\"name\":\"Name name3\",\"birthday\":\"2002-03-24\"}";

        assertEquals(response, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Обновление пользователя с неверным id")
    void updateUserNotTrueIdTest() throws Exception {
        mockMvc.perform((put("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"id\" : \"80\",\n" +
                                " \"login\": \"login3\",\n" +
                                " \"name\": \"Name name3\",\n" +
                                " \"email\": \"mail_mail3@mail.ru\",\n" +
                                " \"birthday\": \"2002-03-24\"\n" +
                                "}"))
                .andExpect(result -> assertEquals("Id пользователя не найдено.",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Создание пользователя без логина")
    void createUserNotLoginTest() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"\",\n" +
                                "  \"name\": \"Name name2\",\n" +
                                "  \"email\": \"mail_mail2@mail.ru\",\n" +
                                "  \"birthday\": \"2002-02-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя с пробелом в логине")
    void createUserWithSpaceInTheLoginTest() throws Exception {
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
    @DisplayName("Создание пользователя с пробелом в логине")
    void createUserWithSymbolInTheLoginTest() throws Exception {
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
    void createUserNotNameTest() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
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
    void createUserNotEmailTest() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"\",\n" +
                                " \"email\": \"\",\n" +
                                " \"birthday\": \"2002-10-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя с некорректным email")
    void createUserNotTrueEmailTest() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"Name name\",\n" +
                                " \"email\": \"mail_яяя?@mail.ru\",\n" +
                                " \"birthday\": \"\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя без указанного дня рождения")
    void createUserNotBirthbayTest() throws Exception {
        mockMvc.perform((post("/users"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"login\": \"login\",\n" +
                                " \"name\": \"Name name\",\n" +
                                " \"email\": \"mail_mail@mail.ru\",\n" +
                                " \"birthday\": \"\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя c неверной датой рождения")
    void creatUserWithAnIncorrectBirthbayTest() throws Exception {
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