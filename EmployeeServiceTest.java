package com.example.employees.service;

import com.example.employees.entity.Employee;
import com.example.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John Doe", "IT", 50000);
    }

    // ✅ createEmployee_validInput_success
    @Test
    void createEmployee_validInput_success() {
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee saved = employeeService.createEmployee(employee);

        assertNotNull(saved);
        assertEquals("John Doe", saved.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    // 🚫 createEmployee_nullName_throwsException
    @Test
    void createEmployee_nullName_throwsException() {
        employee.setName(null);
        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(employee));
    }

    // 🚫 createEmployee_invalidSalary_throwsException
    @Test
    void createEmployee_invalidSalary_throwsException() {
        employee.setSalary(0);
        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(employee));
    }

    // ✅ getEmployeeById_validId_success
    @Test
    void getEmployeeById_validId_success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Employee found = employeeService.getEmployeeById(1L);

        assertEquals("John Doe", found.getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    // 🚫 getEmployeeById_invalidId_throwsException
    @Test
    void getEmployeeById_invalidId_throwsException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> employeeService.getEmployeeById(1L));
    }

    // ✅ updateEmployee_validId_success
    @Test
    void updateEmployee_validId_success() {
        Employee updated = new Employee(null, "Jane Doe", "HR", 60000);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updated);

        Employee result = employeeService.updateEmployee(1L, updated);

        assertEquals("Jane Doe", result.getName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // 🚫 updateEmployee_invalidId_throwsException
    @Test
    void updateEmployee_invalidId_throwsException() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> employeeService.updateEmployee(1L, employee));
    }

    // ✅ deleteEmployee_validId_success
    @Test
    void deleteEmployee_validId_success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    // 🚫 deleteEmployee_invalidId_throwsException
    @Test
    void deleteEmployee_invalidId_throwsException() {
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> employeeService.deleteEmployee(1L));
    }
}
