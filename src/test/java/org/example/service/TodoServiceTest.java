package org.example.service;

import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private  TodoService todoService;

    @Test
    void add() {
//        todoRepository가 save 메소드를 호출하고 TodoEntity 값을 받으면 받은 entity 값을 반환
        Mockito.when(this.todoRepository.save(Mockito.any(TodoEntity.class)))
                .then(AdditionalAnswers.returnsFirstArg());
//      title 명을 지정
        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test Title");
//      entity값 반환
        TodoEntity actual = this.todoService.add(expected);
//        서로 타이틀이 동일한지 확인
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void searchById() {
        TodoEntity entity = new TodoEntity();
        entity.setId(123L);
        entity.setTitle("Title");
        entity.setOrder(0L);
        entity.setCompleted(false);
        Optional<TodoEntity> optional = Optional.of(entity);

//      어떤한 findById값을 받더라도 return 값을 반환
        BDDMockito.given(this.todoRepository.findById(ArgumentMatchers.anyLong()))
                            .willReturn(optional);

        TodoEntity actual =  this.todoService.searchById(123L);

        TodoEntity expected = optional.get();
//
//        같은 값인지 비교
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getOrder(), actual.getOrder());
        assertEquals(expected.getCompleted(), actual.getCompleted());
    }
    
    @Test
    public void searchByIdFailed(){
//        에러 발생 유도
        BDDMockito.given(this.todoRepository.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.empty());
        
        assertThrows(ResponseStatusException.class, () -> {
            this.todoService.searchById(123L);
        });
    }
}