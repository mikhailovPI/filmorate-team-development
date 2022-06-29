package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Вызов метода GET: получение всех фильмов")
    void getFilmTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andReturn();

        String response = "[{\"id\":1,\"name\":\"name 3\",\"description\":\"description3\"," +
                "\"releaseDate\":\"1900-12-24\",\"duration\":21},{\"id\":2,\"name\":\"name2\"," +
                "\"description\":\"description2\",\"releaseDate\":\"2000-12-24\",\"duration\":20}]";

        assertEquals(response, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Создание фильмов")
    void creatFilmTest() throws Exception {

        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description\",\n" +
                                "  \"duration\": \"2\",\n" +
                                "  \"releaseDate\": \"2002-12-24\"\n" +
                                "}"))
                .andExpect(status().isOk());

        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name2\",\n" +
                                "  \"description\": \"description2\",\n" +
                                "  \"duration\": \"20\",\n" +
                                "  \"releaseDate\": \"2000-12-24\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление фильма")
    void updateFilmTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform((put("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"id\" : \"1\",\n" +
                                "  \"name\": \"name 3\",\n" +
                                "  \"description\": \"description3\",\n" +
                                "  \"duration\": \"21\",\n" +
                                "  \"releaseDate\": \"1900-12-24\"\n" +
                                "}"))
                .andReturn();

        String response = "{\"id\":1,\"name\":\"name 3\",\"description\":\"description3\"," +
                "\"releaseDate\":\"1900-12-24\",\"duration\":21}";

        assertEquals(response, mvcResult.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Обновление фильма с неверным id")
    void updateFilmNotWithoutIdTest() throws Exception {
        mockMvc.perform((put("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"id\" : \"100\",\n" +
                                "  \"name\": \"name 3\",\n" +
                                "  \"description\": \"description3\",\n" +
                                "  \"duration\": \"21\",\n" +
                                "  \"releaseDate\": \"1900-12-24\"\n" +
                                "}"))

                .andExpect(result -> assertEquals("Id фильма не найден.",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Создание фильма без имени")
    void createFilmNotNameTest() throws Exception {
        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"description2\",\n" +
                                "  \"duration\": \"20\",\n" +
                                "  \"releaseDate\": \"2000-12-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание фильма без описания")
    void createFilmNotDescriptionTest() throws Exception {
        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"\",\n" +
                                "  \"duration\": \"20\",\n" +
                                "  \"releaseDate\": \"2000-12-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание фильма с описанием более 200 символов")
    void createFilmMaxSymbolDescriptionTest() throws Exception {
        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"ddsfsdfdsfdsfdsfoliodsfdsfdsfdsfdsfdsfdsfdsfdsfdsfdsfds" +
                                "dsfdsfdsfdsfdsfdsfdsfdgfdiolgdssahiuloilloiloiliolidsvdsvfdvfdsvdfvfdvfdvfdv" +
                                "vfdsvfdsvfdvfdvfdsvfdsvfdoiloiloiloisvfdvfdvfddfsvfdsvfdvfdsvfdsvfdsvfdsvfds" +
                                "fsvfdvfdsvfdvfdsvfdsvfdsvfloiloiloildsvfdvfdsvfdsvfdbfdbgfjkcmdsklvckdsjvdsv" +
                                "kdvndsvkjndsвысвысвысвысвысвысвысвыioloilсвысkvndsakvnsalknv;lkdsanv;dsanv;s" +
                                "avsmksadnvkjdсвысвысвысвысвысвысвысвысвысвыакурпsanvkndsavlndsa;vnsavknsa;kv" +
                                "dvnlksdnvkjsвымвыadnvsandvkjlnsadloiltyiu.lkkjvndsakjnv;kdsanvoianvalksnvksa" +
                                "sadnlkvnaskvnas;nvlkdnvkl.dmsvkmdfkmvsdmcsamcmklmsacionmavfdsmvnonj[iajsvdsa" +
                                "vksavdsaknvoinvlksdcldsakco;ksdac;;dsakc;ldsamvs.,damvmslvlssadnv;oiwav\",\n" +
                                "  \"duration\": \"20\",\n" +
                                "  \"releaseDate\": \"2000-12-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание пользователя без даты релиза")
    void createFilmNotReleaseDateTest() throws Exception {
        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description2\",\n" +
                                "  \"duration\": \"20\",\n" +
                                "  \"releaseDate\": \"\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание фильма с неверной датой релиза")
    void createFilmOldReleaseDateTest() throws Exception {
        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description2\",\n" +
                                "  \"duration\": \"20\",\n" +
                                "  \"releaseDate\": \"1000-12-24\"\n" +
                                "}"))
                .andExpect(result -> assertEquals("Дата релиза фильма не может быть раньше 1895-12-28",
                        result.getResolvedException().getMessage()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Создание фильма без продолжительности")
    void createFilmNotDurationTest() throws Exception {
        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description2\",\n" +
                                "  \"duration\": \"\",\n" +
                                "  \"releaseDate\": \"2000-12-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Создание фильма с нулевой продолжительностью")
    void createFilmZeroDurationTest() throws Exception {
        mockMvc.perform((post("/films"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"name\",\n" +
                                "  \"description\": \"description2\",\n" +
                                "  \"duration\": \"0\",\n" +
                                "  \"releaseDate\": \"2000-12-24\"\n" +
                                "}"))
                .andExpect(status().isBadRequest());
    }

}