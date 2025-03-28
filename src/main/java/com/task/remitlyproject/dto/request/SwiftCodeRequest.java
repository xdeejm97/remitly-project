package com.task.remitlyproject.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Swift code request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SwiftCodeRequest {
  private String address;

  @Schema(description = "Bank name", example = "ING")
  @NotBlank(message = "Bank name must not be blank")
  private String bankName;

  @Schema(description = "Country ISO2", example = "XX")
  @NotBlank(message = "Country ISO2 must not be blank")
  private String countryISO2;

  @NotBlank(message = "Country name must not be blank")
  private String countryName;

  @Schema(description = "Swift code", example = "AKBKMTMTXXX")
  @Size(min = 8, max = 11, message = "Swift code must contain from 8 to 11 characters")
  @NotBlank(message = "Swift code must not be blank")
  private String swiftCode;

}
