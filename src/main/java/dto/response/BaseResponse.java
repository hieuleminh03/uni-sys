package dto.response;

import lombok.Builder;
import lombok.Getter;
import util.Paging;

@Getter
@Builder
public class BaseResponse<T> {

    private int code;
    private String message;
    private T data;
    private Paging paging;
    private String error;

    public static <T> BaseResponse<T> success(T data, int code, String message) {
        return BaseResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success(T data, int code, String message, Paging paging) {
        return BaseResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .paging(paging)
                .build();
    }

    public static <T> BaseResponse<T> error(int code, String message, Exception e, String error) {
        return BaseResponse.<T>builder()
                .code(code)
                .message(message)
                .error(null != e ? e.getMessage() : error)
                .build();
    }

    public static <T> BaseResponse<T> ok(T data, String message) {
        return success(data, 200, null != message ? message : "OK");
    }

    public static <T> BaseResponse<T> ok(T data, String message, Paging paging) {
        return success(data, 200, null != message ? message : "OK", paging);
    }

    public static <T> BaseResponse<T> created(T data, String message) {
        return success(data, 201, null != message ? message : "Created");
    }

    public static <T> BaseResponse<T> accepted(T data, String message) {
        return success(data, 202, null != message ? message : "Accepted");
    }

    public static <T> BaseResponse<T> badRequest(String message, Exception e, String error) {
        return error(400, message, e, error);
    }

    public static <T> BaseResponse<T> unauthorized(String message) {
        return error(401, message, null, "Unauthorized");
    }

}
