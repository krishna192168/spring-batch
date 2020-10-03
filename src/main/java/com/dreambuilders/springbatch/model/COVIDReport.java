package com.dreambuilders.springbatch.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "covid_report")
public class COVIDReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;

    @Column(name = "new_confirmed")
    private Long newConfirmed;

    @Column(name = "total_confirmed")
    private Long totalConfirmed;

    @Column(name = "new_death")
    private Long newDeath;

    @Column(name = "new_recovered")
    private Long newRecovered;

    @Column(name = "total_recovered")
    private Long totalRecovered;
}
