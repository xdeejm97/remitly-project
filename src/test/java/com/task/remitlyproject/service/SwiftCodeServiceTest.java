package com.task.remitlyproject.service;


import com.task.remitlyproject.dto.request.SwiftCodeRequest;
import com.task.remitlyproject.dto.response.SwiftCodeResponse;
import com.task.remitlyproject.dto.response.SwiftCodeResponseByCountry;
import com.task.remitlyproject.entity.SwiftCode;
import com.task.remitlyproject.exception.SwiftCodeAlreadyExistsException;
import com.task.remitlyproject.repository.SwiftCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SwiftCodeServiceTest {

  @Mock
  private SwiftCodeRepository swiftCodeRepository;

  @InjectMocks
  private SwiftCodeService swiftCodeService;

  private SwiftCode headQuarterSwiftCode;
  private SwiftCode branchSwiftCode;

  @BeforeEach
  void setUp() {
    headQuarterSwiftCode = SwiftCode.builder()
            .swiftCode("EXAMPLEXXX")
            .bankName("EXAMPLE BANK")
            .address("HEAD OFFICE ADDRESS")
            .countryISO2("US")
            .countryName("UNITED STATES")
            .isHeadquarter(true)
            .build();

    branchSwiftCode = SwiftCode.builder()
            .swiftCode("EXAMPLEABC")
            .bankName("EXAMPLE BANK")
            .address("BRANCH ADDRESS")
            .countryISO2("US")
            .countryName("UNITED STATES")
            .isHeadquarter(false)
            .build();
  }

  @Test
  void testGetSwiftCodeForHeadquarter() {
    when(swiftCodeRepository.findBySwiftCode("EXAMPLEXXX"))
            .thenReturn(Optional.of(headQuarterSwiftCode));

    when(swiftCodeRepository.findBranchesByHeadquarterBankName("EXAMPLE BANK"))
            .thenReturn(Arrays.asList(branchSwiftCode));

    SwiftCodeResponse response = swiftCodeService.getSwiftCodeForHeadquarterOrBranch("EXAMPLEXXX");

    assertNotNull(response);
    assertEquals("EXAMPLEXXX", response.getSwiftCode());
    assertEquals(1, response.getBranches().size());
    assertEquals("EXAMPLEABC", response.getBranches().get(0).getSwiftCode());
  }

  @Test
  void testGetSwiftCodeForBranch() {
    when(swiftCodeRepository.findBySwiftCode("EXAMPLEABC"))
            .thenReturn(Optional.of(branchSwiftCode));

    SwiftCodeResponse response = swiftCodeService.getSwiftCodeForHeadquarterOrBranch("EXAMPLEABC");

    assertNotNull(response);
    assertEquals("EXAMPLEABC", response.getSwiftCode());
    assertNull(response.getBranches());
  }

  @Test
  void testGetSwiftCodeNotFound() {
    when(swiftCodeRepository.findBySwiftCode(anyString()))
            .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
            () -> swiftCodeService.getSwiftCodeForHeadquarterOrBranch("NONEXISTENT"));
  }

  @Test
  void testGetAllSwiftCodesByCountry() {
    List<SwiftCode> countryCodes = Arrays.asList(headQuarterSwiftCode, branchSwiftCode);
    when(swiftCodeRepository.findByCountryISO2("US"))
            .thenReturn(countryCodes);

    SwiftCodeResponseByCountry response = swiftCodeService.getAllSwiftCodesByCountry("US");

    assertNotNull(response);
    assertEquals("US", response.getCountryISO2());
    assertEquals("UNITED STATES", response.getCountryName());
    assertEquals(2, response.getSwiftCodes().size());
  }

  @Test
  void testSaveSwiftCodeEntry() {
    SwiftCodeRequest request = SwiftCodeRequest.builder()
            .swiftCode("NEWBANKXXX")
            .bankName("NEW BANK")
            .address("NEW ADDRESS")
            .countryISO2("CA")
            .countryName("CANADA")
            .build();

    when(swiftCodeRepository.existsBySwiftCode(anyString()))
            .thenReturn(false);

    swiftCodeService.saveSwiftCodeEntry(request);

    verify(swiftCodeRepository, times(1)).save(any(SwiftCode.class));
  }

  @Test
  void testSaveSwiftCodeEntryAlreadyExists() {
    SwiftCodeRequest request = SwiftCodeRequest.builder()
            .swiftCode("EXISTINGXXX")
            .build();

    when(swiftCodeRepository.existsBySwiftCode(anyString()))
            .thenReturn(true);

    assertThrows(SwiftCodeAlreadyExistsException.class,
            () -> swiftCodeService.saveSwiftCodeEntry(request));
  }

  @Test
  void testDeleteBySwiftCode() {
    String swiftCode = "DELETECODE";
    SwiftCode toDelete = SwiftCode.builder()
            .swiftCode(swiftCode)
            .build();

    when(swiftCodeRepository.findBySwiftCode(swiftCode.toUpperCase()))
            .thenReturn(Optional.of(toDelete));

    swiftCodeService.deleteBySwiftCode(swiftCode);

    verify(swiftCodeRepository, times(1)).delete(toDelete);
  }

  @Test
  void testDeleteBySwiftCodeNotFound() {
    when(swiftCodeRepository.findBySwiftCode(anyString()))
            .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
            () -> swiftCodeService.deleteBySwiftCode("NONEXISTENT"));
  }
}