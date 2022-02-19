package com.covid.coronavirostracker.repository;

import com.covid.coronavirostracker.Modal.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}
