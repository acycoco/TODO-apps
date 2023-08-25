package com.example.todo.service.todo;

import com.example.todo.domain.entity.TodoApiEntity;
import com.example.todo.domain.entity.TodoCommentEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.TodoApiRepository;
import com.example.todo.domain.repository.TodoCommentRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.todo.TodoCommentCreateDto;
import com.example.todo.dto.todo.TodoCommentReadDto;
import com.example.todo.dto.todo.TodoCommentUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class TodoCommentService {
    private final UserRepository userRepository;
    private final TodoApiRepository todoApiRepository;
    private final TodoCommentRepository todoCommentRepository;
    public void createTodoComment(Long userId, Long todoId, TodoCommentCreateDto todoCommentCreateDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(todoId);
        if (optionalTodoApiEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Todo 존재 X");
        TodoApiEntity todoApiEntity = optionalTodoApiEntity.get();

        TodoCommentEntity todoCommentEntity = new TodoCommentEntity();
        todoCommentEntity.setTodoApiEntity(todoApiEntity);
        todoCommentEntity.setContent(todoCommentCreateDto.getContent());
        todoCommentEntity.setWriter(user);
        todoCommentRepository.save(todoCommentEntity);
    }

    public Page<TodoCommentReadDto> readTodoCommentsPage(Long userId, Long todoId, Integer page, Integer limit) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(todoId);
        if (optionalTodoApiEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Todo 존재 X");
        TodoApiEntity todoApiEntity = optionalTodoApiEntity.get();

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<TodoCommentEntity> todoCommentEntityPage = todoCommentRepository.findAllByTodoApiEntity(todoApiEntity, pageable);
        Page<TodoCommentReadDto> commentDtoPage = todoCommentEntityPage.map(TodoCommentReadDto::fromEntity);
        return commentDtoPage;
    }

    public void updateTodoComment(Long userId, Long todoId, Long commentId, TodoCommentUpdateDto todoCommentUpdateDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(todoId);
        if (optionalTodoApiEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 Todo 존재 X");


        Optional<TodoCommentEntity> optionalTodoCommentEntity = todoCommentRepository.findById(commentId);
        if (optionalTodoCommentEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TodoComment가 존재 X");
        TodoCommentEntity todoCommentEntity = optionalTodoCommentEntity.get();

        if (!todoCommentEntity.getWriter().equals(user)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 작성자 본인이 아닙니다.");

        todoCommentEntity.setContent(todoCommentUpdateDto.getContent());
        todoCommentRepository.save(todoCommentEntity);
    }
}
