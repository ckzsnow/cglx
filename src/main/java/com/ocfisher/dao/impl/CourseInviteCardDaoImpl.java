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
	public boolean addCourseCard(long courseId, String templateName) {
		try{
			String sql= "replcae into course_invite_card(courseId, template_name, create_time) values (?,?,?)";
			int num = jdbcTemplate.update(sql, 
					courseId, templateName, new Timestamp(System.currentTimeMillis()));
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
}