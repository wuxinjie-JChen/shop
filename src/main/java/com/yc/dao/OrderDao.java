package com.yc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yc.bean.OrderBean;
import com.yc.bean.OrderItem;

public interface OrderDao {

	@Insert("insert into `order` values(#{id},#{uid},#{createTime},#{money},'111',0)")
	void add(OrderBean order);
	
	@Select("select * from `order` where id=#{id} and is_deleted=0")
	@Results(@Result(column="uid",property="user",one=@One(select="com.yc.dao.UserDao.findById")))
	OrderBean selectById(Long id);

	@Select("select * from `order` where uid=#{uid} and is_deleted=0")
	List<OrderBean> selectByUid(int id);

	@Update("update `order` set state=#{state} where id=#{id}")
	void update(OrderBean order);
	
	@Select("<script>" +
			" select * from `order`" +
			" <where> is_deleted =0" +
			" <if test='uid != null'> and uid=#{uid}" +
			" </if>" +
			" <if test='id != null'> and id=#{id}" +
			" </if>" +
			" <if test='state != null'> and state=#{state}" +
			" </if>" +
			" </where>" +
			" </script> ")
	@Results(id="rmorder",value={@Result(column="uid",property="user",
			one=@One(select="com.yc.dao.UserDao.findById"))})
	List<OrderBean> selectByParams(OrderBean bean);
	
	@Update("update `order` set is_deleted=1 where id=#{id}")
	void delete(OrderBean bean);
}
