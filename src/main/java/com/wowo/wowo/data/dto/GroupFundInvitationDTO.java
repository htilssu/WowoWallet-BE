package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wowo.wowo.model.InvitationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupFundInvitationDTO {
    private Long id;
    private String nameGroup;
    private String descriptionGroup;
    private String senderName;
    private String senderEmail;
    private InvitationStatus status;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
