package fullcare.backend.project.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest
//@Transactional
//class ProjectRepositoryTest {
//    @Autowired
//    private ProjectRepository projectRepository;
//
//    @Test
//    public void ListQuery(){
////        List<Project> byCreateDt = projectRepository.findByCreateDt(1l);
////        for (Project project : byCreateDt) {
////            System.out.println("project = " + project);
////        }
////        CustomPageRequest customPageRequest = new CustomPageRequest();
////        PageRequest of = customPageRequest.of();
////        Pageable pa = (Pageable)of;
////        Page<Project> pageProjects = projectRepository.findByCreateDt(pa, 1l);
////        for (Project pageProject : pageProjects) {
////            System.out.println("pageProject = " + pageProject);
////        }
//
//    }
//}