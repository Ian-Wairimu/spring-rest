package com.wairimuian.springrest.controller;

import com.wairimuian.springrest.entity.Employee;
import com.wairimuian.springrest.error.EmployeeNotFoundException;
import com.wairimuian.springrest.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;
    @Autowired
    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = repository.findAll()
                .stream().map(employee -> EntityModel.of(
                        employee,
                        linkTo(methodOn(EmployeeController.class).getEmployee(employee.getId())).withSelfRel(),
                        linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
                )).collect(Collectors.toList());
        return CollectionModel.of(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }
    @PostMapping("/employees")
    public Employee saveEmployee(@RequestBody Employee employee) {
        return repository.save(employee);
    }
    @GetMapping("employees/{id}")
    public EntityModel<Employee> getEmployee(@PathVariable Long id){
        Employee employee =  repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getEmployee(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
                );
    }
    @PutMapping("/employees/{id}")
    public Employee replaceEmployee(@RequestBody Employee employee, @PathVariable Long id){
        return repository.findById(id)
                .map(employee1 -> {
                    employee1.setName(employee.getName());
                    employee1.setRole(employee.getRole());
                    return repository.save(employee1);
                })
                .orElseGet(() -> {
                    employee.setId(id);
                    return repository.save(employee);
                });
    }
    @DeleteMapping("/employees/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
