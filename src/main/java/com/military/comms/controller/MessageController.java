package com.military.comms.controller;

import com.military.comms.dto.MessageRequest;
import com.military.comms.model.Message;
import com.military.comms.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PART 3 - MessageController
 * Secure messaging between military personnel with classification levels.
 * TOP_SECRET messages restricted to OFFICER and ADMIN roles.
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    /** Send a secure message */
    @PostMapping
    public ResponseEntity<Message> sendMessage(
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserDetails sender) {

        Message message = Message.builder()
                .fromUsername(sender.getUsername())
                .toUsername(request.getToUsername())
                .content(request.getContent())
                .classification(request.getClassification())
                .sentAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(messageRepository.save(message));
    }

    /** Receive your inbox messages */
    @GetMapping("/inbox")
    public ResponseEntity<List<Message>> getInbox(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(messageRepository.findByToUsername(user.getUsername()));
    }

    /** TOP SECRET messages - Officers and Admins only */
    @GetMapping("/top-secret")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    public ResponseEntity<List<Message>> getTopSecretMessages() {
        return ResponseEntity.ok(
                messageRepository.findAll().stream()
                        .filter(m -> m.getClassification() == Message.ClassificationLevel.TOP_SECRET)
                        .toList()
        );
    }
}
