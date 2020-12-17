package com.paypal.bfs.test.employeeserv;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.InvalidInputException;
import com.paypal.bfs.test.repo.EmployeeRepository;

@Service
public class ValidatorService {
	
	private static Logger logger = LoggerFactory.getLogger(ValidatorService.class);
	
	
	private static final SimpleDateFormat newPattern = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@Autowired
	EmployeeRepository employeeRepo;
	
	
	
	public Date getDate(String date)
	{
	  try {
		  
		  
		 // java.util.Date d = newPattern.parse(date);
		  java.sql.Date d2= java.sql.Date.valueOf(date);
			return d2;
	  }catch(Exception ex)
	  {
		  ex.printStackTrace();
		 return null;
	  }
		
	}
	
	
	public Employee validateEmployee(Employee e)throws InvalidInputException
	{
		if(!isEmployeeExist(e))
		{
			logger.info(" Employee ID is Valid :"+e.getId());
		}
		
		if(!isValidZip(e))
		{
			throw new InvalidInputException("{status :" + 500 + " , Message :" + "Employee creation failed,Please check the zip}");
		}
		
		return e;
		
	}
	
	
	
	private  boolean isEmployeeExist(Employee e)
	{
		try {
			Employee employee= employeeRepo.getEmployeeByID(e.getId());
			if(employee!=null)
			{
				return true;
			}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				
			}
		return false;
	}
	
	
	private boolean isValidZip(Employee e)
	{
		String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
		Pattern pattern = Pattern.compile(regex);		
		return pattern.matches(regex, e.getZip());
	}

	
	
	public SimpleDateFormat getDateFormat() {
		return newPattern;
	}
}
