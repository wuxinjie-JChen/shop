package com.yc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yc.bean.AdminBean;
import com.yc.dao.AdminDao;

@RestController
@RequestMapping("/admin")
public class AdminServlet {

	@Autowired
	private AdminDao dao;
	
	@RequestMapping("/login")
	public String login(AdminBean admin,HttpServletRequest request){
		AdminBean a;
		try {
			a = dao.find(admin);
			if(a==null){
				return "用户名或密码错误";
			}else{
				HttpSession s=request.getSession();
				s.setAttribute("admin", a);
				return "登录成功";
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		
	}
	
	@RequestMapping("/checkLogin")
	public AdminBean checkLogin(HttpServletRequest request){
		HttpSession s=request.getSession();
		AdminBean a= (AdminBean) s.getAttribute("admin");
		System.out.println(a);
		return a;
	}
	
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request){
		HttpSession s=request.getSession();
		s.removeAttribute("admin");
	}


	/*@RequestMapping("/findPwd")
	public Result findPwd(UserBean bean, String repwd,HttpServletRequest request){
		System.out.println(bean);
		
		UserBean u=bean;
		if(bean.getAccount()==null){
			HttpSession s=request.getSession();
			UserBean user= (UserBean) s.getAttribute("user");
			u.setAccount(user.getAccount());
		}
		if(!bean.getPwd().equals(repwd)){
			return Result.failure("输入两次密码不一致！", null);
		}
		try {
			int a=dao.updatePwd(u);
			System.out.println("a"+a);
			if(a == 1){
				return Result.success("修改密码成功", null);
			}else{
				return Result.success("修改密码失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.failure(e.getMessage(), null);
		}
	}*/
}
