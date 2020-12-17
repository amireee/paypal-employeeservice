package com.paypal.bfs.test.repo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.paypal.bfs.test.employeeserv.ValidatorService;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.exception.InvalidInputException;

@Repository
public class EmployeeRepository {
	
	private static Logger logger = LoggerFactory.getLogger(EmployeeRepository.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	@Autowired
	ValidatorService validator;

	public List<Employee> getAllEmployees() throws Exception {

		final List<Employee> list = new ArrayList<Employee>();
		jdbcTemplate.query("Select * from Employee", new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Employee item = new Employee();
				item.setId(rs.getInt("ID"));
				item.setFirstName(rs.getString("firstName"));
				item.setLastName(rs.getString("lastName"));
				item.setAddress1(rs.getString("Address1"));
				item.setAddress2(rs.getString("Address2"));
				item.setZip(rs.getString("Zip"));
				item.setCity(rs.getString("CITY"));
				Date d =rs.getDate("DOB");
				String dateString=validator.getDateFormat().format(d);
				item.setDob(dateString);
				list.add(item);
			}
		});
		 logger.info("Employee list size : "+list.size());
		return list;
	}

	public Integer createEmployee(final Employee e) throws Exception {

		String query = "INSERT INTO EMPLOYEE (ID,FirstName, LastName, Dob, Address1, Address2,CITY,ZIP)  values(?,?,?,?,?,?,?,?)";

		return jdbcTemplate.execute(query, new PreparedStatementCallback<Integer>() {
			@Override
			public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(1, e.getId());
				ps.setString(2, e.getFirstName());
				ps.setString(3, e.getLastName());
				Date d= validator.getDate(e.getDob());
				ps.setDate(4, d);
				ps.setString(5, e.getAddress1());
				ps.setString(6, e.getAddress2());
				ps.setString(7, e.getCity());
				ps.setString(8, e.getZip());
				
				int status= ps.executeUpdate();
				 logger.info("Employee creation status : "+status);
				 return e.getId();

			}
		});

	}

	public Employee getEmployeeByID(Integer id) throws Exception {

		String query = "SELECT * FROM EMPLOYEE WHERE ID=:ID";
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
		SqlParameterSource param = new MapSqlParameterSource("ID", id);
		Employee result = template.queryForObject(query, param, BeanPropertyRowMapper.newInstance(Employee.class));
		 logger.info("Employee data found  for ID: "+result.getId());
		return result;

	}
	
	
	public boolean delEmployeeByID(Integer id) throws Exception {

		String query = "DELETE from EMPLOYEE where id=?";
		Object[] params= {id};
		int[] types= {Types.BIGINT};
		int rows=jdbcTemplate.update(query,params,types);
		logger.info("Employee data found and deleted  for ID: "+id );
		return rows>0?true:false;

	}
	

	
	public boolean updateEmployeeByID(Employee emp) throws Exception {

		String query = "UPDATE EMPLOYEE set FIRSTNAME=?,LASTNAME=? , Dob=?, Address1=?, Address2=?,CITY=?, Zip=? where ID = ?";
		
		Date d = validator.getDate(emp.getDob()) ;
		Object[] params= {emp.getFirstName(), emp.getLastName() , d , emp.getAddress1(), emp.getAddress2(),
				emp.getCity() , emp.getZip(),emp.getId()};
		int[] types= {Types.VARCHAR, Types.VARCHAR, Types.DATE, Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR, Types.BIGINT};
		int rows=jdbcTemplate.update(query,params,types);
		logger.info("Employee data found and updated  for ID: "+emp.getId() );
		return rows>0?true:false;
	}
	
	
}