package com.tuikhon.constant;

/**
 * Constant messages used across the application.
 * All messages are hard-coded in Vietnamese per project specification.
 */
public final class MessageConstant {

    private MessageConstant() {
        // Utility class
    }

    // Success messages
    public static final String OPERATION_SUCCESSFUL = "Thao tác thành công";
    public static final String LOGIN_SUCCESSFUL = "Đăng nhập thành công";
    public static final String LOGOUT_SUCCESSFUL = "Đăng xuất thành công";
    public static final String REFRESH_TOKEN_SUCCESSFUL = "Làm mới token thành công";
    public static final String REGISTER_SUCCESSFUL = "Đăng ký thành công";
    public static final String UPDATE_PROFILE_SUCCESSFUL = "Cập nhật thông tin thành công";
    public static final String CHANGE_PASSWORD_SUCCESSFUL = "Đổi mật khẩu thành công";

    // Authentication & Authorization error messages
    public static final String UNAUTHORIZED_ACCESS = "Truy cập không được phép";
    public static final String ACCESS_DENIED = "Bị từ chối truy cập";
    public static final String INVALID_CREDENTIALS = "Email hoặc mật khẩu không chính xác";
    public static final String TOKEN_EXPIRED = "Token đã hết hạn";
    public static final String TOKEN_INVALID = "Token không hợp lệ";
    public static final String TOKEN_REVOKED = "Token đã bị thu hồi";

    // User error messages
    public static final String USER_NOT_FOUND = "Không tìm thấy người dùng";
    public static final String USER_ALREADY_EXISTS = "Email đã tồn tại trong hệ thống";
    public static final String OLD_PASSWORD_INCORRECT = "Mật khẩu cũ không chính xác";
    public static final String PASSWORD_CONFIRMATION_MISMATCH = "Mật khẩu xác nhận không khớp";

    // Common error messages
    public static final String INTERNAL_SERVER_ERROR = "Lỗi hệ thống nội bộ";
    public static final String RESOURCE_NOT_FOUND = "Không tìm thấy tài nguyên";
    public static final String BAD_REQUEST = "Yêu cầu không hợp lệ";
    public static final String VALIDATION_FAILED = "Dữ liệu không hợp lệ";
    public static final String FIELD_REQUIRED = "Trường %s là bắt buộc";
    public static final String FIELD_INVALID = "Trường %s không hợp lệ";
}
