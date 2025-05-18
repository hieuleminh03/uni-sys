package controller;

import dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import util.Paging;

/**
 * Controller cơ sở cung cấp các phương thức phản hồi chuẩn hóa
 * Tất cả các controller nên kế thừa lớp này để đảm bảo phản hồi API nhất quán
 */
public abstract class BaseController {

    /**
     * Tạo phản hồi thành công với mã trạng thái 200 trong nội dung phản hồi
     * @param data Dữ liệu được đưa vào phản hồi
     * @param message Thông báo thành công
     * @return ResponseEntity với BaseResponse và HTTP 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> successResponse(T data, String message) {
        return ResponseEntity.ok(BaseResponse.ok(data, message));
    }

    /**
     * Tạo phản hồi thành công với mã trạng thái 200 trong nội dung phản hồi và thông tin phân trang
     * @param data Dữ liệu được đưa vào phản hồi
     * @param message Thông báo thành công
     * @param paging Thông tin phân trang
     * @return ResponseEntity với BaseResponse và HTTP 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> successResponse(T data, String message, Paging paging) {
        return ResponseEntity.ok(BaseResponse.ok(data, message, paging));
    }

    /**
     * Tạo phản hồi với mã trạng thái 201 trong nội dung phản hồi
     * @param data Dữ liệu được đưa vào phản hồi
     * @param message Thông báo thành công
     * @return ResponseEntity với BaseResponse và HTTP 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> createdResponse(T data, String message) {
        return ResponseEntity.ok(BaseResponse.created(data, message));
    }

    /**
     * Tạo phản hồi với mã trạng thái 202 trong nội dung phản hồi
     * @param data Dữ liệu được đưa vào phản hồi
     * @param message Thông báo thành công
     * @return ResponseEntity với BaseResponse và HTTP 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> acceptedResponse(T data, String message) {
        return ResponseEntity.ok(BaseResponse.accepted(data, message));
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái 400 trong nội dung phản hồi
     * @param message Thông báo lỗi
     * @param e Ngoại lệ đã xảy ra
     * @return ResponseEntity với BaseResponse và HTTP 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> badRequestResponse(String message, Exception e) {
        return ResponseEntity.ok(BaseResponse.badRequest(message, e, null));
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái 401 trong nội dung phản hồi
     * @param message Thông báo lỗi
     * @return ResponseEntity với BaseResponse và HTTP 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> unauthorizedResponse(String message) {
        return ResponseEntity.ok(BaseResponse.unauthorized(message));
    }

    /**
     * Tạo phản hồi lỗi với mã trạng thái tùy chỉnh trong nội dung phản hồi
     * @param status Mã trạng thái HTTP để đưa vào nội dung phản hồi
     * @param message Thông báo lỗi
     * @param e Ngoại lệ đã xảy ra
     * @return ResponseEntity với BaseResponse và HTTP 200
     */
    protected <T> ResponseEntity<BaseResponse<T>> errorResponse(HttpStatus status, String message, Exception e) {
        return ResponseEntity.ok(BaseResponse.error(status.value(), message, e, null));
    }
}
