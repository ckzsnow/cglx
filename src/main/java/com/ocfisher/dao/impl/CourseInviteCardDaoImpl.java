package com.ocfisher.dao.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ocfisher.dao.ICourseInviteCardDao;

@Repository("courseInviteCardDao")
public class CourseInviteCardDaoImpl implements ICourseInviteCardDao {

	private static final Logger logger = LoggerFactory
			.getLogger(CourseInviteCardDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getAllCourse() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from course_invite_card";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean addCourseCard(long courseId, int isSeries, String templateName, int needInvitePersonCount) {
		try{
			String sql= "replace into course_invite_card(course_id, is_series, template_name, need_invite_person_count, create_time) values (?,?,?,?,?)";
			int num = jdbcTemplate.update(sql, 
					courseId, isSeries, templateName, needInvitePersonCount, new Timestamp(System.currentTimeMillis()));
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public boolean addCourseInviteRecord(String srcOpenId, String friendOpenId, long courseId) {
		try{
			String sql= "insert into user_course_invite_record(src_open_id, friend_open_id, course_id, create_time) values (?,?,?,?)";
			int num = jdbcTemplate.update(sql, 
					srcOpenId, friendOpenId, courseId, new Timestamp(System.currentTimeMillis()));
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public Map<String, Object> getCourseInviteRecord(String srcOpenId, String friendOpenId, long courseId) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from user_course_invite_record where src_open_id=? and friend_open_id=? and course_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{srcOpenId, friendOpenId, courseId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public int getCourseInviteSupportTotal(String srcOpenId, long courseId) {
		int supportTotal = 0;
		try {
			String sql = "select count(*) from user_course_invite_record where src_open_id=? and course_id=?";
			supportTotal = jdbcTemplate.queryForObject(sql, new Object[]{srcOpenId, courseId}, Integer.class);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return supportTotal;
	}

	@Override
	public Map<String, Object> getCourseById(long courseId) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from course_invite_card where course_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{courseId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public Map<String, Object> getUserAndUserCourseByUserOpenId(String openId, long courseId) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select u.id as user_id, uc.id as user_course_id from user as u LEFT JOIN user_course as uc on u.open_id=? and u.id=uc.user_id and uc.course_id=? where u.open_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{openId, courseId, openId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public boolean addCourseActivityExpireRecord(String openId, long courseId) {
		try{
			String sql= "insert into user_course_invite_expire(src_open_id, course_id, create_time) values (?,?,?)";
			int num = jdbcTemplate.update(sql, 
					openId, courseId, new Timestamp(System.currentTimeMillis()));
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public int getCourseActivityExpireRecordByOpenIdAndCourseId(String openId, long courseId) {
		int count = 0;
		try {
			String sql = "select count(*) from user_course_invite_expire where src_open_id=? and course_id=?";
			count = jdbcTemplate.queryForObject(sql, new Object[]{openId, courseId}, Integer.class);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return count;
	}

	@Override
	public boolean delCourseCard(long courseId) {
		String sql = "delete from course_invite_card where course_id=" + String.valueOf(courseId);
		logger.debug("delete course_invite_card sql : {}", sql);
		try {
			int affectedRows = jdbcTemplate.update(sql);
			return affectedRows > 0;
		} catch(Exception e) {
			logger.error("delete course_invite_card exception : {}", e.toString());
		}
		return false;
	}	
}