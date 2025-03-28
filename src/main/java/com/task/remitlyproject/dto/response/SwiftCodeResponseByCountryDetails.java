package com.task.remitlyproject.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response each Swift code by country.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCodeResponseByCountryDetails {

  private String address;
  private String bankName;
  private String countryISO2;
  @JsonProperty("isHeadquarter")
  private boolean isHeadquarter;
  private String swiftCode;

}
