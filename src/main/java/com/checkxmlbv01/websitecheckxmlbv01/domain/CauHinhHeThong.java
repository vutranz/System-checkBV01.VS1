package com.checkxmlbv01.websitecheckxmlbv01.domain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cau_hinh_he_thong",
       uniqueConstraints = @UniqueConstraint(columnNames = {"co_so_id","khoa"}))
public class CauHinhHeThong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "co_so_id")
    private CoSo coSo;

    private String khoa;

    @Column(columnDefinition = "TEXT")
    private String giaTri;
}
