package com.example.exampleplugin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Test Entity to show an example of db objects
 *
 * @author me@bdavies (Ben Davies)
 * @since 1.0.0
 */
@Entity
@Data
@Builder
@Slf4j
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class TestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;

    private final String name;
}
