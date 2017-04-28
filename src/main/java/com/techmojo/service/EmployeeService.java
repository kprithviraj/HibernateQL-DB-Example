package com.techmojo.service;

import java.util.Collection;

import com.techmojo.model.Employee;

public interface EmployeeService {

    Collection<Employee> findAll();

    Employee findOne(Long id);

    Long createEmployee(String firstName, String lastName, String designation);

    String applyLeave(Long empId, String startDate, String endDate, String leaveType);

    String getYearlyBalance(Long empId);
}
