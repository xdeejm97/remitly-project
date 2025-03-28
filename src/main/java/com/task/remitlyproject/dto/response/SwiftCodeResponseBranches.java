package com.task.remitlyproject.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Response with a Swift code branch.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCodeResponseBranches {

  private String address;
  private String bankName;
  private String countryISO2;
  private String countryName;
  @JsonProperty("isHeadquarter")
  private boolean isHeadquarter;
  private String swiftCode;

}
