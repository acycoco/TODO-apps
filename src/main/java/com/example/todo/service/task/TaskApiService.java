package com.example.todo.service.task;

import com.example.todo.api.notification.NotificationController;
import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.TaskApiEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TaskApiRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.NotificationDto;
import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.task.TaskAndTeamDto;
import com.example.todo.dto.task.TaskApiDto;
import com.example.todo.dto.task.TaskCreateDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskApiService {
    private final TaskApiRepository taskApiRepository;
    private final TeamReposiotry teamReposiotry;
    private final NotificationController notificationController;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    //조직이 존재하는지 확인하는 메소드
    public TeamEntity getTeamById(Long teamId) {
        //해당 Id를 가진 Entity가 존재하는지?
        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty())
            throw new TodoAppException(ErrorCode.NOT_FOUND_TEAM);
        return optionalTeamEntity.get();
    }

    //업무 존재하는지 확인하는 메소드
    public TaskApiEntity getTaskById(Long taskId) {
        Optional<TaskApiEntity> optionalTaskApiEntity = taskApiRepository.findById(taskId);
        if (optionalTaskApiEntity.isPresent())
            return optionalTaskApiEntity.get();
        else throw new TodoAppException(ErrorCode.NOT_FOUND_TASK);
    }

    //멤버인지 확인
    public boolean isMemberOfTeam(Long userId, Long teamId) {
        TeamEntity teamEntity = getTeamById(teamId);
        List<MemberEntity> members = teamEntity.getMembers();

        for (MemberEntity member : members) {
            if (member.getUser().getId().equals(userId)) {
                return true; // 사용자가 해당 팀의 멤버인 경우 true 반환
            }
        }
        return false; // 사용자가 해당 팀의 멤버가 아닌 경우 false 반환
    }

    //업무 등록
    public ResponseDto createTask(Long userId, Long teamId, TaskCreateDto taskCreateDto) {
        log.info("TaskApiService createTask1");
        //팀 존재 확인
        TeamEntity teamEntity = getTeamById(teamId);

        TaskApiEntity taskApiEntity = new TaskApiEntity();
        //조직이 존재하는지 확인
        getTeamById(teamId);
        // 사용자가 해당 팀의 멤버인지 확인
        if (!isMemberOfTeam(userId, teamId)) throw new TodoAppException(ErrorCode.NOT_MATCH_MEMBERID);

        // 담당자 멤버 존재 확인
        User worker = userRepository.findByUsername(taskCreateDto.getWorker()).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        Long workerUserId = worker.getId();
        if (!isMemberOfTeam(workerUserId, teamId)) throw new TodoAppException(ErrorCode.NOT_MATCH_MEMBERID);
        MemberEntity workerMember = memberRepository.findByTeamAndUser(teamEntity,worker).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_MEMBER));

        taskApiEntity.setUserId(userId);
        taskApiEntity.setTeam(teamEntity);
        taskApiEntity.setTaskName(taskCreateDto.getTaskName());
        taskApiEntity.setTaskDesc(taskCreateDto.getTaskDesc());
        taskApiEntity.setStartDate(taskCreateDto.getStartDate());
        taskApiEntity.setDueDate(taskCreateDto.getDueDate());
        taskApiEntity.setMember(workerMember);
        //현재 날짜 추가
        LocalDate currentDate = LocalDate.now();

        taskApiEntity.setStatus("진행중");
        //현재날짜가 아직 startDate 이전이면 진행예정
        if (taskCreateDto.getStartDate().isAfter(currentDate)) {
            taskApiEntity.setStatus("진행예정");
        } // 현재날짜가 dueDate를 지났으면 완료
        else if (taskCreateDto.getDueDate().isBefore(currentDate)) {
            taskApiEntity.setStatus("완료");
        }
        taskApiEntity = taskApiRepository.save(taskApiEntity);
        return new ResponseDto("업무가 등록되었습니다.");
    }

    //업무 상세 조회
    public TaskApiDto readTask(Long teamId, Long taskId, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new TodoAppException(ErrorCode.NOT_FOUND_USER);

        //조직이 존재하는지 확인
        getTeamById(teamId);
        //업무가 존재하는지 확인
        Optional<TaskApiEntity> optionalTaskApiEntity = taskApiRepository.findById(taskId);
        if (optionalTaskApiEntity.isPresent())
            return TaskApiDto.fromEntity(optionalTaskApiEntity.get());
        else throw new TodoAppException(ErrorCode.NOT_FOUND_TASK);
    }

    public List<TaskApiDto> readTasksAll(Long userId, Long teamId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new TodoAppException(ErrorCode.NOT_FOUND_USER);
        //조직이 존재하는지 확인
        getTeamById(teamId);
        // 멤버 여부 확인
        if (isMemberOfTeam(userId, teamId)) throw new TodoAppException(ErrorCode.NOT_MATCH_MEMBERID);
        List<TaskApiEntity> taskApiEntities = taskApiRepository.findAllByTeamId(teamId);
        List<TaskApiDto> taskApiDtoList = new ArrayList<>();
        for (TaskApiEntity taskApiEntity : taskApiEntities) taskApiDtoList.add(TaskApiDto.fromEntity(taskApiEntity));
        return taskApiDtoList;
    }

    // 내 업무 조회
    public List<TaskAndTeamDto> getMyTasks(Long userId) {
        List<TaskAndTeamDto> myTasks = new ArrayList<>();

        // 사용자가 속한 모든 팀 조회
        List<TeamEntity> teams = teamReposiotry.findByMembersUserId(userId);

        for (TeamEntity team : teams) {
            // 팀에서 사용자의 업무 조회
            List<TaskApiEntity> tasks = taskApiRepository.findByTeamIdAndUserId(team.getId(), userId);

            for (TaskApiEntity entity : tasks) {
                TaskAndTeamDto taskAndTeamDto = new TaskAndTeamDto();
                taskAndTeamDto.setTeamId(team.getId());
                taskAndTeamDto.setTeamName(team.getName());
                taskAndTeamDto.setTaskId(entity.getId());
                taskAndTeamDto.setTaskName(entity.getTaskName());
                taskAndTeamDto.setTaskDesc(entity.getTaskDesc());
                taskAndTeamDto.setStatus(entity.getStatus());
                myTasks.add(taskAndTeamDto);
            }
        }
        return myTasks;
    }

    //업무 수정
    public ResponseDto updateTask(Long userId, Long teamId, Long taskId, TaskApiDto taskApiDto) {
        getTeamById(teamId);
        TaskApiEntity taskApiEntity = getTaskById(taskId);
        //대상 업무가 대상 팀의 업무가 맞는지
        if (!teamId.equals(taskApiEntity.getTeam().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_TASK);
        //업무를 만든 관리자인지 확인
        if (!taskApiEntity.getUserId().equals(userId)) {
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERID);
        }
        //맞다면 진행
        // 이전 상태 저장
        String previousStatus = taskApiEntity.getStatus();
        taskApiEntity.setUserId(userId);
        taskApiEntity.setTaskName(taskApiDto.getTaskName());
        taskApiEntity.setTaskDesc(taskApiDto.getTaskDesc());
        taskApiEntity.setStartDate(taskApiDto.getStartDate());
        taskApiEntity.setDueDate(taskApiDto.getDueDate());
        //현재 날짜 추가
        LocalDate currentDate = LocalDate.now();

        taskApiEntity.setStatus("진행중");
        //현재날짜가 아직 startDate 이전이면 진행예정
        if (taskApiDto.getStartDate().isAfter(currentDate)) {
            taskApiEntity.setStatus("진행예정");
        } // 현재날짜가 dueDate를 지났으면 완료
        else if (taskApiDto.getDueDate().isBefore(currentDate)) {
            taskApiEntity.setStatus("완료");
        }
        taskApiRepository.save(taskApiEntity);
        //업무 수정후 업무 상태가 이전 상태와 달라졌을때만 알림보내기
        if (!previousStatus.equals(taskApiEntity.getStatus())) {
            LocalDateTime currentTime = LocalDateTime.now(); // 현재 시간

            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setContent("'" + taskApiEntity.getTeam().getName() + "'팀의 업무'"
                    + taskApiEntity.getTaskName() + "'의 진행상황이 '" + taskApiEntity.getStatus() + "'(으)로 변경되었습니다.");
            notificationDto.setCreatedTime(currentTime);

            notificationController.updateNews(notificationDto);
        }
        return new ResponseDto("업무가 수정되었습니다.");
    }

    //업무 삭제
    public ResponseDto deleteTask(Long userId, Long teamId, Long taskId) {
        getTeamById(teamId);
        //업무 존재 확인
        TaskApiEntity taskApiEntity = getTaskById(taskId);
        //대상 업무가 대상 팀의 업무가 맞는지
        if (!teamId.equals(taskApiEntity.getTeam().getId()))
            throw new TodoAppException(ErrorCode.NOT_MATCH_TEAM_AND_TASK);
        //업무를 만든 관리자인지 확인
        if (!taskApiEntity.getUserId().equals(userId)) {
            throw new TodoAppException(ErrorCode.NOT_MATCH_USERID);
        }
        //맞다면 진행
        taskApiRepository.deleteById(taskApiEntity.getId());
        return new ResponseDto("업무를 삭제했습니다.");
    }
}