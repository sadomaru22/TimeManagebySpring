package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Employee;
import com.example.demo.entity.WorkTime;
import com.example.demo.service.AttendanceEmpService;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.WorkTimeService;

@Controller
@ComponentScan
@RequestMapping("emp")
public class MainController {
	  @Autowired
	  HttpSession session; 
	  
	  @Autowired
	  private AttendanceEmpService service;

	 //初期表示
	  @GetMapping
	  public String index() {   //
		  //✨SendIndexの機能もここで書いておく
			session.removeAttribute("userId");
		    session.removeAttribute("employeeCode");
			session.removeAttribute("message");
		  return "Emp/index";
	  }
	  
	  @GetMapping("login")
	  public String login() {
		  return "Emp/attendance_login";
	  }
	  
	  @PostMapping("logout")
	  public String logout() {
		  session.removeAttribute("employeeCode");
		  
		  return "Emp/logout";
	  }
	  
	  @PostMapping("menu")
	  public String save(Model model, Employee employee, @RequestParam("employee_code")String employee_code, @RequestParam("password")String password, BindingResult result) {
//		    if (result.hasErrors()) {  //エラーの有無確認
//		    	System.out.println("IDまたはパスワードが間違っています");
//		      return "Emp/attendance_login";
//		    }
		  
		  //codeが間違っている場合
		  Employee returnEc = service.loginEmployee(employee_code);
	        if (returnEc == null) {  
	        	model.addAttribute("message", "失敗しました。IDまたはパスワードが違います。");
	        	 return "Emp/attendance_login";
	        } else {
		  
		   String correctCode = returnEc.getEmployee_code();
		   String correctPass = returnEc.getPassword();   
		   
		   if(employee_code.equals(correctCode) && password.equals(correctPass)) {
			   Employee edto = new Employee();
			   edto.setEmployee_code(correctCode);
			   session.setAttribute("employeeCode", edto);
			   return "Emp/attendance_menu";
		   } else {
			   model.addAttribute("message", "従業員コードまたはパスワードが違います。");
			   System.out.println("IDまたはパスワードが間違っています②");
			   return "Emp/attendance_login";
		   }
	    }   
	  }
	  
		//現在日時の表示
		LocalDateTime now = LocalDateTime.now();
	  @Autowired
	  private WorkTimeService wservice;
	  @GetMapping("dakoku")
	  public String dakoku(Model model) {
		  Employee employeeCode = (Employee)session.getAttribute("employeeCode");   //ちゃんと渡ってきてた
		  
			String startCheck = wservice.selectStartTime(employeeCode);
			model.addAttribute("startWork", startCheck);  //setAttrではだめぽい
			System.out.println(startCheck + "=dakokusChk");
		
			String nowFormat = now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
			model.addAttribute("now", nowFormat);
			
			//出勤ボタンが押されてたら退勤押せる
			if(startCheck != null) {
				String finishCheck = wservice.selectFinishTime(employeeCode);
				model.addAttribute("finishWork", finishCheck);
				System.out.println(finishCheck + "=dakokufinChk");
			}
		  
		  return "Emp/attendance_timecard";
	  }
	  
	  
	  @Autowired
	  private AttendanceEmpService attendEmpDao;
	  @PostMapping("timecard")
	  public String timecard(Model model, @RequestParam("attendance")String attendance) {
			String message2 = null;
			
			Employee employeeCode = (Employee) session.getAttribute("employeeCode");	

			boolean Flag = false;
			try {
				if (attendance.equals("出勤処理")) {
					message2 = "出勤しました。";
					Flag = attendEmpDao.setStartTime(employeeCode);  //はいー発見
					System.out.println("Flag:出勤処理");
					
//				} else if (attendance.equals("退勤処理")) {
				} else if (attendance.equals("退勤処理")) {
					System.out.println("attendance = " + attendance);
					message2 = "退勤しました。";
					Flag = attendEmpDao.setFinishTime(employeeCode);
					System.out.println("Flag:大金処理");
				} else {}
				
			} catch (Exception e) {
				e.printStackTrace();
				return "Emp/error_message";
			}
			
			//ViewTimecard.servと同じ処理をまず行う(null or "disabled"のチェック)
			
				String startCheck = wservice.selectStartTime(employeeCode);
				model.addAttribute("startWork", startCheck);
				System.out.println(startCheck + "= startCheck");
				
				String nowFormat = now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
				model.addAttribute("now", nowFormat);
				
				//if(startCheck != null) {   //出勤してるはずだからnullな訳ない。だからいらない
					String finishCheck = wservice.selectFinishTime(employeeCode);
					model.addAttribute("finishWork", finishCheck);
					System.out.println(finishCheck + "= finishCheck");
				//}

			if (Flag) {
				model.addAttribute("message2", message2); 
				model.addAttribute("attendance", attendance);  
				return "Emp/attendance_timecard";  
			} else {   //こんな場合ある？
				model.addAttribute("message2", message2);
				//session.setAttribute("attendance", attendance);
				return "Emp/attendance_timecard";  
			}
	  }
	  
	  @GetMapping("select_timesheet")
	  public String selctTimesheet(Model model) {
		  	//session.getAttribute("employeeCode");
		  	
			LocalDateTime now = LocalDateTime.now();
			int year = now.getYear();
			int lastyear = year - 1;
			int month = now.getMonthValue();
			
			String yearSt = String.valueOf(year);
			String lastyearSt = String.valueOf(lastyear);
			
			ArrayList<String> yst = new ArrayList<String>();
			for (int i = month; i > 0; i--) {
				if (i < 10) {
					yst.add(yearSt + "-0" + String.valueOf(i));
				} else {
					yst.add(yearSt + "-" + String.valueOf(i));
				}
			}
			ArrayList<String> ylst = new ArrayList<String>();
			for (int i = 12; i > month; i--) { //昨年
				if (i < 10) {
					ylst.add(lastyearSt + "-0" + String.valueOf(i));
				} else {
					ylst.add(lastyearSt + "-" + String.valueOf(i));
				}
			}
			model.addAttribute("year", yst);
			model.addAttribute("lastYear", ylst);
			
		  return "Emp/attendance_select_timesheet";
	  }
	  
	  @Autowired
	  private EmployeeService empService;
	  @Autowired
	  private WorkTime workTime;
	  @PostMapping("view_timesheet")
	  public String viewTimesheet(Model model,  @RequestParam("thisMonth")String thisMonth, HttpServletRequest request, HttpServletResponse response) {
		Employee employeeCode = (Employee) session.getAttribute("employeeCode");
		Calendar thisMonthCalendar = Calendar.getInstance();
		thisMonthCalendar.set(Calendar.YEAR, Integer.parseInt(thisMonth.substring(0, 4)));
		thisMonthCalendar.set(Calendar.MONTH, Integer.parseInt(thisMonth.substring(5)));
		
		//try {
		List<WorkTime>workTimeThisMonthList = 
				wservice.selectWorkTimeThisMonthList(employeeCode, thisMonth);
		model.addAttribute("workTimeThisMonthList", workTimeThisMonthList);
		
		//月次報告書の上の名前表示用
		Employee employee = empService.selectEmployee(employeeCode);
		String emplName = employee.getName();
		model.addAttribute("emplName", emplName);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		model.addAttribute("thisYear", thisMonthCalendar.get(Calendar.YEAR));
		model.addAttribute("thisMonth", thisMonthCalendar.get(Calendar.MONTH));
		thisMonthCalendar.add(Calendar.MONTH, -1);
		model.addAttribute("dayOfMonth", thisMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		thisMonthCalendar.add(Calendar.MONTH, 1);
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH時mm分");
		DateTimeFormatter dd  = DateTimeFormatter.ofPattern("dd");
		
		model.addAttribute("timeFormat", timeFormat);
		model.addAttribute("dd", dd);
		
		//DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("HH時間mm分");
		
		int workDate =
				Integer.parseInt(workTime.getWorkDate().format(DateTimeFormatter.ofPattern("dd"))); //出勤日
		model.addAttribute("workDate", workDate);
//		String sTime = workTime.getStartTime().format(timeFormat);
//		String fTime = workTime.getFinishTime().format(timeFormat);
//		int wHour = workTime.getWorkingHours();
				
//			model.addAttribute("sTime", sTime);			
//			model.addAttribute("fTime", fTime);			

//		if (wHour != 0) {
//		 model.addAttribute("wHour", workTime.getWorkingHours());
//		 //model.addAttribute("wMins", workTime.getWorkingMins());
//		}
		
		 Boolean chkDateFlag = false;
		 model.addAttribute("chkDateFlag", chkDateFlag);
		 
		
		//実動時間合計の計算
		WorkTime workTimeList = workTimeThisMonthList.get(0);
		int wtHour = workTimeList.getWorkingHours() * 60;
		int wtMins = workTimeList.getWorkingMins();    
		int wtPlus = wtHour + wtMins;
		
		int sum = 0;
		int result = wservice.lastDay();
        for (int i = 1; i <= result; i++) {
        	sum = sum + wtPlus;
        }
        
        sum = sum / 60;
        model.addAttribute("sum", sum);
		//session.setAttribute("thisMonth", thisMonthCalendar);
		
		return "Emp/attendance_view_timesheet";
	  }
}
