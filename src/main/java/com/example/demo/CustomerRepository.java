package com.example.demo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select a from Customer a where a.id = :id")
    @EntityGraph("testGraph")
    Customer findByIdLocked(long id);
}
