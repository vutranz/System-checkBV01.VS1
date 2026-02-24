package com.checkxmlbv01.websitecheckxmlbv01.ultil.error;

public enum ErrorCodes {

   ID_INVALID("E001", "Id không hợp lệ"),
    STORAGE_ERROR("E002", "Lỗi lưu trữ"),

    USER_NOT_FOUND("U001", "Người dùng không tồn tại"),
    USERNAME_DUPLICATE("U002", "Tên đăng nhập đã tồn tại"),

    USER_ALREADY_ASSIGNED("UA001", "Người dùng đã được gán vào cơ sở này"),
    COSO_NOT_FOUND("C001", "Cơ sở không tồn tại"),
    ASSIGNMENT_NOT_FOUND("UA002", "Quan hệ người dùng - cơ sở không tồn tại"),

    NHOM_DVKT_NOT_FOUND("N001", "Nhóm dịch vụ kỹ thuật không tồn tại"),
    NHOM_DVKT_DUPLICATE("N002", "Tên nhóm dịch vụ kỹ thuật đã tồn tại trong cơ sở"),
    NHOM_DVKT_INVALID("N003", "Thông tin nhóm dịch vụ kỹ thuật không hợp lệ"),

     BAC_SI_DUPLICATE("N003", "Dữ liệu trùng"),

CA_LAM_VIEC_OVERLAP("N005", "Ca làm việc bị trùng thời gian"),
TIME_INVALID("N006", "Thời gian bắt đầu và kết thúc không hợp lệ");
    private final String code;
    private final String message;

    ErrorCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}


