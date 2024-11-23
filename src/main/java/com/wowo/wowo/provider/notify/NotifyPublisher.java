/*
 * ******************************************************
 *  * Copyright (c) 2024 htilssu
 *  *
 *  * This code is the property of htilssu. All rights reserved.
 *  * Redistribution or reproduction of any part of this code
 *  * in any form, with or without modification, is strictly
 *  * prohibited without prior written permission from the author.
 *  *
 *  * Author: htilssu
 *  * Created: 23-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.provider.notify;

public interface NotifyPublisher {

    /**
     * Đẩy thông báo lên kênh
     * @param message chuỗi dữ liệu thông báo
     */
    void publish(String message);
}
