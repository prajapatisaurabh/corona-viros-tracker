package com.covid.coronavirostracker.services;

import com.covid.coronavirostracker.Modal.Person;
import com.covid.coronavirostracker.exception.RecordNotFoundException;
import com.covid.coronavirostracker.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {

    @Autowired
    private PersonRepository repo;

    public List<Person> getAllPerson() {
        List<Person> personList = repo.findAll();
        if (personList.size() > 0 ){
            return personList;
        }
        return new ArrayList<Person>();
    }

    public Person getPersonById(int id) throws RecordNotFoundException
    {
        Optional<Person> person = repo.findById(id);

        if(person.isPresent()) {
            return person.get();
        } else {
            throw new RecordNotFoundException("No person record exist for given id");
        }
    }

    public Person createOrUpdatePerson(Person entity){
        if(entity.getId() != 0)
        {
            entity = repo.save(entity);
            return entity;
        }
        else
        {
            Optional<Person> person = repo.findById(entity.getId());

            if(person.isPresent())
            {
                Person newEntity = person.get();
                newEntity.setName(entity.getName());
                newEntity.setAge(entity.getAge());
                newEntity.setHasMedicalCondition(entity.getHasMedicalCondition());

                newEntity = repo.save(newEntity);

                return newEntity;
            } else {
                entity = repo.save(entity);
                return entity;
            }
        }
    }

    public void deleteEmployeeById(int id) throws RecordNotFoundException {
        Optional<Person> employee = repo.findById(id);

        if (employee.isPresent()) {
            repo.deleteById(id);
        } else {
            throw new RecordNotFoundException("No employee record exist for given id");
        }
    }
}
