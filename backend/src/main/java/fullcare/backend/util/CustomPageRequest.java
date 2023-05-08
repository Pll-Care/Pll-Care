package fullcare.backend.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class CustomPageRequest {
    private int page = 1;
    private int size = 10;
    private Direction direction = Direction.DESC;

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        int DEFAULT_SIZE = 10;
        int MAX_SIZE = 50;
        this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public PageRequest of() {
        return PageRequest.of(page - 1, size, direction, "createDate");
    }
}
