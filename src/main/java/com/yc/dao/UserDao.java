package com.yc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.yc.bean.UserBean;


public interface UserDao {

	@Insert("insert into user values(null,#{account},#{name},#{pwd},#{email},#{phone},null)")
	public int add(UserBean user);
	
	@Select("select * from user where account=#{account} and pwd=#{pwd} and is_deleted=0")
	public UserBean find(UserBean user);

	@Select("select * from user where account=#{account} and is_deleted=0")
	public UserBean findByAccount(UserBean user);
	
	@Select("select * from user where id=#{id} and is_deleted=0")
	public UserBean findById(int id);
	
	@Update("update user set addr=#{addr} where id=#{id}")
	public void updateAddr(@Param("id") int id,@Param("addr") String addr);

	@Select("select * from user where account=#{account} and email=#{email} and is_deleted=0")
	public UserBean check(UserBean bean);

	@Update("update user set pwd=#{pwd} where account=#{account}")
	public int updatePwd(UserBean bean);

	@Update("update user set headImg=#{headImg} where id=#{id}")
	public void updateHeadImg(UserBean loginedUser);

	@Update("update user set phone=#{phone} where id=#{id}")
	public void changePhone(@Param("phone") String phone, @Param("id") int id);

	@Update("update user set email=#{email} where id=#{id}")
	public void changeEmail(@Param("email") String email, @Param("id") int id);

	@Select("select * from user where is_deleted=0")
	public List<UserBean> queryAll();

	@Update("update user set is_deleted=1 where id=#{id}")
	public int delete(int id);
}
