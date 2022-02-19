package com.covid.coronavirostracker.controller;

import com.covid.coronavirostracker.Modal.Person;
import com.covid.coronavirostracker.exception.RecordNotFoundException;
import com.covid.coronavirostracker.services.PersonService;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class HomeController {

    @Autowired
    private PersonService service;

    private List<Person> persons = null;

    @GetMapping("/")
    public String home(Model model) {
        List<Person> list = service.getAllPerson();
        if (!list.isEmpty()){
            persons = list;
        }
        model.addAttribute("persons", list);
        return "list-employees";
    }


    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editPersonById(Model model, @PathVariable("id") Optional<Integer> id)
            throws RecordNotFoundException
    {
        if (id.isPresent()) {
            Person entity = service.getPersonById(id.get());
            model.addAttribute("person", entity);
        } else {
            model.addAttribute("person", new Person());
        }
        return "add-edit-employee";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deletePersonById(Model model, @PathVariable("id") int id)
            throws RecordNotFoundException
    {
        service.deleteEmployeeById(id);
        return "redirect:/";
    }


    @RequestMapping(path = "/priority")
    public String allocatePriority(Model model)
            throws RecordNotFoundException
    {
        if (persons == null){
            return "redirect:/";
        }
        if (persons.size() <= 0){
            return "redirect:/";
        }

        for (Person p:persons) {
            if (p.getAge() >= 18 && p.getAge() <= 64 && p.getHasMedicalCondition().equalsIgnoreCase("yes"))
                p.setPriority(6);
            else if (p.getAge() > 90)
                p.setPriority(10);
            else if (p.getAge() > 80)
                p.setPriority(9);
            else if (p.getAge() > 70)
                p.setPriority(8);
            else if (p.getAge() <= 18)
                p.setPriority(1);
            else if (p.getAge() > 18 && p.getAge() <= 29)
                p.setPriority(2);
            else if (p.getAge() >= 30 && p.getAge() <= 44)
                p.setPriority(3);
            else if (p.getAge() >= 45 && p.getAge() <= 54)
                p.setPriority(4);
            else if (p.getAge() >= 55 && p.getAge() <= 64)
                p.setPriority(5);
            else if (p.getAge() >= 64 && p.getAge() <= 69)
                p.setPriority(7);
        }

        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getPriority() -  o2.getPriority();
            }
        });

        model.addAttribute("persons", persons);
        return "list-priority-employees";
    }

    @RequestMapping(path = "/createPerson", method = RequestMethod.POST)
    public String createOrUpdatePerson(Person person)
    {
        service.createOrUpdatePerson(person);
        return "redirect:/";
    }


}
