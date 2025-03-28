package com.task.remitlyproject.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Swift entity class.
 */
@Entity
@Table(name = "swift_codes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SwiftCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "country_ISO2_code")
  private String countryISO2;
  @Column(name = "swift_code", nullable = false)
  private String swiftCode;
  @Column(name = "bank_name")
  private String bankName;
  private String address;
  @Column(name = "country_name")
  private String countryName;
  private boolean isHeadquarter;

}
