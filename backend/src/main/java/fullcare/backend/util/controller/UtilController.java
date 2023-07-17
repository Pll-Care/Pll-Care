package fullcare.backend.util.controller;

import fullcare.backend.util.dto.TechStackResponse;
import fullcare.backend.util.TechStackUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "유틸", description = "유틸 관련 API")
@RequestMapping("/api/auth/util")
@RestController
public class UtilController {
    @Operation(method = "get", summary = "기술스택 검색")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "기술스택 검색 성공", useReturnTypeSchema = true),
//            @ApiResponse(responseCode = "400", description = "기술스택 검색 실패", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FailureResponse.class)))
    })
    @GetMapping(value = "/techstack")
    public ResponseEntity<TechStackResponse> findTechStack(@RequestParam("tech") String tech) {
        TechStackResponse response = TechStackUtil.findTechStack(tech);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
