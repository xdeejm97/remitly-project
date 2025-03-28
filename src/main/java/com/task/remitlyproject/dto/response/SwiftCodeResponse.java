package com.task.remitlyproject.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response with a Swift code headquarter.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCodeResponse {

  private String address;
  private String bankName;
  private String countryISO2;
  private String countryName;
  @JsonProperty("isHeadquarter")
  private boolean isHeadquarter;
  private String swiftCode;
  private List<SwiftCodeResponseBranches> branches;

}
