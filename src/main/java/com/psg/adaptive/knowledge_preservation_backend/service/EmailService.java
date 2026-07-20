package com.psg.adaptive.knowledge_preservation_backend.service;

import org.springframework.stereotype.Component;

@Component
public interface EmailService {
    void sendResetPasswordEmail(String email, String token);
}
