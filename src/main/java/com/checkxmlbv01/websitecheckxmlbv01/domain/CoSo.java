package com.checkxmlbv01.websitecheckxmlbv01.domain;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "co_so")
public class CoSo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Mã cơ sở không được để trống")
    private String maCoSo;

    @Column(nullable = false)
    @NotBlank(message = "Tên cơ sở không được để trống")
    private String tenCoSo;

    private LocalDateTime taoLuc;
}
