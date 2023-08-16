package com.example.todo.api;

import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoApiService {
    private final TodoApiRepository todoApiRepository;

    //해당 to do가  존재하는지 확인하는 메소드
    public TodoApiEntity getTodoById(Long id) {
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(id);
        if (optionalTodoApiEntity.isEmpty())
            throw new TodoAppException(ErrorCode.NOT_FOUND_TODO);
        return optionalTodoApiEntity.get();
    }

    //to do 등록
    public ResponseDto createTodo(TodoApiDto todoApiDto) {

        TodoApiEntity todoApiEntity = new TodoApiEntity();
        todoApiEntity.setTitle(todoApiDto.getTitle());
        todoApiEntity.setContent(todoApiDto.getContent());
        todoApiEntity.setStartDate(todoApiDto.getStartDate());
        todoApiEntity.setDueDate(todoApiDto.getDueDate());
        todoApiEntity.setStatus("진행중");

        todoApiRepository.save(todoApiEntity);

        return new ResponseDto("Todo 등록이 완료되었습니다.");
    }

    //to do 상세 조회
    public TodoApiDto readTodo(Long todoId){
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(todoId);
        if(optionalTodoApiEntity.isPresent())
            return TodoApiDto.fromEntity(optionalTodoApiEntity.get());
        else throw new TodoAppException(ErrorCode.NOT_FOUND_TODO);
    }
    //특정 유저 to do 목록 조회
    public Page<TodoApiDto> readUserTodoAll(Long userId, Integer page, Integer limit){
        //해당 유저가 존재하는지 확인
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(userId);
        if (optionalTodoApiEntity.isEmpty())
            throw new TodoAppException(ErrorCode.NOT_FOUND_USER);
        optionalTodoApiEntity.get();
        //존재한다면 조회하기
        Pageable pageable = PageRequest.of(page, limit);
        Page<TodoApiEntity> todoApiEntities = todoApiRepository.findAll(pageable);
        Page<TodoApiDto> todoApiDtos = todoApiEntities.map(TodoApiDto::fromEntity);
        return todoApiDtos;
    }

    //to do 수정
    public ResponseDto updateTodo(Long todoId, TodoApiDto todoApiDto) {
        TodoApiEntity todoApiEntity = getTodoById(todoId);

        todoApiEntity.setTitle(todoApiDto.getTitle());
        todoApiEntity.setContent(todoApiDto.getContent());
        todoApiEntity.setStartDate(todoApiDto.getDueDate());
        todoApiEntity.setDueDate(todoApiDto.getDueDate());
        todoApiRepository.save(todoApiEntity);
        return new ResponseDto("Todo가 수정 되었습니다.");
    }

    //to do 삭제
    public ResponseDto deleteTodo(Long todoId, TodoApiDto todoApiDto){
        TodoApiEntity todoApiEntity = getTodoById(todoId);
        todoApiRepository.deleteById(todoApiEntity.getId());
        return new ResponseDto("Todo가 삭제되었습니다.");
    }
    public boolean likeTodoById(Long todoId) {
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(todoId);

        if (optionalTodoApiEntity.isPresent()) {
            TodoApiEntity todoApiEntity = optionalTodoApiEntity.get();
            //좋아요 +1
            todoApiEntity.addLike();
            todoApiRepository.save(todoApiEntity);
            return true;
        }
        return false;
    }

    public boolean unlikeTodoById(Long todoId) {
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(todoId);

        if (optionalTodoApiEntity.isPresent()) {
            TodoApiEntity todoApiEntity = optionalTodoApiEntity.get();
            //좋아요 +1
            todoApiEntity.removeLike();
            todoApiRepository.save(todoApiEntity);
            return true;
        }
        return false;
    }
}

