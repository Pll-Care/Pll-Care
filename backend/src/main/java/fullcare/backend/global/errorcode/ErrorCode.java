package fullcare.backend.global.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();

    HttpStatus getStatus();

    String getMessage();
}


// * error code 순서

// * 1. Not Found
// * 2. Bad Request
// * 3. Forbidden


// * ACCESS -> CREATE -> MODIFY -> DELETE


// * error code 순서

// * 1. Not Found
// * 2. Bad Request
// * 3. Forbidden


// * ACCESS -> CREATE -> MODIFY -> DELETE


// * error code 순서

// * 1. Not Found
// * 2. Bad Request
// * 3. Forbidden


// * ACCESS -> CREATE -> MODIFY -> DELETE