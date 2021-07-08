package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;

@Component
@Repository
public class EmployeeDaoImpl implements EmployeeDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private Employee employee;
	
	@Override
	public Employee selectEmployee(Employee employeeCode) {
		try {
		String sql = "SELECT * FROM employee WHERE employee_code = ?;";

		Map<String, Object>map = jdbcTemplate.queryForMap(sql, employeeCode.getEmployee_code());

		employee.setEmployee_code((String)map.get("employee_code"));
		employee.setName((String)map.get("name"));
		employee.setPost((String)map.get("post"));
		
//		if(rs.next() && rs.getString(1).equals(employeeCode.getEmoployee_code())){
//			employee.setEmoployee_code(rs.getString(1));
//			employee.setName(rs.getString(2));
//			employee.setPost(rs.getString(3));
//		}
		return employee;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Employee selectEmployeeSt(String employeeCode) {
		try {
		String sql = "SELECT * FROM employee WHERE employee_code = ?;";
		System.out.println(employeeCode);
		Map<String, Object>map = jdbcTemplate.queryForMap(sql, employeeCode);

		employee.setEmployee_code((String)map.get("employee_code"));
		employee.setName((String)map.get("name"));
		employee.setPost((String)map.get("post"));
		employee.setPassword((String)map.get("password"));
		
//		if(rs.next() && rs.getString(1).equals(employeeCode.getEmoployee_code())){
//			employee.setEmoployee_code(rs.getString(1));
//			employee.setName(rs.getString(2));
//			employee.setPost(rs.getString(3));
//		}
		return employee;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Employee> selectEmployeeAll() {
		String sql = "SELECT * FROM employee";

		List<Map<String, Object>> emps = jdbcTemplate.queryForList(sql);
		
		List<Employee> list = new ArrayList<Employee>();
		
		for(Map<String, Object> em: emps) {
			Employee employee = new Employee(
					(String)em.get("employee_code"),
					(String)em.get("name"),
					(String)em.get("post"),
					(String)em.get("password")
					);
			list.add(employee);
		}
		
		return list;
	}
	
	@Override
	public int insertEmployee(Employee edt) {
			String sql = "INSERT INTO employee (employee_code, name, post, password) VALUES (?, ?, ?, ?)";
			
			return jdbcTemplate.update(sql, edt.getEmployee_code(),
					edt.getName(), edt.getPost(), edt.getPassword());

	}
	
	@Override
	public int updateEmployee(Employee emp) {
		//try {
			String sql = "UPDATE employee SET name = '" + emp.getName()
			+ "', post = '" + emp.getPost()
			+ "', password = '"+ emp.getPassword()
			+ "' WHERE employee_code = '" + emp.getEmployee_code() + "';";
			
			return jdbcTemplate.update(sql);
//		} catch (Exception e) {
//			System.out.println("更新失敗");
//			e.printStackTrace();
//			return 0;
//		}
	}
	
	@Override
	public int deleteEmployee(String emp) {
		String sql = "DELETE FROM employee WHERE employee_code = '"
				+ emp + "';";
		
		return jdbcTemplate.update(sql);
	}
}
