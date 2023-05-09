package fullcare.backend.project.controller;

import fullcare.backend.project.domain.State;
import fullcare.backend.project.dto.ProjectCreateRequest;
import fullcare.backend.project.dto.ProjectListResponse;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.security.jwt.CurrentLoginUser;
import fullcare.backend.security.oauth2.domain.CustomOAuth2User;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/auth/project")
@RestController
@RequiredArgsConstructor
@Tag(name = "project", description = "프로젝트 API")
public class ProjectController {
    private final ProjectService projectService;

    @Operation(method = "get", summary = "프로젝트 리스트")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectListResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> list(CustomPageRequest pageRequest, @RequestBody List<State> states, @CurrentLoginUser CustomOAuth2User user) {
        System.out.println("states.get(0) = " + states.get(0));
        Long memberId = Long.parseLong(user.getName());
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<ProjectListResponse> responses = projectService.findProjectList(pageable, memberId, states);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    @Operation(method = "post", summary = "프로젝트 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 생성 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity create(@RequestBody ProjectCreateRequest projectCreateRequest, @CurrentLoginUser CustomOAuth2User user) {
        Long memberId = Long.parseLong(user.getName());

        projectService.createProject(memberId, projectCreateRequest);
        
        return new ResponseEntity(HttpStatus.OK);
    }
}
