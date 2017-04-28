package com.techmojo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.techmojo.model.Employee;
import com.techmojo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Collection<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findOne(Long id) {
        return employeeRepository.findOne(id);
    }

    @Override
    public Long createEmployee(String firstName, String lastName, String designation) {
        Employee employee = new Employee();
        employee.setFirst_name(firstName);
        employee.setLast_name(lastName);
        employee.setDesignation(designation);
        employee.setCasual_leaves(TechMojoConstants.CASUAL_LEAVE_COUNT);
        employee.setSick_leaves(TechMojoConstants.SICK_LEAVE_COUNT);
        employee = employeeRepository.save(employee);
        return employee.getId();
    }

    @Override
    public String applyLeave(Long empId, String startDate, String endDate, String leaveType) {
        Employee employee = findOne(empId);
        int workDays = 0;
        if (employee != null) {
            try {
                Date sDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
                Date eDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);

                Calendar startCal = Calendar.getInstance();
                startCal.setTime(sDate);

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(eDate);
                if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
                    return "Start and EndDate cannot be same";
                }
                if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
                    return "StartDate cannot be more than EndDate";
                }

                while(startCal.getTimeInMillis() <= endCal.getTimeInMillis()){
                    startCal.add(Calendar.DAY_OF_MONTH, 1);
                    if (startCal.get(Calendar.DAY_OF_WEEK) != 1 && startCal.get(Calendar.DAY_OF_WEEK) != 2) {
                        workDays = workDays+1;
                    }
                }

                if (workDays < 1) {
                    return "No working days found between the given days";
                }

                switch (leaveType) {
                    case "CL": {
                        Integer leaveCount = employee.getCasual_leaves();
                        if(leaveCount > workDays){
                            Integer balanceCount = leaveCount - workDays;
                            employee.setCasual_leaves(balanceCount);
                            employeeRepository.save(employee);
                        } else {
                            return "Insufficient Balance";
                        }
                        break;
                    }
                    case "SL": {
                        Integer leaveCount = employee.getSick_leaves();
                        if(leaveCount > workDays){
                            Integer balanceCount = leaveCount - workDays;
                        employee.setSick_leaves(balanceCount);
                        employeeRepository.save(employee);
                        } else {
                            return "Insufficient Balance";
                        }
                        break;
                    }
                    default:
                        return "Please specify a proper leave type";
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return "Invalid date type please user dd/MM/yyyy format";
            }
        }
        return "Successfully applied leave for " + workDays + " working Days";
    }

    @Override
    public String getYearlyBalance(Long empId) {
        Employee employee = findOne(empId);
        Integer casual = employee.getCasual_leaves();
        Integer sick = employee.getSick_leaves();
        Integer yearlyBalance = casual + sick;
        StringBuilder balance = new StringBuilder();
        balance.append("Yearly Balance: " + yearlyBalance+"\n");
        balance.append("Causal Balance: " + casual+"\n");
        balance.append("SickLeave Balance: " + sick + "\n");

        return balance.toString();
    }
}
