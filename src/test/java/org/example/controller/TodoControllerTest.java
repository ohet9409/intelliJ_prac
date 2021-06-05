package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

//컨트롤러를 활용한 테스트는 WebMvcTest 사용
@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mvc; // mvc 선언

    @MockBean
    TodoService todoService;

    private TodoEntity expected;

//    테스트 진행하기 전에 실행
    @BeforeEach
    void setup() {
//        초기화
        this.expected = new TodoEntity();
        this.expected.setId(123L);
        this.expected.setTitle("Test Title");
        this.expected.setOrder(0L);
        this.expected.setCompleted(false);
    }
    @Test
    void create() throws Exception {
        Mockito.when(this.todoService.add(ArgumentMatchers.any(TodoRequest.class)))
                .then((i) -> {
                    TodoRequest request = i.getArgument(0,TodoRequest.class);
                    return new TodoEntity(this.expected.getId(),
                                          request.getTitle(),
                                          this.expected.getOrder(),
                                          this.expected.getCompleted());
                });

        TodoRequest request = new TodoRequest();
        request.setTitle("ANY TITLE");

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        this.mvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("ANY TITLE"));
    }

    @Test
    void readOne() {
    }
}