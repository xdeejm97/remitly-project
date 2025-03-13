package com.task.remitlyproject.dto.response;

import lombok.Builder;

@Builder
public record SwiftCodeResponse(
        String message
) {
}
