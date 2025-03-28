package com.task.remitlyproject.service;

import com.task.remitlyproject.constant.SwiftCodeConstants;
import com.task.remitlyproject.dto.request.SwiftCodeRequest;
import com.task.remitlyproject.dto.response.SwiftCodeResponse;
import com.task.remitlyproject.dto.response.SwiftCodeResponseBranches;
import com.task.remitlyproject.dto.response.SwiftCodeResponseByCountry;
import com.task.remitlyproject.dto.response.SwiftCodeResponseByCountryDetails;
import com.task.remitlyproject.entity.SwiftCode;
import com.task.remitlyproject.exception.SwiftCodeAlreadyExistsException;
import com.task.remitlyproject.repository.SwiftCodeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service responsible for Swift code entity.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class SwiftCodeService {

  private final SwiftCodeRepository swiftCodeRepository;

  /**
   * Initializes the service by importing Swift codes upon bean creation.
   * This method is automatically called after dependency injection.
   */
  @PostConstruct
  public void init() {
    importSwiftCodes();
  }

  /**
   * Retrieves Swift code details for a given Swift code, handling both headquarters and branch codes.
   *
   * @param swiftCode The Swift code to look up
   * @return SwiftCodeResponse containing detailed information about the Swift code
   * @throws EntityNotFoundException if the Swift code is not found
   */
  public SwiftCodeResponse getSwiftCodeForHeadquarterOrBranch(String swiftCode) {
    SwiftCode swiftCodeEntity = swiftCodeRepository.findBySwiftCode(swiftCode.toUpperCase(Locale.ROOT))
            .orElseThrow(() -> new EntityNotFoundException(
                    String.format("SWIFT Code not found: %s", swiftCode.toUpperCase(Locale.ROOT))));

    return swiftCode.endsWith("XXX") ? buildSwiftCodeResponseHeadquarter(swiftCodeEntity)
            : buildSwiftCodeResponseBranch(swiftCodeEntity);
  }

  /**
   * Retrieves all Swift codes for a specific country.
   *
   * @param countryISO2 The ISO 2-letter country code
   * @return SwiftCodeResponseByCountry containing Swift codes for the specified country
   * @throws EntityNotFoundException if no Swift codes are found for the country
   */
  public SwiftCodeResponseByCountry getAllSwiftCodesByCountry(String countryISO2) {
    List<SwiftCode> byCountryISO2 = swiftCodeRepository.findByCountryISO2(countryISO2.toUpperCase(Locale.ROOT));
    List<SwiftCodeResponseByCountryDetails> swiftCodeResponseByCountryDetails = getSwiftCodeResponseDetails(byCountryISO2);

    return SwiftCodeResponseByCountry
            .builder()
            .countryISO2(countryISO2.toUpperCase(Locale.ROOT))
            .countryName(byCountryISO2.get(0).getCountryName())
            .swiftCodes(swiftCodeResponseByCountryDetails)
            .build();
  }

  /**
   * Saves a new Swift code entry to the database.
   *
   * @param swiftCodeRequest The request containing Swift code details
   * @throws SwiftCodeAlreadyExistsException if a Swift code with the same value already exists
   */
  @Transactional
  public void saveSwiftCodeEntry(SwiftCodeRequest swiftCodeRequest) {
    if (swiftCodeRepository.existsBySwiftCode(swiftCodeRequest.getSwiftCode().toUpperCase(Locale.ROOT))) {
      throw new SwiftCodeAlreadyExistsException(swiftCodeRequest.getSwiftCode().toUpperCase(Locale.ROOT));
    }
    boolean isHeadquarter = swiftCodeRequest.getSwiftCode().toUpperCase(Locale.ROOT).endsWith("XXX");

    SwiftCode swiftCode = SwiftCode.builder()
            .address(swiftCodeRequest.getAddress().toUpperCase(Locale.ROOT))
            .bankName(swiftCodeRequest.getBankName().toUpperCase(Locale.ROOT))
            .countryISO2(swiftCodeRequest.getCountryISO2().toUpperCase(Locale.ROOT))
            .countryName(swiftCodeRequest.getCountryName().toUpperCase(Locale.ROOT))
            .isHeadquarter(isHeadquarter)
            .swiftCode(swiftCodeRequest.getSwiftCode().toUpperCase(Locale.ROOT))
            .build();
    swiftCodeRepository.save(swiftCode);
    log.info("Created Swift Code entry for: {}", swiftCode.getSwiftCode());
  }

  /**
   * Deletes a Swift code entry from the database.
   *
   * @param swiftCode The Swift code to be deleted (case-insensitive)
   * @throws EntityNotFoundException if the Swift code is not found
   */
  @Transactional
  public void deleteBySwiftCode(String swiftCode) {
    String upperCaseSwiftCode = swiftCode.toUpperCase(Locale.ROOT);
    SwiftCode entityBySwiftCode = swiftCodeRepository.findBySwiftCode(upperCaseSwiftCode)
            .orElseThrow(() -> new EntityNotFoundException(
                    String.format("SWIFT Code not found: %s", upperCaseSwiftCode)));
    log.info("Deleted Swift Code entry: {}", upperCaseSwiftCode);
    swiftCodeRepository.delete(entityBySwiftCode);
  }

  /**
   * Imports Swift codes from a predefined source (e.g., Google Sheets CSV).
   * Logs the number of successfully imported Swift codes.
   */
  private void importSwiftCodes() {
    List<SwiftCode> swiftCodes = new ArrayList<>();

    try {
      URL url = new URL(SwiftCodeConstants.GOOGLE_SHEETS_CSV_URL);
      BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      reader.readLine();

      readSwiftCodes(reader, swiftCodes);
      swiftCodeRepository.saveAll(swiftCodes);
      log.info("Successfully imported {} Swift Codes to database", swiftCodes.size());
      reader.close();
    } catch (IOException e) {
      log.error("Error reading Swift Codes from source", e);
    }

  }

  /**
   * Reads Swift codes from a BufferedReader and populates the provided list.
   *
   * @param reader BufferedReader containing Swift code data
   * @param swiftCodes List to be populated with parsed Swift codes
   * @throws IOException if an error occurs while reading the input
   */
  private void readSwiftCodes(BufferedReader reader, List<SwiftCode> swiftCodes) throws IOException {
    String line;
    boolean isHeadquarter = false;
    while ((line = reader.readLine()) != null) {
      String[] a = line.split("\"");

      if (a[SwiftCodeConstants.SWIFT_CODE_INDEX].endsWith(SwiftCodeConstants.SWIFT_CODE_ENDING_XXX)) {
        isHeadquarter = true;
      }

      SwiftCode swiftCode = SwiftCode.builder()
              .countryISO2(a[SwiftCodeConstants.COUNTRY_ISO2_INDEX])
              .swiftCode(a[SwiftCodeConstants.SWIFT_CODE_INDEX])
              .bankName(a[SwiftCodeConstants.BANK_NAME_INDEX])
              .address(a[SwiftCodeConstants.ADDRESS_INDEX])
              .countryName(a[SwiftCodeConstants.COUNTRY_NAME_INDEX])
              .isHeadquarter(isHeadquarter)
              .build();

      swiftCodes.add(swiftCode);
      isHeadquarter = false;
    }
  }

  /**
   * Builds a SwiftCodeResponse for a headquarters Swift code.
   * Retrieves and includes all non-headquarters branches for the same bank.
   *
   * @param swiftCodeEntity The headquarters SwiftCode entity
   * @return SwiftCodeResponse containing headquarters details and its branches
   */
  private SwiftCodeResponse buildSwiftCodeResponseHeadquarter(SwiftCode swiftCodeEntity) {
    List<SwiftCode> branchesByHeadquarterBankName = swiftCodeRepository
            .findBranchesByHeadquarterBankName(swiftCodeEntity.getBankName());
    List<SwiftCodeResponseBranches> swiftCodeResponseBranches = getSwiftCodeResponseBranches(branchesByHeadquarterBankName);

    return SwiftCodeResponse.builder()
            .address(swiftCodeEntity.getAddress())
            .bankName(swiftCodeEntity.getBankName())
            .countryISO2(swiftCodeEntity.getCountryISO2())
            .countryName(swiftCodeEntity.getCountryName())
            .isHeadquarter(swiftCodeEntity.isHeadquarter())
            .swiftCode(swiftCodeEntity.getSwiftCode())
            .branches(swiftCodeResponseBranches)
            .build();
  }

  /**
   * Converts a list of SwiftCode entities to a list of SwiftCodeResponseBranches.
   * Filters out headquarters codes (ending with XXX) to include only branch codes.
   *
   * @param branchesByHeadquarterBankName List of SwiftCode entities to be converted
   * @return List of SwiftCodeResponseBranches representing bank branches
   */
  private static List<SwiftCodeResponseBranches> getSwiftCodeResponseBranches(List<SwiftCode> branchesByHeadquarterBankName) {
    return branchesByHeadquarterBankName.stream()
            .filter(branch -> !branch.getSwiftCode().endsWith("XXX"))
            .map(branch -> SwiftCodeResponseBranches
                    .builder()
                    .address(branch.getAddress())
                    .bankName(branch.getBankName())
                    .countryISO2(branch.getCountryISO2())
                    .isHeadquarter(branch.isHeadquarter())
                    .swiftCode(branch.getSwiftCode())
                    .build())
            .collect(Collectors.toList());
  }

  /**
   * Builds a SwiftCodeResponse for a branch Swift code.
   * Creates a response without any additional branch information.
   *
   * @param swiftCodeEntity The branch SwiftCode entity
   * @return SwiftCodeResponse containing branch details
   */
  private SwiftCodeResponse buildSwiftCodeResponseBranch(SwiftCode swiftCodeEntity) {
    return SwiftCodeResponse.builder()
            .address(swiftCodeEntity.getAddress())
            .bankName(swiftCodeEntity.getBankName())
            .countryISO2(swiftCodeEntity.getCountryISO2())
            .countryName(swiftCodeEntity.getCountryName())
            .isHeadquarter(swiftCodeEntity.isHeadquarter())
            .swiftCode(swiftCodeEntity.getSwiftCode())
            .build();
  }

  /**
   * Converts a list of SwiftCode entities to a list of SwiftCodeResponseDetails.
   * Used for creating responses for Swift codes within a specific country.
   *
   * @param byCountryISO2 List of SwiftCode entities from a specific country
   * @return List of SwiftCodeResponseDetails representing Swift codes in the country
   */
  private List<SwiftCodeResponseByCountryDetails> getSwiftCodeResponseDetails(List<SwiftCode> byCountryISO2) {
    return byCountryISO2.stream()
            .map(branch -> SwiftCodeResponseByCountryDetails
                    .builder()
                    .address(branch.getAddress())
                    .bankName(branch.getBankName())
                    .countryISO2(branch.getCountryISO2())
                    .isHeadquarter(branch.isHeadquarter())
                    .swiftCode(branch.getSwiftCode())
                    .build())
            .collect(Collectors.toList());
  }
}
