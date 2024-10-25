package com.wowo.wowo.providers.notify;

public interface NotifyPublisher {

    /**
     * Đẩy thông báo lên kênh
     * @param message chuỗi dữ liệu thông báo
     */
    void publish(String message);
}
