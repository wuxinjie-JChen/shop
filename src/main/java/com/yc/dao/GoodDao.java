package com.yc.dao;

import java.util.List;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yc.bean.GoodBean;

public interface GoodDao {

	@Select("select * from good where is_deleted=0 order by num desc")
	@Results(@Result(column="classId",property="category",
			one=@One(select="com.yc.dao.CategoryDao.findById")))
	public List<GoodBean> find();
	
	@Select("select * from good where id=#{id}")
	@Results(@Result(column="classId",property="category",
			one=@One(select="com.yc.dao.CategoryDao.findById")))
	public GoodBean findById(GoodBean bean);

	@Select("select * from good where name like concat('%',#{name},'%')")
	@Results(@Result(column="classId",property="category",
		one=@One(select="com.yc.dao.CategoryDao.findById")))
	public List<GoodBean> findByName(String name);
	
	@Update("update good set num=num+#{num} where id=#{id}")
	public void update(@Param("num")int num,@Param("id")int id);
	
	@Update("update good set is_deleted=1 where id=#{id}")
	public int delete(int id);
	
	@Select("<script>" +
			" select * from good" +
			" <where>" +
			" <if test='classId != null'> and classId=#{classId}" +
			" </if>" +
			" </where>" +
			" </script> ")
	@Results(@Result(column="classId",property="category",
		one=@One(select="com.yc.dao.CategoryDao.findById")))
	public List<GoodBean> findByClassId(int classId);
}
