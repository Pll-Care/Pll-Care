package fullcare.backend.util.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TechStackResponse {
    List<TechStackDto> stackList = new ArrayList<>();
    public void addTechStack(TechStackDto techStack){
        this.stackList.add(techStack);
    }
}
