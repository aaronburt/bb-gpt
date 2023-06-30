package com.example.exampleplugin.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The example repository for all the test names in the system
 *
 * @author me@bdavies (Ben Davies)
 * @since 1.0.0
 */
@Repository
public interface TestRepository extends JpaRepository<TestEntity, Integer> {
}
