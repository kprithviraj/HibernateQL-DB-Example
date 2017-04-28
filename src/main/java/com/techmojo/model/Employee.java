package com.techmojo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue
    private Long id;
    private String first_name;
    public String last_name;
    public String designation;
    public Integer casual_leaves;
    public Integer sick_leaves;
}
