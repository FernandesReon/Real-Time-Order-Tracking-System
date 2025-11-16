package com.reon.order_backend.email;

public interface EmailService {
    /*
    send order status notification to user
     */
    void sendOrderSuccessEmail(String to, String subject, String body);
    void sendOrderStatusEmail(String to, String subject, String body);
}
