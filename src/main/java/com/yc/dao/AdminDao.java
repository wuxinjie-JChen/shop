package com.yc.dao;

import org.apache.ibatis.annotations.Select;

import com.yc.bean.AdminBean;


public interface AdminDao {
	
	@Select("select * from admin where account=#{account} and pwd=#{pwd}")
	public AdminBean find(AdminBean admin);

}
