package com.task.remitlyproject.repository;

import com.task.remitlyproject.entity.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {

  void deleteBySwiftCode(String swiftCode);
  SwiftCode findBySwiftCode(String swiftCode);

}
