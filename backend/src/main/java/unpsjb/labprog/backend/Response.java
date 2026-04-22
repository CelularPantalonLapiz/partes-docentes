package unpsjb.labprog.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    private int status;
    private String message;
    private Object data;

    public Response(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public static ResponseEntity<Object> response(HttpStatus status, String message, Object responseObj) {
        Response res = new Response(status.value(), message, responseObj);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public static ResponseEntity<Object> ok(Object responseObj) {
        return response(HttpStatus.OK, "OK", responseObj);
    }

    public static ResponseEntity<Object> ok(Object responseObj, String msj) {
        return response(HttpStatus.OK, msj, responseObj);
    }

    public static ResponseEntity<Object> notFound(String msj) {
        return response(HttpStatus.NOT_FOUND, msj, null);
    }
}