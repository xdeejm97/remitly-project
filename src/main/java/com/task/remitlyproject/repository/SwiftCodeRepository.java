package com.task.remitlyproject.repository;

import com.task.remitlyproject.entity.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
/**
 * Repository for the Swift code entity.
 */
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {

  void deleteBySwiftCode(String swiftCode);

  boolean existsBySwiftCode(String swiftCode);

  Optional<SwiftCode> findBySwiftCode(String swiftCode);

  @Query("SELECT s FROM SwiftCode s WHERE s.bankName = :bankName")
  List<SwiftCode> findBranchesByHeadquarterBankName(@Param("bankName") String bankName);

  List<SwiftCode> findByCountryISO2(String bankName);

}
