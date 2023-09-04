package com.example.todo.service.todo;

import com.example.todo.domain.entity.LikeEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.LikeRepository;
import com.example.todo.domain.repository.TodoApiRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.todo.TodoApiDto;
import com.example.todo.domain.entity.TodoApiEntity;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoApiService {
    private final TodoApiRepository todoApiRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    //해당 to do가  존재하는지 확인하는 메소드
    public TodoApiEntity getTodoById(Long id) {
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(id);
        if (optionalTodoApiEntity.isEmpty())
            throw new TodoAppException(ErrorCode.NOT_FOUND_TODO);
        return optionalTodoApiEntity.get();
    }

    //해당 유저가 존재하는지 확인
    public User getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new TodoAppException(ErrorCode.NOT_FOUND_USER);
        return optionalUser.get();
    }

    @Transactional
    //to do 등록
    public ResponseDto createTodo(Long userId, TodoApiDto todoApiDto) {
        TodoApiEntity todoApiEntity = new TodoApiEntity();
        User user = getUserById(userId);

        todoApiEntity.setUser(user);
        todoApiEntity.setTitle(todoApiDto.getTitle());
        todoApiEntity.setContent(todoApiDto.getContent());
        todoApiEntity.setStartDate(todoApiDto.getStartDate());
        todoApiEntity.setDueDate(todoApiDto.getDueDate());
        //현재 날짜 추가
        LocalDate currentDate = LocalDate.now();

        todoApiEntity.setStatus("진행중");
        //현재날짜가 아직 startDate 이전이면 진행예정
        if (todoApiDto.getStartDate().isAfter(currentDate)) {
            todoApiEntity.setStatus("진행예정");
        } // 현재날짜가 dueDate를 지났으면 완료
        else if (todoApiDto.getDueDate().isBefore(currentDate)) {
            todoApiEntity.setStatus("완료");
        }

        todoApiRepository.save(todoApiEntity);

        return new ResponseDto("Todo 등록이 완료되었습니다.");
    }

    //to do 상세 조회
    public TodoApiDto readTodo(Long todoId) {
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(todoId);
        if (optionalTodoApiEntity.isPresent())
            return TodoApiDto.fromEntity(optionalTodoApiEntity.get());
        else throw new TodoAppException(ErrorCode.NOT_FOUND_TODO);
    }

    //특정 유저 to do 목록 조회
    public Page<TodoApiDto> readUserTodoAll(Long userId, Integer page, Integer limit) {
        //해당 유저가 존재하는지 확인
        Optional<TodoApiEntity> optionalTodoApiEntity = todoApiRepository.findById(userId);
        if (optionalTodoApiEntity.isEmpty())
            throw new TodoAppException(ErrorCode.NOT_FOUND_USER);
        optionalTodoApiEntity.get();
        //존재한다면 조회하기
        Pageable pageable = PageRequest.of(page, limit);
        Page<TodoApiEntity> todoApiEntities = todoApiRepository.findByUserId(userId, pageable);
        Page<TodoApiDto> todoApiDtos = todoApiEntities.map(TodoApiDto::fromEntity);
        return todoApiDtos;
    }

    //to do 수정
    public ResponseDto updateTodo(Long userId, Long todoId, TodoApiDto todoApiDto) {
        TodoApiEntity todoApiEntity = getTodoById(todoId);
        User user = getUserById(userId);

        // To do 작성자인지 확인
        if (!todoApiEntity.getUser().getId().equals(userId)) {
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERID);
        }

        todoApiEntity.setUser(user);
        todoApiEntity.setTitle(todoApiDto.getTitle());
        todoApiEntity.setContent(todoApiDto.getContent());
        todoApiEntity.setStartDate(todoApiDto.getDueDate());
        todoApiEntity.setDueDate(todoApiDto.getDueDate());
        //현재 날짜 추가
        LocalDate currentDate = LocalDate.now();

        todoApiEntity.setStatus("진행중");
        //현재날짜가 아직 startDate 이전이면 진행예정
        if (todoApiDto.getStartDate().isAfter(currentDate)) {
            todoApiEntity.setStatus("진행예정");
        } // 현재날짜가 dueDate를 지났으면 완료
        else if (todoApiDto.getDueDate().isBefore(currentDate)) {
            todoApiEntity.setStatus("완료");
        }
        todoApiRepository.save(todoApiEntity);
        return new ResponseDto("Todo가 수정 되었습니다.");
    }

    //to do 삭제
    public ResponseDto deleteTodo(Long userId, Long todoId) {
        TodoApiEntity todoApiEntity = getTodoById(todoId);

        // To do 작성자인지 확인
        if (!todoApiEntity.getUser().getId().equals(userId)) {
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERID);
        }

        todoApiRepository.deleteById(todoApiEntity.getId());
        return new ResponseDto("Todo가 삭제되었습니다.");
    }

    public boolean likeTodo(Long userId, Long todoId) {
        Optional<LikeEntity> optionalLikeEntity = likeRepository.findByUserIdAndTodoId(userId, todoId);
        boolean result;

        if (optionalLikeEntity.isPresent()) {
            likeRepository.delete(optionalLikeEntity.get());
            result = false;
        } else {
            LikeEntity likeEntity = new LikeEntity();
            likeEntity.setUserId(userId);
            likeEntity.setTodoId(todoId);
            likeRepository.save(likeEntity);
            result = true;
        }
        return result;
    }
}


