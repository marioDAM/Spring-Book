package com.example.library.book.controllers;

import com.example.library.book.dto.books.BookDTO;
import com.example.library.book.dto.books.CreateBookDTO;
import com.example.library.book.models.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRestControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    Book book = Book.builder()
            .id(1L)  // El id de la BBDD
            .name("Zumo de Naranja")
            .author("autor de prueba")
            .ISBN(955)
            .build();
    @Autowired
    private JacksonTester<CreateBookDTO> jsonCreateBookDTO;
    @Autowired
    private JacksonTester<BookDTO> jsonBookDTO;

    @Test
    @Order(1)
    public void findAllTest() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                        get("/rest/books/")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        List<BookDTO> myObjects = Arrays.asList(mapper.readValue(response.getContentAsString(), BookDTO[].class));

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.OK.value()),
                () -> assertTrue(response.getContentAsString().contains("\"id\":" + book.getId())),
                () -> assertTrue(myObjects.size() > 0),
                () -> assertEquals(myObjects.get(0).getId(), book.getId()),
                () -> assertEquals(myObjects.get(0).getName(), book.getName()),
                () -> assertEquals(myObjects.get(0).getAuthor(), book.getAuthor()),
                () -> assertEquals(myObjects.get(0).getISBN(), book.getISBN())
        );
    }

    @Test
    @Order(2)
    public void findByIdTest() throws Exception {

        var response = mockMvc.perform(
                        get("/rest/books/" + book.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        var res = jsonBookDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN())
        );
    }

    @Test
    @Order(3)
    public void saveTest() throws Exception {
        var createBookDTO = CreateBookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();


        var response = mockMvc.perform(MockMvcRequestBuilders.post("/rest/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateBookDTO.write(createBookDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        var res = jsonBookDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN()),
                () -> assertEquals(res.getName(), createBookDTO.getName()),
                () -> assertEquals(res.getAuthor(), createBookDTO.getAuthor()),
                () -> assertEquals(res.getISBN(), createBookDTO.getISBN())
        );
    }

    @Test
    @Order(4)
    public void updateTest() throws Exception {
        var bookDTO = BookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        var response = mockMvc.perform(put("/rest/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBookDTO.write(bookDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        var res = jsonBookDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN()),
                () -> assertEquals(res.getName(), bookDTO.getName()),
                () -> assertEquals(res.getAuthor(), bookDTO.getAuthor()),
                () -> assertEquals(res.getISBN(), bookDTO.getISBN())
        );
    }

    @Test
    @Order(5)
    public void deleteTest() throws Exception {

        var response = mockMvc.perform(MockMvcRequestBuilders.delete("/rest/books/" + book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        var res = jsonBookDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN())
        );
    }

    @Test
    @Order(6)
    public void findAllAlternativeTest() throws Exception {
        mockMvc.perform(get("/rest/books/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(book.getName())))
                .andExpect(jsonPath("$[0].author", is(book.getAuthor())))
                .andExpect(jsonPath("$[0].ISBN", is(book.getISBN())))
                .andReturn();
    }

    @Test
    @Order(7)
    public void findByIdlternativeTest() throws Exception {
        mockMvc.perform(get("/rest/books/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.ISBN", is(book.getISBN())))
                .andReturn();
    }

    @Test
    @Order(8)
    public void postAlternativeTest() throws Exception {
        var createBookDTO = CreateBookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();


        var json = jsonCreateBookDTO.write(createBookDTO).getJson();

        mockMvc.perform(post("/rest/books/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.ISBN", is(book.getISBN())))
                .andReturn();
    }

    @Test
    @Order(9)
    public void updateAlternativeTest() throws Exception {
        var bookDTO = BookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        var json = jsonBookDTO.write(bookDTO).getJson();

        mockMvc.perform(put("/rest/books/" + book.getId())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.ISBN", is(book.getISBN())))
                .andReturn();
    }

    @Test
    @Order(10)
    public void deleteAlternativeTest() throws Exception {
        mockMvc.perform(delete("/rest/books/" + book.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(book.getName())))
                .andExpect(jsonPath("$.author", is(book.getAuthor())))
                .andExpect(jsonPath("$.ISBN", is(book.getISBN())))
                .andReturn();
    }*/
//}
