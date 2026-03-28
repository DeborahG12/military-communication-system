package com.military.comms.dto;

import com.military.comms.model.Message.ClassificationLevel;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class MessageRequest {
    @NotBlank private String toUsername;
    @NotBlank private String content;
    private ClassificationLevel classification = ClassificationLevel.UNCLASSIFIED;
}
