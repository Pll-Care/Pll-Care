package fullcare.backend.evaluation.controller;


import fullcare.backend.evaluation.dto.request.FinalEvalCreateRequest;
import fullcare.backend.evaluation.dto.request.MidTermEvalCreateRequest;
import fullcare.backend.evaluation.service.EvaluationService;
import fullcare.backend.global.exception.InvalidAccessException;
import fullcare.backend.member.domain.Member;
import fullcare.backend.projectmember.service.ProjectMemberService;
import fullcare.backend.security.jwt.CurrentLoginMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth/evaluation")
@RestController
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final ProjectMemberService projectMemberService;


    // ! 평가를 조회하는 API는 로그인된 사용자 누구나 접근 가능하게 해야하는가?


    // todo 구현해야하는 API 기능
    // * 중간평가 관련
    // * 1. 중간 평가 추가 (수정 및 삭제 없음)
    // * 2. 중간 평가 조회 (모든 팀원)
    // ? 3. 중간 평가 조회 (개인) -> 필요한가?

    @PostMapping("/midterm")
    public ResponseEntity midtermEvalCreate(@RequestBody MidTermEvalCreateRequest midTermEvalCreateRequest,
                                            @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(midTermEvalCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        evaluationService.createMidtermEvaluation(midTermEvalCreateRequest, member);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/midtermlist")
    public ResponseEntity midtermEvalList(@RequestParam("project_id") Long projectId,
                                          @CurrentLoginMember Member member) {
        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        evaluationService.findMidtermEvaluationList(projectId);

        return new ResponseEntity(HttpStatus.OK);
    }


    // * 최종평가 관련
    // * 1. 최종 평가 추가, 수정, 삭제
    // * 2. 최종 평가 목록 조회
    // * 3. 최종 평가 단건 조회


    @PostMapping("/final")
    public ResponseEntity finalEvalCreate(@RequestBody FinalEvalCreateRequest finalEvalCreateRequest,
                                          @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(finalEvalCreateRequest.getProjectId(), member.getId()))) {
            throw new InvalidAccessException("해당 프로젝트에 접근 권한이 없습니다.");
        }

        evaluationService.createFinalEvaluation(finalEvalCreateRequest, member);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/final/{evaluationId}")
    public ResponseEntity finalEvalDetails(@PathVariable Long evaluationId,
                                           @CurrentLoginMember Member member) {


        evaluationService.findFinalEvaluationDetailResponse(evaluationId);

        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/finallist")
    public ResponseEntity finalEvalList(@RequestParam("project_id") Long projectId,
                                        @CurrentLoginMember Member member) {

        if (!(projectMemberService.validateProjectMember(projectId, member.getId()))) {
            throw new InvalidAccessException("프로젝트에 대한 권한이 없습니다.");
        }

        evaluationService.findFinalEvaluationList(projectId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
