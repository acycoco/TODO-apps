package com.example.todo.service.team;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.UsersSubscriptionEntity;
import com.example.todo.domain.entity.enums.SubscriptionStatus;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.UsersSubscriptionRepository;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.team.TeamCreateDto;
import com.example.todo.dto.team.TeamJoinDto;
import com.example.todo.dto.team.TeamUpdateDto;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.TodoAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j // 나중에 지우기
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamReposiotry teamReposiotry;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final UsersSubscriptionRepository usersSubscriptionRepository;
    @Transactional
    public void createTeam(Long userId, TeamCreateDto teamCreateDto) {

        User manager = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        User manager = optionalUser.get();
        if (teamCreateDto.getParticipantNum() > 5) {
            UsersSubscriptionEntity usersSubscription = usersSubscriptionRepository.findByUsersAndSubscriptionStatus(manager, SubscriptionStatus.ACTIVE)
                    .orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_ACTIVE_SUBSCRIPTION));
            if ( teamCreateDto.getParticipantNum() > usersSubscription.getSubscription().getMaxMember())
                throw new TodoAppException(ErrorCode.EXCEED_ALLOWED_TEAM_MEMBERS);

        }

        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(teamCreateDto.getName());
        teamEntity.setDescription(teamCreateDto.getDescription());
        teamEntity.setJoinCode(teamCreateDto.getJoinCode());
        teamEntity.setManager(manager);
        teamEntity.setParticipantNumMax(teamCreateDto.getParticipantNum());

        // manager를 멤버로 추가
        MemberEntity member = new MemberEntity();
        member.setTeam(teamEntity);
        member.setUser(manager);

        teamEntity.setMember(new ArrayList<>());
        teamEntity.getMember().add(member);
        teamEntity.setParticipantNum(teamEntity.getMember().size());
        teamEntity = teamReposiotry.save(teamEntity);
        memberRepository.save(member);

    }

    @Transactional
    public void joinTeam(Long userId, TeamJoinDto teamJoinDto, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));

        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        if (!team.getJoinCode().equals(teamJoinDto.getJoinCode())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong JoinCode!");


        //팀 멤버수 제한
        if (team.getParticipantNum().equals(team.getParticipantNumMax())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "팀의 최대 허용 멤버 수를 초과했습니다.");
        }

        if (memberRepository.findByTeamAndUser(team, user).isPresent()) throw new TodoAppException(ErrorCode.ALREADY_USER_JOINED);

        MemberEntity member = new MemberEntity();
        member.setTeam(team);
        member.setUser(user);
        memberRepository.save(member);

        team.getMember().add(member);
        team.setParticipantNum(team.getParticipantNum()+1);
        teamReposiotry.save(team);


    }

    public void updateTeamDetails(Long userId, TeamUpdateDto teamUpdateDto, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        if (team.getManagerId() != user.getId()) throw new TodoAppException(ErrorCode.MISMATCH_MANAGERID_USERID);


        if (teamUpdateDto.getName() != null) {
            team.setName(teamUpdateDto.getName());
        }

        if (teamUpdateDto.getDescription() != null) {
            team.setDescription(teamUpdateDto.getDescription());
        }

        if (teamUpdateDto.getJoinCode() != null) {
            team.setJoinCode(teamUpdateDto.getJoinCode());
        }

        if (teamUpdateDto.getManager() != null) {
            team.setManager(teamUpdateDto.getManager());
        }
        teamReposiotry.save(team);
    }

    public void deleteTeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));

        if (team.getManagerId() != user.getId()) throw new TodoAppException(ErrorCode.MISMATCH_MANAGERID_USERID);

        teamReposiotry.delete(team);
    }
    @Transactional
    public void leaveTeam(Long userId, Long teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_USER));
        TeamEntity team = teamReposiotry.findById(teamId).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_TEAM));
        MemberEntity member = memberRepository.findByTeamAndUser(team, user).orElseThrow(() -> new TodoAppException(ErrorCode.NOT_FOUND_MEMBER));

        team.getMember().remove(member);
        team.setParticipantNum(team.getParticipantNum()-1);
        teamReposiotry.save(team);
        memberRepository.delete(member);
    }
}
