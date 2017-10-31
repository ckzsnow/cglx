package com.ocfisher.dao.impl;

import java.sql.Timestamp;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ocfisher.dao.ICourseInviteCardUnionidDao;

@Repository("courseInviteCardUnionidDao")
public class CourseInviteCardUnionidDaoImpl implements ICourseInviteCardUnionidDao {

	private static final Logger logger = LoggerFactory
			.getLogger(CourseInviteCardUnionidDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> getInfoByUnionIdAndCourseId(String unionid, long courseid) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from course_invite_card_unionid where unionid=? and course_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{unionid, courseid});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public int getCountByUnionIdAndCourseId(String unionid, long courseid) {
		int count = 0;
		try {
			String sql = "select * from course_invite_card_unionid where unionid=? and course_id=?";
			count = jdbcTemplate.queryForObject(sql, new Object[]{unionid, courseid}, Integer.class);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return count;
	}

	@Override
	public boolean addInfo(String unionId, long courseid, int isSeries) {
		try{
			String sql= "replace into course_invite_card_unionid(unionid, course_id, is_series, create_time) values (?,?,?,?)";
			int num = jdbcTemplate.update(sql, 
					unionId, courseid, isSeries, new Timestamp(System.currentTimeMillis()));
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	
}