package com.example.todo.service.taskComment;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TaskApiEntity;
import com.example.todo.domain.entity.TaskCommentEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TaskApiRepository;
import com.example.todo.domain.repository.TaskCommentRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.taskComment.TaskCommentCreateDto;
import com.example.todo.dto.taskComment.TaskCommentReadDto;
import com.example.todo.dto.taskComment.TaskCommentUpdateDto;
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
public class TaskCommentService {
    private final TaskCommentRepository taskCommentRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final TeamReposiotry teamReposiotry;
    private final TaskApiRepository taskApiRepository;

    public void createTaskComment(Long userId, Long teamId, Long taskId, TaskCommentCreateDto taskCommentCreateDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        Optional<TaskApiEntity> optionalTaskApiEntity = taskApiRepository.findById(taskId);
        if (optionalTaskApiEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 task 존재 X");
        TaskApiEntity taskApiEntity = optionalTaskApiEntity.get();

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByTeamAndUser(team, user);
        if (optionalMemberEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀에 가입X");

        TaskCommentEntity taskCommentEntity = new TaskCommentEntity();
        taskCommentEntity.setWriter(user);
        taskCommentEntity.setContent(taskCommentCreateDto.getContent());
        taskCommentEntity.setTaskApiEntity(taskApiEntity);
        taskCommentRepository.save(taskCommentEntity);
    }

    public Page<TaskCommentReadDto> readTaskCommentsPage(Long userId, Long teamId, Long taskId, Integer page, Integer limit) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        Optional<TaskApiEntity> optionalTaskApiEntity = taskApiRepository.findById(taskId);
        if (optionalTaskApiEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 task 존재 X");
        TaskApiEntity taskApiEntity = optionalTaskApiEntity.get();

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<TaskCommentEntity> taskCommentEntityPage = taskCommentRepository.findAllByTaskApiEntity(taskApiEntity, pageable);
        Page<TaskCommentReadDto> commentDtoPage = taskCommentEntityPage.map(TaskCommentReadDto::fromEntity);
        return commentDtoPage;
    }

    public void updateTaskComment(Long userId, Long teamId, Long taskId, Long commentId, TaskCommentUpdateDto taskCommentUpdateDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        Optional<TaskApiEntity> optionalTaskApiEntity = taskApiRepository.findById(taskId);
        if (optionalTaskApiEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 task 존재 X");
        TaskApiEntity taskApiEntity = optionalTaskApiEntity.get();

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByTeamAndUser(team, user);
        if (optionalMemberEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀에 가입X");

        Optional<TaskCommentEntity> optionalTaskCommentEntity = taskCommentRepository.findById(commentId);
        if (optionalTaskCommentEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TaskComment가 존재X");
        TaskCommentEntity taskCommentEntity = optionalTaskCommentEntity.get();

        taskCommentEntity.setContent(taskCommentUpdateDto.getContent());
        taskCommentRepository.save(taskCommentEntity);
    }
}
