package com.afs.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void cleanRepository() {
        employeeRepository.clearAll();
    }

    @Test
    void should_get_all_employee_when_perform_getAll_given_employees() throws Exception {
        // given
        employeeRepository.createEmployee(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.createEmployee(new Employee(11, "Bob", 11, "Male", 1000));
        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees"))
                // 1. assert response code
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Susan", "Bob")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
    }

    @Test
    void should_get_employee_by_id_when_perform_get_by_id_given_employees() throws Exception {
        // given
        Employee susan = employeeRepository.createEmployee(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.createEmployee(new Employee(11, "Bob", 11, "Male", 1000));
        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees/{id}", susan.getId()))
                // 1. assert response code
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(10000));
    }

    @Test
    void should_get_employee_by_gender_when_perform_get_by_gender_given_employees() throws Exception {
        // given
        Employee susan = employeeRepository.createEmployee(new Employee(10, "Susan", 22, "Female", 10000));
        employeeRepository.createEmployee(new Employee(11, "Bob", 11, "Male", 1000));
        // when & then
        client.perform(MockMvcRequestBuilders.get("/employees?gender={gender}", susan.getGender()))
                // 1. assert response code
                .andExpect(MockMvcResultMatchers.status().isOk())
                // 2. assert response data
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Susan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("Female"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(10000));
    }
}
