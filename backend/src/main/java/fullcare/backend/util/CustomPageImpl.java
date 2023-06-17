package fullcare.backend.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(value = {"pageable", "number"})
public class CustomPageImpl<T> extends PageImpl<T> {
    int pageNumber;

    public CustomPageImpl(List content, Pageable pageable, long total) {
        super(content,pageable.getSort().isSorted() ? pageable : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending()), total);

    }
    public CustomPageImpl(List content) {
        super(content);
    }
    public int getPageNumber() {
        return super.getNumber()+1;
    }

}