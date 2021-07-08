package com.example.demo.dao;

import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;

	/**
	 * @author Kazuma Watanabe
	 * 出退勤時刻管理データベースと繋ぐDAOクラス。
	 */
@Component
@Repository
public class AttendanceEmployeeDaoImpl implements AttendanceEmployeeDao {
//		static Connection con = null;  //一度呼び出したら消えないようにするため、static
//		String tableName = "employee";
//		String tableName2 = "worktime";
		static Statement stmt = null;
//		private ResultSet rs = null;
//		private PreparedStatement ps = null;
		
		@Autowired
		private JdbcTemplate jdbcTemplate;   //JDBCでDBにアクセスするためのクラス

		/**
		 * 日付/時間オブジェクトの出力および解析のためのフォーマッタ。<br>
		 * "HH:mm:ss"のフォーマットで表記。
		 */
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
		/**
		 * 日付/時間オブジェクトの出力および解析のためのフォーマッタ。<br>
		 * "HH:mm:ss"のフォーマットで表記。
		 */

		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		@Override
		public Employee loginEmployee(String id) {
			Employee employee = new Employee();
			try {
			String sql = "SELECT * FROM employee WHERE employee_code=?";	
//			RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<Employee>(Employee.class);
//			jdbcTemplate.queryForObject(sql, rowMapper, id);
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, id);  //おそらくStmt型、Ps型両方いける。第2引数入れるか入れへんか
			employee.setEmployee_code((String)map.get("employee_code"));
			employee.setPassword((String)map.get("password"));
			
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return employee;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public boolean setStartTime(Employee employee) {
			//con.setAutoCommit(false);
		
			LocalDateTime now = LocalDateTime.now();
			
			try {
				//既にその日のデータが追加されていたらfalseを返す
			String sql = "SELECT startTime from worktime WHERE employee_code = ? and day = '" + now.format(dateFormat) + "';";
			return jdbcTemplate.query(sql, new Object[] {new String(employee.getEmployee_code())},
					rs -> {
						if(rs.next()) { //データあり
							System.out.println("データありの場合");
							return false;
						} else {
//				sql = "INSERT INTO worktime (employee_code, day, startTime) VALUES ('"
//				+ employee.getEmoployee_code() + "', '" + now.format(dateFormat) + "', '"
//				+ now.format(timeFormat) + "' );";
//				stmt.executeUpdate(sql);
							System.out.println("ここ通ってんの？");
							
							String sql2 = "INSERT INTO worktime (employee_code, day, startTime) VALUES (?, ?, ?);";
							jdbcTemplate.update(sql2,
									employee.getEmployee_code(),
									now.format(dateFormat),
									now.format(timeFormat));
							return true;
						}
			});
			} catch(Exception e) {
				return false;
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean setFinishTime(Employee employee) {
			//con.setAutoCommit(false);
			LocalDateTime now = LocalDateTime.now();
			
			try {
			//出勤が押されていなかったらfalseを返す
			String sql = "SELECT * from worktime WHERE employee_code = ? and day = '" + now.format(dateFormat) + "';";
			return jdbcTemplate.query(sql, new Object[] {new String(employee.getEmployee_code())},
					rs -> {
						if(!rs.next()) {
							
							System.out.println("退勤時間入ってます");
							return false;
						} else {
							String sql2 = "UPDATE worktime SET finishTime='" + now.format(timeFormat)
							+ "' WHERE employee_code='" + employee.getEmployee_code() + 
							"' AND day='" + now.format(dateFormat) + "'";
							
							jdbcTemplate.update(sql2);   //これでもOKということがわかった
							
							//con.commit();
							return true;
						}
			
			});
			} catch(Exception e) {
				return false;
			}
		}
}
