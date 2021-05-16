package com.yc.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.yc.bean.UserBean;
import com.yc.dao.UserDao;
import com.yc.service.MailService;
import com.yc.service.ServiceException;
import com.yc.service.UserService;
import com.yc.util.CodeUtil;
import com.yc.util.Result;

@RestController
@RequestMapping("/user")
public class UserServlet {

	@Autowired
	private UserDao dao;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/reg")
	public Result reg(@Valid UserBean bean, Errors errors,String repwd,String code,HttpServletRequest request){
		System.out.println(bean);
		// 判断是否出现验证错误
		if (errors.hasFieldErrors("phone") || errors.hasFieldErrors("name") || errors.hasFieldErrors("account")
				|| errors.hasFieldErrors("pwd") || errors.hasFieldErrors("email")) {
			return Result.failure("字段验证错误！", errors.getAllErrors());
		}
		HttpSession s=request.getSession();
		String qCode=(String) s.getAttribute("qCode");
		System.out.println(qCode);
		System.out.println(code);
		if(qCode==null || !qCode.equalsIgnoreCase(code)){
			return Result.failure("验证码错误！", null);
		}
		try {
			int a=userService.register(bean,repwd);
			if(a==1){
				return Result.success("注册成功", null);
			}else{
				return Result.success("注册失败", null);
			}
		} catch (ServiceException e) {
			return Result.failure(e.getMessage(), null);
		}
	}
	
	@RequestMapping("/sendCode")
	public void sendMail(String email,HttpServletRequest request){
		String qCode=CodeUtil.genCode();
		HttpSession s=request.getSession();
		mailService.sendMail(email, "杰辰购物验证码", qCode);
		s.setAttribute("qCode", qCode);
	}
	
	@RequestMapping("/login")
	public String login(UserBean user,HttpServletRequest request){
		UserBean u;
		try {
			u = userService.login(user);
			if(u==null){
				return "用户名或密码错误";
			}else{
				HttpSession s=request.getSession();
				s.setAttribute("user", u);
				return "登录成功";
			}
		} catch (ServiceException e) {
			return e.getMessage();
		}
		
	}
	
	@RequestMapping("/checkLogin")
	public UserBean checkLogin(HttpServletRequest request){
		HttpSession s=request.getSession();
		UserBean u= (UserBean) s.getAttribute("user");
		System.out.println(u);
		return u;
	}
	
	@RequestMapping("/changePhone")
	public Result changePhone(String phone,@SessionAttribute UserBean user,HttpServletRequest request){
		dao.changePhone(phone,user.getId());
		logout(request);
		login(user, request);
		return Result.success("修改电话号码成功", null);
		
	}
	
	@RequestMapping("/changeEmail")
	public Result changeEmail(String email,@SessionAttribute UserBean user,HttpServletRequest request){
		dao.changeEmail(email,user.getId());
		logout(request);
		login(user, request);
		return Result.success("修改邮箱成功", null);
	}
	
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request){
		HttpSession s=request.getSession();
		s.removeAttribute("user");
	}

	@RequestMapping("/checkAccount")
	public Result checkAccount(@Valid UserBean bean, Errors errors){
		if ( errors.hasFieldErrors("account") || errors.hasFieldErrors("email")) {
			return Result.failure("字段验证错误！", errors.getAllErrors());
		}
		System.out.println(bean);
		try {
			UserBean a=dao.check(bean);
			if(a!=null){
				return Result.success("验证成功", null);
			}else{
				return Result.failure("账号或邮箱错误！！！", null);
			}
		} catch (Exception e) {
			return Result.failure(e.getMessage(), null);
		}
	}

	@RequestMapping("/findPwd")
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
	}

	@RequestMapping("/check")
	public Result check(String code,HttpServletRequest request){
		HttpSession s=request.getSession();
		String qCode=(String) s.getAttribute("qCode");
		System.out.println(qCode);
		System.out.println(code);
		if(qCode==null || !qCode.equalsIgnoreCase(code)){
			return Result.failure("验证码错误！", null);
		}else {
			return Result.success("验证成功",null);
		}
	}
	
	@PostMapping("upload")
	public Result upload(@RequestParam("headImgFile") MultipartFile headImgFile,
			@SessionAttribute UserBean user){
		// 1. 保存文件 （保存到磁盘上）
		String newfile = UUID.randomUUID().toString();//   1  时间戳， 2 随机数，  3，UUID
		String oldfile = headImgFile.getOriginalFilename();
		// 获取原文件的后缀名
		String suffix = oldfile.substring(oldfile.lastIndexOf("."));
		newfile += suffix;
		try {
			headImgFile.transferTo(new File("d:/jcshopping/upload_head", newfile));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			return Result.success("文件上传失败！", null);
		}
		// 2. 返回图片的web路径 
		String webpath = "upload_head/" + newfile;
		user.setHeadImg(webpath);
		userService.updateHeadImg(user);
		return Result.success("文件上传成功！", webpath);
	}
	
	@RequestMapping("queryAll")
	public List<UserBean> queryAll(){
		List<UserBean> list=dao.queryAll();
		return list;
	};
	
	@RequestMapping("delete")
	public int delete(int id){
		int a=dao.delete(id);
		return a;
	};
}
