package com.task.remitlyproject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response with a Swift code by country.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCodeResponseByCountry {

  private String countryISO2;
  private String countryName;
  private List<SwiftCodeResponseByCountryDetails> swiftCodes;

}
