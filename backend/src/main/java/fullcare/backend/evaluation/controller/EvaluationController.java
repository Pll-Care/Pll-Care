package fullcare.backend.evaluation.controller;


import fullcare.backend.evaluation.dto.*;
import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.FinalEvalUpdateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.dto.response.*;
import fullcare.backend.evaluation.service.EvaluationService;
import fullcare.backend.global.errorcode.EvaluationErrorCode;
import fullcare.backend.global.exceptionhandling.exception.UnauthorizedAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.project.service.ProjectService;
import fullcare.backend.projectmember.domain.ProjectMember;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.schedulemember.service.ScheduleMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth/evaluation")
@RestController
@Tag(name = "평가", description = "평가 관련 API")
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final ProjectMemberService projectMemberService;
    private final ScheduleMemberService scheduleMemberService;
    private final ProjectService projectService;

    // * 중간평가 관련
    @Operation(method = "get", summary = "중간 평가 모달창 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 모달창 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/midterm")
    public ResponseEntity<MidTermEvalModalResponse> midtermEvalModal(@RequestParam Long scheduleId, @RequestParam Long projectId,
                                                                     @CurrentLoginMember Member member) {
        if (!(scheduleMemberService.validateScheduleMember(scheduleId, projectId, member.getId()))) {
            throw new UnauthorizedAccessException(EvaluationErrorCode.UNAUTHORIZED_ACCESS);
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
        evaluationService.createMidtermEvaluation(midTermEvalCreateRequest, member.getId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(method = "get", summary = "중간 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 모달창 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/midterm/detail")
    public ResponseEntity<List<BadgeDto>> findMidterm(@RequestParam("project_id") Long projectId,
                                                      @CurrentLoginMember Member member) {
        List<BadgeDto> response = evaluationService.findMidtermEvaluationDetailResponse(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "중간 평가 차트, 랭킹 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 차트, 랭킹 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/midtermlist") // 디자인에서 뱃지 개수 차트 부분
    public ResponseEntity<EverythingEvalResponse<FinalCharDto<BadgeDto>, MidTermRankingDto>> midtermEvalList(@RequestParam("project_id") Long projectId,
                                                                                                             @CurrentLoginMember Member member) {
        EverythingEvalResponse response = evaluationService.findMidtermEvaluationList(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }


    // * 최종평가 관련
    @Operation(method = "post", summary = "최종 평가 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 생성 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @PostMapping("/final")
    public ResponseEntity<FinalEvaluationCreateResponse> finalEvalCreate(@RequestBody FinalEvalCreateRequest finalEvalCreateRequest,
                                                                         @CurrentLoginMember Member member) {
        Long finalEvalId = evaluationService.createFinalEvaluation(finalEvalCreateRequest, member.getId());
        return new ResponseEntity(new FinalEvaluationCreateResponse(finalEvalId), HttpStatus.OK);
    }

//    @Operation(method = "put", summary = "최종 평가 수정")
//    @ApiResponses(value = {
//            @ApiResponse(description = "최종 평가 수정 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
//    })
//    @PutMapping("/final/{evaluationId}")
//    public ResponseEntity finalEvalUpdate(@RequestBody FinalEvalUpdateRequest finalEvalUpdateRequest, @PathVariable Long evaluationId,
//                                          @CurrentLoginMember Member member) {
//        // ? 작성자가 맞는지 검증
//        if (!evaluationService.validateAuthor(evaluationId, member.getId())) {
//            throw new UnauthorizedAccessException(EvaluationErrorCode.UNAUTHORIZED_ACCESS);
//        }
//        evaluationService.updateFinalEvaluation(evaluationId, finalEvalUpdateRequest);
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    @Operation(method = "delete", summary = "최종 평가 삭제")
//    @ApiResponses(value = {
//            @ApiResponse(description = "최종 평가 삭제 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
//    })
//    @DeleteMapping("/final/{evaluationId}")
//    public ResponseEntity finalEvalDelete(@PathVariable Long evaluationId, @Valid @RequestParam("project_id") Long projectId,
//                                          @CurrentLoginMember Member member) {
//        // ? 작성자가 맞는지 검증
//        if (!evaluationService.validateAuthor(evaluationId, member.getId())) {
//            throw new UnauthorizedAccessException(EvaluationErrorCode.UNAUTHORIZED_ACCESS);
//        }
//        evaluationService.deleteFinalEvaluation(evaluationId, projectId);
//
//        return new ResponseEntity(HttpStatus.OK);
//    }

    @Operation(method = "get", summary = "최종 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/final/{evaluationId}")
    public ResponseEntity<FinalEvaluationResponse> finalEvalDetails(@PathVariable Long evaluationId, @RequestParam("project_id") Long projectId,
                                                                    @CurrentLoginMember Member member) {// ? 평가된 사람 id, 평가 id 조건 검색 해서 일치하면 접근 허용 필요
        FinalEvaluationResponse response = evaluationService.findFinalEvaluationDetailResponse(projectId, member.getId(), evaluationId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "최종 평가 차트, 랭킹 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 차트, 랭킹 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/finallist") // 최종평가 차트 api
    public ResponseEntity<EverythingEvalResponse<FinalCharDto<ScoreDto>, FinalTermRankingDto>> finalEvalList(@RequestParam("project_id") Long projectId,
                                                                                                             @CurrentLoginMember Member member) {
        EverythingEvalResponse response = evaluationService.findFinalEvaluationList(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "프로젝트 평가 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 평가 멤버 조회 성공", responseCode = "200", useReturnTypeSchema = true)
    })// * 프로젝트 참여자 리스트
    @GetMapping("/participant")
    public ResponseEntity<List<ParticipantResponse>> findParticipantList(@RequestParam("project_id") Long projectId,
                                                                         @CurrentLoginMember Member member) {
        List<ParticipantResponse> response = evaluationService.findParticipantList(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
