package fullcare.backend.project.repository;

import fullcare.backend.project.domain.Project;
import fullcare.backend.project.dto.ProjectListResponse;
import fullcare.backend.util.CustomPageRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void ListQuery(){
//        List<Project> byCreateDt = projectRepository.findByCreateDt(1l);
//        for (Project project : byCreateDt) {
//            System.out.println("project = " + project);
//        }
//        CustomPageRequest customPageRequest = new CustomPageRequest();
//        PageRequest of = customPageRequest.of();
//        Pageable pa = (Pageable)of;
//        Page<Project> pageProjects = projectRepository.findByCreateDt(pa, 1l);
//        for (Project pageProject : pageProjects) {
//            System.out.println("pageProject = " + pageProject);
//        }

    }
}