package fullcare.backend;


import fullcare.backend.memo.repository.MemoRepository;
import fullcare.backend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final ProjectRepository projectRepository;
    private final MemoRepository memoRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {


    }
}
