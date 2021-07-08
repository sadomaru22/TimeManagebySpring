package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Employee;
import com.example.demo.entity.User;
import com.example.demo.entity.WorkTime;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.UserDaoService;
import com.example.demo.service.WorkTimeService;

@Controller
@ComponentScan
@RequestMapping("admin")
public class AdminController {
	  @Autowired
	  HttpSession session; 
	  
	  @Autowired
	  private UserDaoService userService;
	  
	  @GetMapping("login")
	  public String login() {
		  return "Admin/login";
	  }
	  
	  @PostMapping("logout")
	  public String logout() {
		  session.removeAttribute("userId");
		  
		  return "Admin/logout";
	  }
	  
	  @PostMapping("menu")
	  public String menu(Model model, 
			  @RequestParam("userId")String userId, @RequestParam("password")String password) {
		  
		  User returnUser = userService.loginUser(userId);
	        if (returnUser == null) {  
	        	model.addAttribute("message", "失敗しました。IDまたはパスワードが違います。");
	        	 return "Admin/login";
	        } else {
	        	
	        	String correctId = returnUser.getUserId();
	        	String correctPass = returnUser.getPassword();
	        	
	        	if (userId.equals(correctId) && password.equals(correctPass)) {
	        		User udto = new User();
	        		udto.setUserId(correctId);
	        		session.setAttribute("userId", udto);
	        		return "Admin/menu";
	        	} else {
	 			   model.addAttribute("message", "従業員コードまたはパスワードが違います。");
				   System.out.println("IDまたはパスワードが間違っています②");
				   return "Admin/login";
	        	}
	        }   	
	  }
	  
	  @GetMapping("menuBack")
	  public String menuBack() {
		  return "Admin/menu";
	  }
	  
	  @GetMapping("regist_employee")
	  public String registEmployee(@ModelAttribute("employee") Employee employee) {
		  return "Admin/regist_employee";
	  }
	  @Autowired
	  private EmployeeService empService;
	  @PostMapping("admin_regist")
	  public String register (Model model, @RequestParam("employee_code")String ec
			  , @RequestParam("name")String name, @RequestParam("post")String post, @RequestParam("password")String password) {
		  Employee edt = new Employee();
		  edt.setEmployee_code(ec);
		  edt.setName(name);
		  edt.setPost(post);
		  edt.setPassword(password);
		  
		  int any = empService.insertEmployee(edt);
		  
		  return "Admin/completion";
	  }
	  
	  @GetMapping("display_employee_list")
	  public String displayEmplist(Model model) {
		  List<Employee> viewList = empService.selectEmployeeAll();
		  model.addAttribute("viewList", viewList);
		  return "Admin/show_all_employee";
	  }
	  
	  @Autowired
	  private WorkTimeService wservice;
	  
	  @PostMapping("checkEditDelete")
	  public String CheckEditDelete(Model model, @RequestParam("employee_code")String ec, @RequestParam("submit")String submit) {
		  Employee edt = new Employee();
		  edt.setEmployee_code(ec);
		  session.setAttribute("employee_code", ec);
		  
			if (submit.equals("従業員を編集する")) {
				Employee empEdit = empService.selectEmployeeSt(ec);
				
				model.addAttribute("employee", empEdit);
				
				return "Admin/edit_employee";   //指定された従業員コードの従業員情報をとるため、必要
			
				
			} else if (submit.equals("労働時間の修正")) {
				LocalDateTime now = LocalDateTime.now();
				model.addAttribute("year", now.getYear());
				
				int month = now.getMonthValue();
				if (month < 10) {
					String monthSt =  String.valueOf(month);
					model.addAttribute("month", "0" + monthSt);
				} else {
					model.addAttribute("month", month);
				}
				
				ArrayList<String> arw = new ArrayList<String>();
				int result = wservice.lastDay();
					for (int d = 1; d <= result; d++) {
						if (d < 10) {
							arw.add("0" + String.valueOf(d));
						} else {
							arw.add(String.valueOf(d));
						}
					} 
				model.addAttribute("arw", arw);	
				
				return "Admin/modify_radio_worktime";

				
			}  else if (submit.equals("従業員を削除する")) {
				Employee empEdit = empService.selectEmployeeSt(ec);
				model.addAttribute("name", empEdit.getName());
				return "Admin/delete_employee";

			} else {
				return "Admin/show_all_employee";

			}
	  }
	  
	  @PostMapping("editEmployee")
	  public String editEmployee(Model model, @RequestParam("employee_code")String ec
			  , @RequestParam("name")String name, @RequestParam("post")String post, @RequestParam("password")String password) {
		  Employee edt = new Employee();
		  edt.setEmployee_code(ec);
		  edt.setName(name);
		  edt.setPost(post);
		  edt.setPassword(password);
		  
		  empService.updateEmployee(edt);
		  return "Admin/completion";
	  }
	  
	  @GetMapping("delete_employee")
	  public String deleteEmployee(Model model) {
		  String ec = (String)session.getAttribute("employee_code");
		  
		  empService.deleteEmployee(ec);
		  
		  model.addAttribute("ec", ec);
		  
		  return "Admin/delete_completion";
	  }
	  
	  @PostMapping("modify_check_worktime")
	  public String modifyCheckWorktime(Model model, @RequestParam("thisDate")String thisDate) {
		  String ec = (String)session.getAttribute("employee_code");
		  
		LocalDateTime now = LocalDateTime.now();
		int mon = now.getMonthValue();
		String month = String.valueOf(mon);
		
		String thisMonthDate = month + "-" + thisDate;
		model.addAttribute("thisMonthDate", thisMonthDate);
		
		WorkTime wt = wservice.selectWorkTime(ec, thisMonthDate);
		model.addAttribute("wt", wt);
		  return "Admin/modify_show_worktime";
	  }
	  
	  @PostMapping("modify_comp_worktime")
	  public String modifyCompWorktime(Model model, @RequestParam("day")String day, @RequestParam("startTime")String startTime, 
			  @RequestParam("finishTime")String finishTime, @RequestParam("employee_code")String employeeCode) {
			//String型からLocalTime型にキャスト(WorkTimeDTOに入れるため)
			LocalTime st = LocalTime.parse(startTime);
			LocalTime ft = LocalTime.parse(finishTime);
			
			WorkTime wdto = new WorkTime();
			wdto.setEmployee_code(employeeCode);
			wdto.setStartTime(st);
			wdto.setFinishTime(ft);
			
			wservice.updateWorkTime(wdto, day);
			
			model.addAttribute("employeeCode", employeeCode);
		  
		  return "Admin/modify_completion";
	  }
	  
	  @GetMapping("regist_adminuser")
	  public String registAdminUser(@ModelAttribute("employee") Employee employee) {
		  return "Admin/regist_adminuser";
	  }
	  
	  @PostMapping("RegistAdminuser")
	  public String RegistAdminuser(Model model, @RequestParam("userId")String userId, 
			  @RequestParam("password")String password) {
		  userService.insertUser(userId, password);
		  
		  //model.addAttribute("loginUserId", userId);
		  return "Admin/completion";
	  }
}
