package com.task.remitlyproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SWIFT entity class.
 */
@Entity
@Table(name = "swift_codes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SwiftCodes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String address;
  private String bankName;
  private String countryISO2;
  private String countryName;
  private boolean isHeadquarter;
  private String swiftCode;

}
