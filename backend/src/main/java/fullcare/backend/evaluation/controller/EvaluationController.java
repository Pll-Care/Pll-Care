package fullcare.backend.evaluation.controller;


import fullcare.backend.evaluation.dto.BadgeDto;
import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.FinalEvalUpdateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.dto.response.*;
import fullcare.backend.evaluation.service.EvaluationService;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.schedule.dto.response.ScheduleMonthResponse;
import fullcare.backend.schedulemember.service.ScheduleMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import fullcare.backend.util.CustomPageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth/evaluation")
@RestController
@Tag(name="평가", description = "평가 관련 API")
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final ProjectMemberService projectMemberService;
    private final ScheduleMemberService scheduleMemberService;


    // * 중간평가 관련
    @Operation(method = "get", summary = "중간 평가 모달창 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 모달창 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = MidTermEvalModalResponse.class))})
    })
    @GetMapping("/midterm")
    public ResponseEntity midtermEvalModal(@RequestParam Long scheduleId,
                                            @CurrentLoginMember Member member) {
        if (!(scheduleMemberService.validateScheduleMember(scheduleId, member.getId()))) {
            throw new InvalidAccessException("해당 평가에 접근 권한이 없습니다.");
        }
        MidTermEvalModalResponse response = evaluationService.modal(scheduleId, member.getId());

        return new ResponseEntity(response, HttpStatus.OK);
    }
    @Operation(method = "post", summary = "중간 평가 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 생성 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping("/midterm")
    public ResponseEntity midtermEvalCreate(@RequestBody MidTermEvalCreateRequest midTermEvalCreateRequest,
                                            @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(midTermEvalCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        evaluationService.createMidtermEvaluation(midTermEvalCreateRequest, member);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "중간 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 모달창 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = MidtermDetailResponse.class))})
    })
    @GetMapping("/midterm/detail")
    public ResponseEntity<?> findMidterm(@RequestParam("project_id") Long projectId,
                                         @CurrentLoginMember Member member) {
        List<BadgeDto> response = evaluationService.findMidtermEvaluationDetailResponse(projectId, member.getId());

        return new ResponseEntity(response, HttpStatus.OK);
    }
    @Operation(method = "get", summary = "중간 평가 차트, 랭킹 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 차트, 랭킹 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = EverythingEvalResponse.class))})
    })
    @GetMapping("/midtermlist") // 디자인에서 뱃지 개수 차트 부분
    public ResponseEntity midtermEvalList(@RequestParam("project_id") Long projectId,
                                          @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        EverythingEvalResponse response = evaluationService.findMidtermEvaluationList(projectId);
        return new ResponseEntity(response, HttpStatus.OK);
    }


    // * 최종평가 관련
    @Operation(method = "post", summary = "최종 평가 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 생성 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PostMapping("/final")
    public ResponseEntity finalEvalCreate(@RequestBody FinalEvalCreateRequest finalEvalCreateRequest,
                                          @CurrentLoginMember Member member) {
        if ((evaluationService.validateFinalDuplicationAuthor(finalEvalCreateRequest.getEvaluatedId(), member.getId()))) {
            throw new InvalidAccessException("중복 평가는 불가능합니다.");
        }
        if (!(projectMemberService.validateProjectMember(finalEvalCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }
        evaluationService.createFinalEvaluation(finalEvalCreateRequest, member);
        return new ResponseEntity(HttpStatus.OK);
    }
    @Operation(method = "put", summary = "최종 평가 수정")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 수정 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @PutMapping("/final/{evaluationId}")
    public ResponseEntity finalEvalUpdate(@RequestBody FinalEvalUpdateRequest finalEvalUpdateRequest, @PathVariable Long evaluationId,
                                          @CurrentLoginMember Member member) {
        // ? 작성자가 맞는지 검증
        if (!evaluationService.validateAuthor(evaluationId, member.getId())) {
            throw new InvalidAccessException("해당 평가에 접근 권한이 없습니다.");
        }
        evaluationService.updateFinalEvaluation(evaluationId, finalEvalUpdateRequest);

        return new ResponseEntity(HttpStatus.OK);
    }
    @Operation(method = "delete", summary = "최종 평가 삭제")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 삭제 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    })
    @DeleteMapping("/final/{evaluationId}")
    public ResponseEntity finalEvalDelete(@PathVariable Long evaluationId,@Valid @RequestParam("project_id") Long projectId,
                                          @CurrentLoginMember Member member) {
        // ? 작성자가 맞는지 검증
        if (!evaluationService.validateAuthor(evaluationId, member.getId())) {
            throw new InvalidAccessException("해당 평가에 접근 권한이 없습니다.");
        }
        evaluationService.deleteFinalEvaluation(evaluationId, projectId);

        return new ResponseEntity(HttpStatus.OK);
    }
    @Operation(method = "get", summary = "최종 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = FinalEvaluationResponse.class))})
    })
    @GetMapping("/final/{evaluationId}")
    public ResponseEntity finalEvalDetails(@PathVariable Long evaluationId,
                                           @CurrentLoginMember Member member) {// ? 평가된 사람 id, 평가 id 조건 검색 해서 일치하면 접근 허용 필요
        FinalEvaluationResponse response = evaluationService.findFinalEvaluationDetailResponse(evaluationId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "최종 평가 차트, 랭킹 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 차트, 랭킹 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = EverythingEvalResponse.class))})
    })
    @GetMapping("/finallist") // 최종평가 차트 api
    public ResponseEntity finalEvalList(@RequestParam("project_id") Long projectId,
                                        @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        EverythingEvalResponse response = evaluationService.findFinalEvaluationList(projectId);

        return new ResponseEntity(response,HttpStatus.OK);
    }

    @Operation(method = "get", summary = "프로젝트 평가 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 평가 멤버 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = ParticipantResponse.class))})
    })// * 프로젝트 참여자 리스트
    @GetMapping("/participant")
    public ResponseEntity findParticipantList(@RequestParam("project_id") Long projectId,
                                        @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        List<ParticipantResponse> response = evaluationService.findParticipantList(projectId);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    @Operation(method = "get", summary = "개인페이지 프로젝트 평가 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 프로젝트 평가 리스트 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = MyEvalListResponse.class))})
    })// * 개인 페이지
    @GetMapping("/mine")
    public ResponseEntity findMyEvalList(CustomPageRequest pageRequest, @CurrentLoginMember Member member) {
        PageRequest of = pageRequest.of("project");
        Pageable pageable = (Pageable) of;
        Page<MyEvalListResponse> response = evaluationService.findMyEvalList(pageable, member.getId());
        return new ResponseEntity(response,HttpStatus.OK);
    }
    @Operation(method = "get", summary = "개인페이지 프로젝트 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 프로젝트 평가 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = MyEvalDetailResponse.class))})
    })
    @GetMapping("/mine/{projectId}")
    public ResponseEntity findMyEvalDetail(@PathVariable Long projectId, @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }
        MyEvalDetailResponse response = evaluationService.findMyEval(projectId, member.getId());

        return new ResponseEntity(response,HttpStatus.OK);
    }
    @Operation(method = "get", summary = "개인페이지 평가 차트 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "개인페이지 평가 차트 조회 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,  schema = @Schema(implementation = MyEvalChartResponse.class))})
    })
    @GetMapping("/mine/chart")
    public ResponseEntity findMyEvalChart(@CurrentLoginMember Member member) {
        MyEvalChartResponse response = evaluationService.findMyEvalChart(member.getId());
        return new ResponseEntity(response,HttpStatus.OK);
    }
}
