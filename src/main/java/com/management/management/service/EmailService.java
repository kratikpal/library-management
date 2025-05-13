package com.management.management.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
