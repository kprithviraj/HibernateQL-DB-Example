package com.techmojo.web.controllers;

import com.techmojo.model.Employee;
import com.techmojo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/createEmployee", method = RequestMethod.GET)
    public String createEmployee (@RequestParam("fname") String firstName,
                                  @RequestParam("lname") String lastName,
                                  @RequestParam("designation") String designation) {

        Long id =  employeeService.createEmployee(firstName, lastName, designation);

        return  "Successfully created user with id : " + id;
    }

    @RequestMapping(value = "/applyLeave", method = RequestMethod.GET)
    public String applyLeave (@RequestParam("empId") Long empId,
                              @RequestParam("startDate") String startDate,
                              @RequestParam("endDate") String endDate,
                              @RequestParam("leaveType") String leaveType) {

        return employeeService.applyLeave(empId, startDate, endDate, leaveType);
    }


    @RequestMapping(value = "/getAllEmployees",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Employee>> getAllEmployees() {

        Collection<Employee> employees = employeeService.findAll();
        return new ResponseEntity<Collection<Employee>>(employees,HttpStatus.OK);
    }

    @RequestMapping(value = "/getYearlyBalance",method = RequestMethod.GET)
    public String getYearlyBalance(@RequestParam("empId") Long empId) {
        return employeeService.getYearlyBalance(empId);
    }
}
