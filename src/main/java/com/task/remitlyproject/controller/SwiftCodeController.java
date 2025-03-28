package com.task.remitlyproject.controller;

import com.task.remitlyproject.dto.request.SwiftCodeRequest;
import com.task.remitlyproject.dto.response.SwiftCodeResponse;
import com.task.remitlyproject.dto.response.SwiftCodeResponseByCountry;
import com.task.remitlyproject.service.SwiftCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/v1/swift-codes")
@RequiredArgsConstructor
@Tag(name = "SWIFT Code Controller")
public class SwiftCodeController {

  private final SwiftCodeService swiftCodeService;

  @Operation(summary = "Retrieve details of a single SWIFT code whether for a headquarters or branches.")
  @GetMapping("/{swift-code}")
  public ResponseEntity<SwiftCodeResponse> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
    SwiftCodeResponse swiftCodeResponse = swiftCodeService.getSwiftCodeForHeadquarterOrBranch(swiftCode);
    return ResponseEntity.status(HttpStatus.OK).body(swiftCodeResponse);
  }

  @Operation(summary = "Return all SWIFT codes with details for a specific country (both headquarters and branches).")
  @GetMapping("/country/{countryISO2code}")
  public ResponseEntity<Object> getAllSwiftCodesByCountry(@PathVariable("countryISO2code") String countryISO2) {
    SwiftCodeResponseByCountry allSwiftCodesByCountry = swiftCodeService.getAllSwiftCodesByCountry(countryISO2);
    return ResponseEntity.status(HttpStatus.OK).body(allSwiftCodesByCountry);
  }

  @Operation(summary = "Add new SWIFT code for a specific country")
  @PostMapping
  public ResponseEntity<Map<String, String>> addSwiftCode(@RequestBody SwiftCodeRequest swiftCodeRequest) {
    swiftCodeService.saveSwiftCodeEntry(swiftCodeRequest);
    return ResponseEntity.ok(Map.of("message",
            "SWIFT code saved to database successfully for a specific country."));
  }

  @Operation(summary = "Delete swift-code data if swift code matches the one in the database")
  @DeleteMapping("/{swift-code}")
  public ResponseEntity<Map<String, String>> deleteBySwiftCode(@PathVariable("swift-code") String swiftCode) {
    swiftCodeService.deleteBySwiftCode(swiftCode);
    return ResponseEntity.ok(Map.of("message", "SWIFT code entry deleted successfully."));
  }
}
