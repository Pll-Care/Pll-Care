package fullcare.backend.evaluation.controller;


import fullcare.backend.evaluation.dto.*;
import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.dto.response.*;
import fullcare.backend.evaluation.service.EvaluationService;
import fullcare.backend.global.dto.ErrorResponse;
import fullcare.backend.global.errorcode.EvaluationErrorCode;
import fullcare.backend.global.exceptionhandling.exception.UnauthorizedAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.schedulemember.service.ScheduleMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private final ScheduleMemberService scheduleMemberService;

    // * 중간평가 관련 API
    // * READ API
    @Operation(method = "get", summary = "중간 평가 모달창 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 모달창 조회 성공", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MidTermEvalModalResponse.class))),
            @ApiResponse(description = "중간 평가 모달창 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/midterm")
    public ResponseEntity<MidTermEvalModalResponse> midtermEvalModal(@RequestParam Long scheduleId, @RequestParam Long projectId,
                                                                     @CurrentLoginMember Member member) {
        if (!(scheduleMemberService.validateScheduleMember(scheduleId, projectId, member.getId()))) {
            throw new UnauthorizedAccessException(EvaluationErrorCode.UNAUTHORIZED_ACCESS);
        }
        MidTermEvalModalResponse response = evaluationService.setMidEvalModal(scheduleId, member.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "중간 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "중간 평가 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/midterm/detail")
    public ResponseEntity<List<BadgeDto>> findMidterm(@RequestParam("project_id") Long projectId,
                                                      @CurrentLoginMember Member member) {
        List<BadgeDto> response = evaluationService.findMidtermEvaluationDetailResponse(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "중간 평가 차트, 랭킹 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 차트, 랭킹 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "중간 평가 차트, 랭킹 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/midtermlist")
    public ResponseEntity<EverythingEvalResponse<FinalCharDto<BadgeDto>, MidTermRankingDto>> midtermEvalList(@RequestParam("project_id") Long projectId,
                                                                                                             @CurrentLoginMember Member member) {
        EverythingEvalResponse response = evaluationService.findMidtermEvaluationList(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // * CREATE API
    @Operation(method = "post", summary = "중간 평가 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "중간 평가 생성 성공", responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE)}),
            @ApiResponse(description = "중간 평가 생성 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/midterm")
    public ResponseEntity midtermEvalCreate(@RequestBody @Valid MidTermEvalCreateRequest midTermEvalCreateRequest,
                                            @CurrentLoginMember Member member) {
        evaluationService.createMidtermEvaluation(midTermEvalCreateRequest, member.getId());
        return new ResponseEntity(HttpStatus.OK);
    }



    // * 최종평가 관련
    // * READ API
    @Operation(method = "get", summary = "최종 평가 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "최종 평가 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/final/{evaluationId}")
    public ResponseEntity<FinalEvaluationResponse> finalEvalDetails(@PathVariable Long evaluationId, @RequestParam("project_id") Long projectId,
                                                                    @CurrentLoginMember Member member) {
        FinalEvaluationResponse response = evaluationService.findFinalEvaluationDetailResponse(projectId, member.getId(), evaluationId);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(method = "get", summary = "최종 평가 차트, 랭킹 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 차트, 랭킹 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "최종 평가 차트, 랭킹 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/finallist") // 최종평가 차트 api
    public ResponseEntity<EverythingEvalResponse<FinalCharDto<ScoreDto>, FinalTermRankingDto>> finalEvalList(@RequestParam("project_id") Long projectId,
                                                                                                             @CurrentLoginMember Member member) {
        EverythingEvalResponse response = evaluationService.findFinalEvaluationList(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

    // * CREATE API
    @Operation(method = "post", summary = "최종 평가 생성")
    @ApiResponses(value = {
            @ApiResponse(description = "최종 평가 생성 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "최종 평가 생성 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/final")
    public ResponseEntity<FinalEvaluationCreateResponse> finalEvalCreate(@RequestBody FinalEvalCreateRequest finalEvalCreateRequest,
                                                                         @CurrentLoginMember Member member) {
        Long finalEvalId = evaluationService.createFinalEvaluation(finalEvalCreateRequest, member.getId());
        return new ResponseEntity(new FinalEvaluationCreateResponse(finalEvalId), HttpStatus.OK);
    }


    // * 프로젝트 참여자 리스트
    @Operation(method = "get", summary = "프로젝트 평가 멤버 조회")
    @ApiResponses(value = {
            @ApiResponse(description = "프로젝트 평가 멤버 조회 성공", responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(description = "프로젝트 평가 멤버 조회 실패", responseCode = "400",  content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/participant")
    public ResponseEntity<List<ParticipantResponse>> findParticipantList(@RequestParam("project_id") Long projectId,
                                                                         @CurrentLoginMember Member member) {
        List<ParticipantResponse> response = evaluationService.findParticipantList(projectId, member.getId());
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
