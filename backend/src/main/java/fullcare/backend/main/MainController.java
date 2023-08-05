package fullcare.backend.main;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "메인 페이지", description = "메인 페이지 관련 API")
@RequestMapping("/api/main")
@RestController
public class MainController {

    
}
