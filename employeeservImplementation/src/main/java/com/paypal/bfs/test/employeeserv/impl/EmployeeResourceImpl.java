package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.ValidatorService;
import com.paypal.bfs.test.employeeserv.api.EmployeeResource;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.DatanotFoundException;
import com.paypal.bfs.test.employeeserv.exception.InvalidInputException;
import com.paypal.bfs.test.repo.EmployeeRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {

	private static Logger logger = LoggerFactory.getLogger(EmployeeResourceImpl.class);

	@Autowired
	EmployeeRepository employeeRepo;
	
	@Autowired
	ValidatorService validator;

	@Override
    public ResponseEntity<Employee> employeeGetById(String id) {

        Employee employee = new Employee();
        employee.setId(Integer.valueOf(id));
        employee.setFirstName("BFS");
        employee.setLastName("Developer");

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

	public ResponseEntity<List<Employee>> employeeList() throws DatanotFoundException {

		List<Employee> list;

		try {
			list = employeeRepo.getAllEmployees();
			if (list.size() == 0) {
				throw new DatanotFoundException("No Employee found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DatanotFoundException("No Employee found");
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	public ResponseEntity<Employee> createEmployee(Employee e) throws InvalidInputException {

		try {
			validator.validateEmployee(e);
			Integer flag = employeeRepo.createEmployee(e);
			Employee employee = employeeRepo.getEmployeeByID(flag);
			return new ResponseEntity<>(employee, HttpStatus.OK);

		} catch (Exception ex) {

			if (ex.getMessage().contains("Unique index or primary key violation")) {
				throw new InvalidInputException("{status :" + 500 + " , Message :" + "Employee ID should be Unique }");
			}
			ex.printStackTrace();
			throw new InvalidInputException("{status :" + 500 + " , Message :" + "Employee creation failed Please check inputs}");
		}

	}

	
	public ResponseEntity<Employee> getEmployeeBy(String id)throws DatanotFoundException {
		try {
			Employee employee = employeeRepo.getEmployeeByID(Integer.parseInt(id));
			return new ResponseEntity<>(employee, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DatanotFoundException("{status :" + 404 + " , Message :" + "Employee Id not matching}");
		}
	}

	
	public ResponseEntity<String> delEmployeeBy(String id) throws DatanotFoundException {
		try {
			boolean del = employeeRepo.delEmployeeByID(Integer.parseInt(id));
			if(del) {
			return new ResponseEntity<>("{'status' : 200 , 'Message' : 'Employee Id processed'}", HttpStatus.OK);
			}
			throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DatanotFoundException("{'status' :" + 404 + " , 'Message' :" + "'Employee Id not matching'}");
		}
	}

	
	public ResponseEntity<Employee> updateEmployeeById(Employee e) throws DatanotFoundException {
		try {
			boolean update = employeeRepo.updateEmployeeByID( e );
			if(update) {
				Employee employee = employeeRepo.getEmployeeByID(e.getId() );
				return new ResponseEntity<>(employee, HttpStatus.OK);
			}
			throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new DatanotFoundException("{'status' :" + 404 + " , 'Message' :" + "'Employee Update is Failure please check your inputs'}");
		}
	}

}
