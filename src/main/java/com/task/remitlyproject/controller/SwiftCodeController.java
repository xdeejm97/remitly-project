package com.task.remitlyproject.controller;

import com.task.remitlyproject.dto.request.SwiftCodeRequest;
import com.task.remitlyproject.service.SwiftCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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


  @Operation(summary = "Add new SWIFT code for a specific country")
  @PostMapping
  public ResponseEntity<Map<String, String>> addSWIFTCode(@RequestBody SwiftCodeRequest swiftCodeRequest) {
    swiftCodeService.saveSwiftCodeEntry(swiftCodeRequest);
    return ResponseEntity.ok(Map.of("message", "SWIFT code saved to database successfully"));
  }

  @Operation(summary = "Delete swift-code data if swift code matches the one in the database")
  @DeleteMapping("/{swift-code}")
  public ResponseEntity<Map<String, String>> deleteBySwiftCode(@PathVariable("swift-code") String swiftCode) {
    swiftCodeService.deleteBySwiftCode(swiftCode);
    return ResponseEntity.ok(Map.of("message", "SWIFT code deleted successfully"));
  }
}
