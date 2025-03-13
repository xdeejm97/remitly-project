package com.task.remitlyproject.dto.request;


public record SwiftCodeRequest(
        String address,
        String bankName,
        String countryISO2,
        String countryName,
        boolean isHeadquarter,
        String swiftCode
) {
}
