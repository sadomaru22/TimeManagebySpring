package com.example.demo.entity;

import java.io.Serializable;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data

@Table("employee")
@Configuration
public class Employee implements Serializable {
	
	  private String employee_code;
	  private String name;
	  private String post;
	  private String password;
	  
		public Employee() {}
		
		public Employee(String employee_code, String name, String post, String password) {
			this.employee_code = employee_code;
			this.name = name;
			this.post = post;
			this.password = password;
		}
		
//		//★コンストラクタは引数が違っていれば同じ名前でも複数作れる（↓従業員ログイン用）
//		public Employee(String employee_code, String password) {
//			this.emoployee_code = employee_code;
//			this.password = password;
//		}
//		
//		//管理者側で一覧表示するときに使う
//		public Employee(String employee_code, String name, String post) {
//			this.emoployee_code = employee_code;
//			this.name = name;
//			this.post = post;
//		} 
}
