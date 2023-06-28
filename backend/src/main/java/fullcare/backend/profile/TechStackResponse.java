package fullcare.backend.profile;

import fullcare.backend.profile.TechStack;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TechStackResponse {
    List<String> stackList = new ArrayList<>();
    public void addTechStack(String techStack){
        this.stackList.add(techStack);
    }
}
