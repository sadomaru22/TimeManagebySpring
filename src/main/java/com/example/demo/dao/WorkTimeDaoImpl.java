package com.example.demo.dao;

import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.WorkTime;

@Component
@Repository
public class WorkTimeDaoImpl implements WorkTimeDao {
	String tableName = "worktime";
	//private ResultSet rs = null;
	static Statement stmt = null;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//@Transactional
//	@Override
//	public String selectStartTime(Employee employee) {
//		System.out.println(employee.getEmoployee_code());
//		try {
//			
//		String sql = "SELECT * FROM " + tableName + " WHERE employee_code = '" + employee.getEmoployee_code() +
//				"' AND day = '" + LocalDate.now() + "';";
//		System.out.println(LocalDate.now() + "= day");
//		Map<String, Object>map = jdbcTemplate.queryForMap(sql);
//		
//		if(map != null) {
//			return "disabled";  
//		} else {
//			
//			//System.out.println("nullpointer");
//			return null;
//		}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;   //null=データがない=書き込める
//		}
//	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String selectStartTime(Employee employee) {
		//System.out.println(employee.getEmoployee_code());
		try {	
		String sql = "SELECT startTime FROM " + tableName + " WHERE employee_code = ? AND day = '" + LocalDate.now() + "';";
		
		return jdbcTemplate.query(sql, new Object[] {new String(employee.getEmployee_code())},
				rs -> {
		
		if(rs.next()) {
			return "disables";  
		} else {
			//System.out.println("ここ通ってんの？");
			return null;
		}
		});
		} catch (Exception e) {
			e.printStackTrace();
			return null;   //null=データがない=書き込める
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String selectFinishTime(Employee employee) {
		try {
		String sql = "SELECT * FROM " + tableName + " WHERE employee_code = ? AND day = '" + LocalDate.now() + "';";
		
		//rs = stmt.executeQuery(sql);
//		Map<String, Object>map = null;
//		map = jdbcTemplate.queryForMap(sql);
//		LocalTime fTime = (LocalTime)map.get("finishTime"); 
		return jdbcTemplate.query(sql, new Object[] {new String(employee.getEmployee_code())},
				rs -> {
					if(rs.next() && rs.getString(4) == null) {   //🌟
						System.out.println("taikin");
						return "disablef";
					} else {
						return null;
					}					 
				});
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Autowired
	private WorkTime workTime;
	
	@Autowired
	private List<WorkTime> workTimeThisMonthList;
	
	//@SuppressWarnings("deprecation")
	@Override
	public List<WorkTime> selectWorkTimeThisMonthList(Employee employee, String thisMonth) {
		System.out.println(employee.getEmployee_code());
		System.out.println(thisMonth);
		String sql = "SELECT * FROM " + tableName + " WHERE employee_code = '" + employee.getEmployee_code()
				+ "' AND day LIKE '" + thisMonth + "%';";
		
		//RowMapper<WorkTime> rowMapper = new BeanPropertyRowMapper<WorkTime>(WorkTime.class);
		List<Map<String, Object>> getList = jdbcTemplate.queryForList(sql);
				for (Map<String, Object> map : getList){
					//while(rs.next()){
						
						DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						Object day = map.get("day");
						workTime.setWorkDate((LocalDate.parse((CharSequence) day, dtf1)));
						
						DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("HH:mm:ss");
						String sTime = map.get("startTime").toString();
						String fTime = map.get("finishTime").toString();
						if(sTime != null) {
							LocalTime startTime = LocalTime.parse(sTime, dtf2);
							workTime.setStartTime(startTime);
						}
						if(fTime != null) {
							LocalTime finishTime = LocalTime.parse(fTime, dtf2);
							workTime.setFinishTime(finishTime);
						}
						
						
						if(sTime != null && fTime != null) {  //両方値が入ってないと計算できないようにする
							//自動計算セットするメソッド
							workTime.calcWorkingHours();
						}
						workTimeThisMonthList.add(workTime);
					//}	
				};

		return workTimeThisMonthList;
	}
	
	@Override
    public int lastDay() {
    	LocalDateTime now = LocalDateTime.now();
    	//対象年
    	int year = now.getYear();
    	//対象月
    	int month = now.getMonthValue();

        //取得処理
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        //int result = 
        int result = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //標準出力
        //System.out.format("取得結果=%1$d", result);
        return result;
    }
	
//	@Autowired
//	private WorkTime worktime;
	
	@Override
	public WorkTime selectWorkTime(String ec, String thisDate) {
		//WorkTime worktime = null;
		//try {
			String sql = "SELECT * FROM " + tableName + " WHERE employee_code = ?"
					+ " AND day LIKE '%" + thisDate + "%';";
			Map<String, Object>map = jdbcTemplate.queryForMap(sql, ec);
			WorkTime worktime = new WorkTime();		
			worktime.setEmployee_code((String)map.get("employee_code"));
			worktime.setStartTime(LocalTime.parse(map.get("startTime").toString()));  //Stringに変換したものをさらにlocaltimeに変換
			worktime.setFinishTime(LocalTime.parse(map.get("finishTime").toString()));

//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
		return worktime;
	}
	
	public WorkTime updateWorkTime(WorkTime wdto, String day) {
		String sql = "UPDATE " + tableName + " SET startTime = '" + wdto.getStartTime()
		+ "', finishTime = '" + wdto.getFinishTime()
		+ "' WHERE employee_code = '" + wdto.getEmployee_code() + "' AND day LIKE '%" + day + "%';";

		jdbcTemplate.update(sql);
		return wdto;
	}
}
