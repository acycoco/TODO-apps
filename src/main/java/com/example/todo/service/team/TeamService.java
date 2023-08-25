package com.example.todo.service.team;

import com.example.todo.domain.entity.MemberEntity;
import com.example.todo.domain.entity.TeamEntity;
import com.example.todo.domain.entity.user.User;
import com.example.todo.domain.repository.MemberRepository;
import com.example.todo.domain.repository.TeamReposiotry;
import com.example.todo.domain.repository.user.UserRepository;
import com.example.todo.dto.TeamCreateDto;
import com.example.todo.dto.TeamJoinDto;
import com.example.todo.dto.TeamUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j // 나중에 지우기
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamReposiotry teamReposiotry;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    public void createTeam(Long userId, TeamCreateDto teamCreateDto) {

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");

        User manager = optionalUser.get();
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setName(teamCreateDto.getName());
        teamEntity.setDescription(teamCreateDto.getDescription());
        teamEntity.setJoinCode(teamCreateDto.getJoinCode());
        teamEntity.setManager(manager);
        teamEntity = teamReposiotry.save(teamEntity);

        // manager를 멤버로 추가
        MemberEntity member = new MemberEntity();
        member.setTeam(teamEntity);
        member.setUser(manager);
        memberRepository.save(member);

    }

    @Transactional
    public void joinTeam(Long userId, TeamJoinDto teamJoinDto, Long teamId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        if (!team.getJoinCode().equals(teamJoinDto.getJoinCode())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong JoinCode!");

        if (memberRepository.findByTeamAndUser(team, user).isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 해당 팀에 가입했음!");

        MemberEntity member = new MemberEntity();
        member.setTeam(team);
        member.setUser(user);
        memberRepository.save(member);

        team.getMember().add(member);
        teamReposiotry.save(team);
    }

    public void updateTeamDetails(Long userId, TeamUpdateDto teamUpdateDto, Long teamId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        if (team.getManager().getId() != user.getId()) {
            log.info("현 관리자가 아닙니다!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현 관리자가 아닙니다.");
        }

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
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        if (team.getManager().getId() != user.getId()) {
            log.info("현 관리자가 아닙니다!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현 관리자가 아닙니다.");
        }
        teamReposiotry.delete(team);
    }
    @Transactional
    public void leaveTeam(Long userId, Long teamId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저 존재 X");
        User user = optionalUser.get();

        Optional<TeamEntity> optionalTeamEntity = teamReposiotry.findById(teamId);
        if (optionalTeamEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀 존재 X");
        TeamEntity team = optionalTeamEntity.get();

        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByTeamAndUser(team, user);
        if (optionalMemberEntity.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 팀에 가입X");
        MemberEntity member = optionalMemberEntity.get();

        team.getMember().remove(member);
        teamReposiotry.save(team);

        memberRepository.delete(member);
    }
}
