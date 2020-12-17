package com.paypal.bfs.test.employeeserv.api;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.DatanotFoundException;
import com.paypal.bfs.test.employeeserv.exception.InvalidInputException;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Interface for employee resource operations.
 */
public interface EmployeeResource {

    /**
     * Retrieves the {@link Employee} resource by id.
     *
     * @param id employee id.
     * @return {@link Employee} resource.
     */
    @RequestMapping("/v1/bfs/employees/{id}")
    ResponseEntity<Employee> employeeGetById(@PathVariable("id") String id) ;
    
    @RequestMapping("/v1/bfs/emp/{id}")
    ResponseEntity<Employee> getEmployeeBy(@PathVariable("id") String id) throws DatanotFoundException;
    
    @RequestMapping( value="/v1/bfs/empdel/{id}", method = RequestMethod.DELETE)
    ResponseEntity<String> delEmployeeBy(@PathVariable("id") String id) throws DatanotFoundException;
    
    
    @RequestMapping(method = RequestMethod.PUT, value="/v1/bfs/empupdate", consumes = "application/json", produces = "application/json")
    ResponseEntity<Employee> updateEmployeeById(@RequestBody Employee e) throws DatanotFoundException;
    
    
    @RequestMapping(method = RequestMethod.POST, value="/v1/bfs/employee", consumes = "application/json", produces = "application/json")
    ResponseEntity<Employee> createEmployee(@RequestBody Employee e)throws InvalidInputException;
    
    
    @RequestMapping("/v1/bfs/employees/list")
    ResponseEntity<List<Employee>> employeeList()  throws DatanotFoundException;
    
    // ----------------------------------------------------------
    // TODO - add a new operation for creating employee resource.
    // ----------------------------------------------------------
}
