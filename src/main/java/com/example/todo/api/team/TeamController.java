package com.example.todo.api.team;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.TeamCreateDto;
import com.example.todo.dto.TeamJoinDto;
import com.example.todo.dto.TeamUpdateDto;
import com.example.todo.service.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public String getTeamGeneratePage(Authentication authentication) {
        return "team-generate.html";
    }

    @GetMapping("/search-page")
    public String getTeamSearchPage() {
        return "team-generate.html";
    }

    @PostMapping
    public ResponseDto createTeam(Authentication authentication,
                                  @Valid TeamCreateDto teamCreateDto) {
        teamService.createTeam(authentication, teamCreateDto);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("새로운 팀 등록이 완료되었습니다.");
        return responseDto;

    }


    @PostMapping("/{teamId}/member")
    public ResponseDto joinTeam(Authentication authentication,
                                @Valid TeamJoinDto teamJoinDto,
                                @PathVariable("teamId") Long teamId) {

        teamService.joinTeam(authentication, teamJoinDto, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀에 가입이 완료되었습니다.");
        return responseDto;
    }


    @PutMapping("/{teamId}")
    public ResponseDto updateTeamDetails(Authentication authentication,
                                         TeamUpdateDto teamUpdateDto,
                                         @PathVariable("teamId") Long teamId) {
        teamService.updateTeamDetails(authentication, teamUpdateDto, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀 정보 수정이 완료되었습니다.");
        return responseDto;
    }


    @DeleteMapping("/{teamId}")
    public ResponseDto deleteTeam(Authentication authentication,
                                  @PathVariable("teamId") Long teamId) {
        teamService.deleteTeam(authentication, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀 삭제가 완료되었습니다.");
        return responseDto;
    }


    @DeleteMapping("/{teamId}/member")
    public ResponseDto leaveTeam(Authentication authentication,
                                 @PathVariable("teamId") Long teamId) {
        teamService.leaveTeam(authentication, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀을 탈퇴하였습니다.");
        return responseDto;
    }
}
