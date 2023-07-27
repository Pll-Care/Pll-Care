package fullcare.backend.project.controller;

import fullcare.backend.global.State;
import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.dto.SuccessResponse;
import fullcare.backend.global.errorcode.ProjectErrorCode;
import fullcare.backend.global.exceptionhandling.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.request.*;
import fullcare.backend.project.dto.response.*;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageImpl;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "프로젝트", description = "프로젝트 관련 API")
@RequestMapping("/api/auth/project")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    // ! API 요청시에 필수적으로 필요한 값이라면 PathVariable, 필수가 아니거나 값이 많으면 RequestParam
    // TODO POST? PUT? PATCH?

    // * 새로운 프로젝트 생성
    @Operation(method = "post", summary = "프로젝트 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 생성 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ProjectCreateRequest projectCreateRequest, @CurrentLoginMember Member member) {
        projectService.createProject(member, projectCreateRequest);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    // * 특정 프로젝트 내용 수정
    @Operation(method = "put", summary = "프로젝트 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 수정 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{projectId}")
    public ResponseEntity update(@PathVariable Long projectId, @Valid @RequestBody ProjectUpdateRequest projectUpdateRequest, @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 수정 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_MODIFY);
        }

        // ? project 엔티티를 직접 넘겨주는 건 문제가 없을까? -> 생성 삭제 수정 등 많은 service 단 함수에 적용되는 고민거리(추가 쿼리가 1번 더 나감)
        projectService.updateProject(findProjectMember.getProject().getId(), projectUpdateRequest);
        return new ResponseEntity(new ProjectUpdateResponse(projectUpdateRequest.getImageUrl()), HttpStatus.OK);
    }

    // * 특정 프로젝트 삭제
    @Operation(method = "delete", summary = "프로젝트 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 삭제 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 삭제 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity delete(@PathVariable Long projectId, @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 삭제 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_DELETE);
        }

        projectService.deleteProject(findProjectMember.getProject().getId()); // todo id 넘겨주기 or 엔티티 넘겨주기
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 완료 처리
    @Operation(method = "post", summary = "프로젝트 완료 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 완료 처리 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 완료 처리 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{projectId}/complete")
    public ResponseEntity complete(@PathVariable Long projectId, @CurrentLoginMember Member member) {

        // ! try cat 문을 통해서 ErrorCode를 변환해줘야할 수도 있음
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 상태 수정 권한이 없음 -> 403 Forbidden
        if (!findProjectMember.isLeader()) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트에 대한 완료 권한이 없습니다.(리더가 아님)"
        }

        projectService.completeProject(findProjectMember.getProject().getId());
        return new ResponseEntity(new ProjectUpdateStateResponse(projectId), HttpStatus.OK);
    }

    // * 특정 프로젝트 조회
    @Operation(method = "get", summary = "프로젝트 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 단건 조회 성공", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProjectDetailResponse.class))),
            @ApiResponse(responseCode = "400", description = "프로젝트 단건 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<?> details(@PathVariable Long projectId,
                                     @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), true);
        Project findProject = findProjectMember.getProject();

        ProjectDetailResponse projectDetailResponse = ProjectDetailResponse.builder()
                .title(findProject.getTitle())
                .description(findProject.getDescription())
                .startDate(findProject.getStartDate())
                .endDate(findProject.getEndDate())
                .imageUrl(findProject.getImageUrl())
                .state(findProject.getState())
                .build();

        return new ResponseEntity<>(projectDetailResponse, HttpStatus.OK);
    }


    // * 사용자가 속한 프로젝트 목록
    @Operation(method = "get", summary = "사용자 프로젝트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로젝트 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "사용자 프로젝트 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<CustomPageImpl<ProjectListResponse>> list(CustomPageRequest pageRequest, @RequestParam List<State> state, @CurrentLoginMember Member member) {

        PageRequest of = pageRequest.of();
        Pageable pageable = (Pageable) of;
        CustomPageImpl<ProjectListResponse> responses = projectService.findProjectList(pageable, member.getId(), state);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // * 사용자가 속한 프로젝트 목록(간단한 버전)
    @Operation(method = "get", summary = "사용자 프로젝트 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 프로젝트 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "사용자 프로젝트 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/simplelist")
    public ResponseEntity<SuccessResponse<List<ProjectSimpleListResponse>>> projectList(@CurrentLoginMember Member member) {
        List<ProjectSimpleListResponse> result = projectService.findSimpleProjectList(member.getId());
        SuccessResponse<List<ProjectSimpleListResponse>> responses = new SuccessResponse<>(result);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // ! ============================================== 여기부터는 프로젝트에 소속된 사용자와 관련된 API ==============================================

    // * 프로젝트에 소속된 멤버의 역할 수정
    @Operation(method = "put", summary = "프로젝트 멤버 역할 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 역할 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 역할 수정 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{projectId}/positionchange")
    public ResponseEntity updateProjectMemberPosition(@PathVariable Long projectId, @RequestBody ProjectMemberPositionUpdateRequest projectMemberPositionUpdateRequest, @CurrentLoginMember Member member) {
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 멤버 역할 수정 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트 멤버 역할 수정 권한이 없습니다.(리더가 아님)"
        }

        projectMemberService.updateProjectMemberPosition(findProjectMember.getProject().getId(), projectMemberPositionUpdateRequest.getMemberId(), projectMemberPositionUpdateRequest.getPosition());

        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트 멤버에게 리더 위임
    @Operation(method = "put", summary = "프로젝트 리더 위임")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 리더 위임 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 리더 위임 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{projectId}/leaderchange")
    public ResponseEntity changeProjectLeader(@PathVariable Long projectId, @RequestBody ProjectLeaderChangeRequest projectLeaderChangeRequest, @CurrentLoginMember Member member) {
        // todo 리더는 팀에서 한 명만 존재해야한다.
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 리더 위임 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트 리더 위임 권한이 없습니다.(리더가 아님)"
        }

        projectMemberService.changeProjectLeader(findProjectMember.getProject().getId(), projectLeaderChangeRequest.getMemberId(), findProjectMember.getMember().getId());

        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 멤버 탈퇴 (본인이 탈퇴)
    @Operation(method = "delete", summary = "프로젝트 멤버 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 탈퇴 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 탈퇴 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{projectId}/selfout")
    public ResponseEntity selfOutProjectMember(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 리더는 스스로 탈퇴가 불가능함. 누군가에게 리더를 위임해야한다.
        if (findProjectMember.isLeader()) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트 리더는 스스로 탈퇴할 수 없습니다."
        }

        projectMemberService.deleteProjectMember(findProjectMember.getProject().getId(), findProjectMember.getMember().getId());
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 특정 프로젝트 멤버 삭제 (리더가 강퇴)
    @Operation(method = "delete", summary = "프로젝트 멤버 강퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 강퇴 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 강퇴 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{projectId}/kickout")
    public ResponseEntity kickOutProjectMember(@PathVariable Long projectId, @RequestBody ProjectMemberDeleteRequest projectMemberDeleteRequest, @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 강퇴 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트 멤버 강퇴 권한이 없습니다.(리더가 아님)"
        } else {
            if (findProjectMember.getMember().getId() == projectMemberDeleteRequest.getMemberId()) {
                throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트 리더는 자신을 강퇴할 수 없습니다."
            }
        }

        projectMemberService.deleteProjectMember(findProjectMember.getProject().getId(), projectMemberDeleteRequest.getMemberId());
        return new ResponseEntity(HttpStatus.OK);
    }


    // * 프로젝트 지원자 수락(리더가 수락)
    @Operation(method = "post", summary = "프로젝트 지원자 수락")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원자 수락 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원자 수락 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{projectId}/applyaccept")
    public ResponseEntity acceptApply(@PathVariable Long projectId, @RequestBody ApplyMemberAcceptRequest applyMemberAcceptRequest, @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 지원자 수락 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트 멤버 추가 권한이 없습니다.(리더가 아님)"
        }

        projectMemberService.acceptApplyMember(applyMemberAcceptRequest.getPostId(), applyMemberAcceptRequest.getMemberId());
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 프로젝트 지원자 거절(리더가 거절)
    @Operation(method = "post", summary = "프로젝트 지원자 거절")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원자 거절 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원자 거절 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{projectId}/applyreject")
    public ResponseEntity rejectApply(@PathVariable Long projectId, @RequestBody ApplyMemberRejectRequest applyMemberRejectRequest, @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), false);

        // ! 프로젝트에 소속되어있지만 리더가 아니라서 지원자 거절 권한이 없음 -> 403 Forbidden
        if (!(findProjectMember.isLeader())) {
            throw new InvalidAccessException(ProjectErrorCode.INVALID_ACCESS); // todo "프로젝트 멤버 추가 권한이 없습니다.(리더가 아님)"
        }

        projectMemberService.rejectApplyMember(applyMemberRejectRequest.getPostId(), applyMemberRejectRequest.getMemberId());
        return new ResponseEntity(HttpStatus.OK);
    }

    // * 특정 프로젝트의 멤버 목록
    @Operation(method = "get", summary = "프로젝트 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 멤버 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 멤버 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{projectId}/memberlist")
    public ResponseEntity<List<ProjectMemberListResponse>> projectMemberList(@PathVariable Long projectId, @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), true);

        List<ProjectMemberListResponse> response = projectMemberService.findProjectMembers(findProjectMember.getProject().getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // * 특정 프로젝트의 지원 목록
    @Operation(method = "get", summary = "프로젝트 지원 현황 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 지원 현황 리스트 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "프로젝트 지원 현황 리스트 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{projectId}/applylist")
    public ResponseEntity<List<ApplyMemberListResponse>> applyMemberList(@PathVariable Long projectId, @CurrentLoginMember Member member) {

        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), true);

        List<ApplyMemberListResponse> response = projectMemberService.findApplyMembers(findProjectMember.getProject().getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }


    // * 특정 프로젝트의 완료 여부
    @Operation(method = "get", summary = "프로젝트 완료 여부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 완료 여부 조회 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 완료 여부 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{projectId}/iscompleted")
    public ResponseEntity<ProjectCompleteResponse> isProjectCompleted(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), true);
        Project findProject = findProjectMember.getProject();

        ProjectCompleteResponse response = ProjectCompleteResponse.builder()
                .isCompleted(findProject.getState() == State.COMPLETE)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // * 특정 프로젝트의 리더 여부
    @Operation(method = "get", summary = "프로젝트 리더 여부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 리더 여부 조회 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "프로젝트 리더 여부 조회 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{projectId}/isleader")
    public ResponseEntity<ProjectLeaderResponse> isProjectLeader(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        ProjectMember findProjectMember = projectService.isProjectAvailable(projectId, member.getId(), true);

        ProjectLeaderResponse response = ProjectLeaderResponse.builder()
                .isLeader(findProjectMember.isLeader())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
