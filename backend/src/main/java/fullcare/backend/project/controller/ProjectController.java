package fullcare.backend.project.controller;

import fullcare.backend.global.State;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.dto.request.ProjectCreateRequest;
import fullcare.backend.project.dto.request.ProjectStateUpdateRequest;
import fullcare.backend.project.dto.request.ProjectUpdateRequest;
import fullcare.backend.project.dto.response.ProjectListResponse;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.project.dto.response.ProjectMemberListResponse;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/auth/project")
@RestController
@RequiredArgsConstructor
@Tag(name = "프로젝트", description = "프로젝트 관련 API")
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;
    @Operation(method = "get", summary = "프로젝트 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 멤버 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProjectMemberListResponse.class))})
    })
    @GetMapping("/memberList")
    public ResponseEntity<?> projectMemberList(@RequestParam(name="project_id") Long projectId){
        List<ProjectMemberListResponse> response = projectService.findProjectMembers(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(method = "get", summary = "사용자 프로젝트 리스트")

    @ApiResponses(value = {
            @ApiResponse(description = "사용자 프로젝트 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProjectListResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> list(CustomPageRequest pageRequest, @Valid @RequestParam List<State> state, @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        Page<ProjectListResponse> responses = projectService.findMyProjectList(pageable, member.getId(), state);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    @Operation(method = "post", summary = "프로젝트 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 생성 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    @PostMapping
    public ResponseEntity create(@RequestBody ProjectCreateRequest projectCreateRequest, @CurrentLoginMember Member member) {

        projectService.createProject(member.getId(), projectCreateRequest);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity delete(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        // 삭제 권한이 있는지 검사

        projectService.deleteProject(projectId);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/{projectId}")
    @Operation(method = "put", summary = "프로젝트 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 변경 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity update(@PathVariable Long projectId, @Valid @RequestBody ProjectUpdateRequest projectUpdateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        projectService.update(projectId, projectUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PutMapping("/{projectId}/state") // ! 프로젝트 완료는 리더만, 모든 평가가 완료될 때 조건 필요
    @Operation(method = "put", summary = "프로젝트 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 상태 변경 성공", responseCode = "200", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity updateState(@PathVariable Long projectId, @Valid @RequestBody ProjectStateUpdateRequest projectStateUpdateRequest, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        projectService.updateState(projectId, projectStateUpdateRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    // ! 팀 탈퇴 기능 추가 필요
    // 프로젝트 생성 썸네일
    // 프로젝트 상태 변환 (진행 -> 완료)
    // 프로젝트 멤버 영입
    // 프로젝트 삭제
    // 프로젝트 업데이트


}
