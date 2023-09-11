package com.example.todo.service.task;

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
import com.example.todo.dto.task.TaskCommentCreateDto;
import com.example.todo.dto.task.TaskCommentReadDto;
import com.example.todo.dto.task.TaskCommentReplyDto;
import com.example.todo.dto.task.TaskCommentUpdateDto;
import com.example.todo.service.notification.NotificationService;
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
    private final NotificationService notificationService;

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

        // 댓글을 작성한 사용자와 업무 관리자를 비교
        if (!user.getId().equals(team.getManager().getId())) {
            // 알림 메시지 생성
            String message = "'" + team.getName() + "'팀의 " + user.getUsername() + "님이'" + taskApiEntity.getTaskName() + "'에 메시지를 남겼습니다.";
            // 관리자에게 알림을 보냄
            notificationService.notify(team.getManager().getId(), message);
        }
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
    //답글 달기
    public void addReply(Long userId, Long teamId, Long taskId, Long commentId, TaskCommentReplyDto taskCommentReplyDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        Optional<TaskApiEntity> optionalTaskApiEntity = taskApiRepository.findById(taskId);
        if (optionalTaskApiEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 task 존재 X");
        TaskApiEntity taskApiEntity = optionalTaskApiEntity.get();

        Optional<TaskCommentEntity> optionalTaskCommentEntity = taskCommentRepository.findById(commentId);
        if (optionalTaskCommentEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 TaskComment가 존재X");
        TaskCommentEntity taskCommentEntity = optionalTaskCommentEntity.get();

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByTeamAndUser(team, user);
        if (optionalMemberEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀에 가입X");

        //맞다면 진행한다.
        taskCommentEntity.setReply(taskCommentReplyDto.getReply());
        taskCommentRepository.save(taskCommentEntity);

        boolean isManager = userId.equals(team.getManager().getId());
        // 답글을 단 사람이 업무 관리자가 아니라면 업무 관리자에게 알림 전송
        if (!isManager) {
            // 알림 메시지 생성
            String message = "'" + team.getName() + "'팀의 " + user.getUsername() + "님이'" + taskApiEntity.getTaskName() + "'에 메시지를 남겼습니다.";
            // 업무 관리자에게 알림 보내기
            notificationService.notify(team.getManager().getId(), message);
        } else {
            // 업무 관리자가 답글을 달았다면, 댓글을 쓴 사람에게 알림 보내기
            // 알림 메시지 생성
            String message = "'" + team.getName() + "'팀의 " + team.getManager().getUsername() + "님이'" + taskApiEntity.getTaskName() + "'에 메시지를 남겼습니다.";
            // 댓글을 쓴 사람에게 알림 보내기
            notificationService.notify(taskCommentEntity.getWriter().getId(), message);
        }

    }
}
