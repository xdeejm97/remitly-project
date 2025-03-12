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
public class Swift {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

}
