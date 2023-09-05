package com.example.todo.api.team;

import com.example.todo.dto.ResponseDto;
import com.example.todo.dto.team.TeamCreateDto;
import com.example.todo.dto.team.TeamJoinDto;
import com.example.todo.dto.team.TeamOverviewDto;
import com.example.todo.dto.team.TeamUpdateDto;
import com.example.todo.service.team.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public String getTeamCreatePage(Authentication authentication) {
        return "team-create.html";
    }

    @GetMapping("/search-page")
    public String getTeamSearchPage() {
        return "team-search.html";
    }

    @GetMapping("/search?keyword=")
    public Page<TeamOverviewDto> searchTeam(@RequestParam("keyword") String keyword,
                                            @RequestParam(value = "page", defaultValue = "0") Integer page,
                                            @RequestParam(value = "limit", defaultValue = "30") Integer limit) {
        return teamService.searchTeam(keyword, page, limit);
    }


    @PostMapping
    public ResponseDto createTeam(Authentication authentication,
                                  @RequestBody TeamCreateDto teamCreateDto) {
        Long userId = Long.parseLong(authentication.getName());
        log.info(teamCreateDto.getName());
        log.info(teamCreateDto.getJoinCode());
        log.info(teamCreateDto.getDescription());
        teamService.createTeam(userId, teamCreateDto);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("새로운 팀 등록이 완료되었습니다.");
        return responseDto;

    }


    @PostMapping("/{teamId}/member")
    public ResponseDto joinTeam(Authentication authentication,
                                @RequestBody @Valid TeamJoinDto teamJoinDto,
                                @PathVariable("teamId") Long teamId) {
        Long userId = Long.parseLong(authentication.getName());
        teamService.joinTeam(userId, teamJoinDto, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀에 가입이 완료되었습니다.");
        return responseDto;
    }


    @PutMapping("/{teamId}")
    public ResponseDto updateTeamDetails(Authentication authentication,
                                         @RequestBody TeamUpdateDto teamUpdateDto,
                                         @PathVariable("teamId") Long teamId) {
        Long userId = Long.parseLong(authentication.getName());
        teamService.updateTeamDetails(userId, teamUpdateDto, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀 정보 수정이 완료되었습니다.");
        return responseDto;
    }


    @DeleteMapping("/{teamId}")
    public ResponseDto deleteTeam(Authentication authentication,
                                  @PathVariable("teamId") Long teamId) {
        Long userId = Long.parseLong(authentication.getName());
        teamService.deleteTeam(userId, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀 삭제가 완료되었습니다.");
        return responseDto;
    }


    @DeleteMapping("/{teamId}/member")
    public ResponseDto leaveTeam(Authentication authentication,
                                 @PathVariable("teamId") Long teamId) {
        Long userId = Long.parseLong(authentication.getName());
        teamService.leaveTeam(userId, teamId);

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage("팀을 탈퇴하였습니다.");
        return responseDto;
    }
}
