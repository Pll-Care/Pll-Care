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
/*    private Map<String, Object> paging = new HashMap<>();*/
    int pageNumber;
    @JsonCreator
    public CustomPageImpl(List content, Pageable pageable, long total) {
        super(content,pageable.getSort().isSorted() ? pageable : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending()), total);

    }

    @JsonCreator
    public CustomPageImpl(List content) {
        super(content);
    }

    //@JsonGetter(value = "contents")
/*
    @Override
    public List getContent() {
        return super.getContent();
    }
*/


    public int getPageNumber() {
        return super.getNumber()+1;
    }
/*
    @JsonGetter(value = "paging")

    public Map getPaging() {
        paging.put("totalPages", super.getTotalPages());
        paging.put("totalElements", super.getTotalElements());
        paging.put("pageSize", super.getSize());
        paging.put("pageNumber", super.getNumber() + 1);
        paging.put("isFirst", super.isFirst());
        paging.put("isLast", super.isLast());
        paging.put("sort", super.getSort());
        paging.put("isEmpty", super.isEmpty());
        return paging;
    }
*/



}