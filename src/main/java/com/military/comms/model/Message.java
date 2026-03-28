package com.military.comms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Represents a secure message sent between military personnel.
 */
@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fromUsername;

    @Column(nullable = false)
    private String toUsername;

    @Column(nullable = false, length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    private ClassificationLevel classification;  // UNCLASSIFIED, CONFIDENTIAL, SECRET, TOP_SECRET

    @Column(nullable = false)
    private LocalDateTime sentAt;

    public enum ClassificationLevel {
        UNCLASSIFIED, CONFIDENTIAL, SECRET, TOP_SECRET
    }
}
