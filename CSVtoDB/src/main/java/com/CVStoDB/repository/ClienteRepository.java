package com.CVStoDB.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CVStoDB.model.Customer;

public interface ClienteRepository extends JpaRepository<Customer, Integer>{

}
