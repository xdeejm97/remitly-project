package com.task.remitlyproject.service;

import com.task.remitlyproject.dto.request.SwiftCodeRequest;
import com.task.remitlyproject.entity.SwiftCode;
import com.task.remitlyproject.repository.SwiftCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Log4j2
public class SwiftCodeService {

  private final SwiftCodeRepository swiftCodeRepository;


  public void saveSwiftCodeEntry(SwiftCodeRequest swiftCodeRequest) {
    swiftCodeRepository.save(SwiftCode.builder()
                    .address(swiftCodeRequest.address().toUpperCase(Locale.ROOT))
                    .bankName(swiftCodeRequest.bankName().toUpperCase(Locale.ROOT))
                    .countryISO2(swiftCodeRequest.countryISO2().toUpperCase(Locale.ROOT))
                    .countryName(swiftCodeRequest.countryName().toUpperCase(Locale.ROOT))
                    .isHeadquarter(swiftCodeRequest.isHeadquarter())
                    .swiftCode(swiftCodeRequest.swiftCode().toUpperCase(Locale.ROOT))
            .build());
    log.info("Attempt to create swift code entry");
  }

  public void deleteBySwiftCode(String swiftCode) {
    SwiftCode entityBySwiftCode = swiftCodeRepository.findBySwiftCode(swiftCode.toUpperCase(Locale.ROOT));
    if(entityBySwiftCode == null) {
      throw new EntityNotFoundException("SwiftCode not found: " + swiftCode.toUpperCase(Locale.ROOT));
    }
    swiftCodeRepository.delete(entityBySwiftCode);
    log.info("Attempt to delete swift code entry by swift code");

  }
}
