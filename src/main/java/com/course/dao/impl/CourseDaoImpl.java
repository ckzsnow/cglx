package com.course.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.course.dao.ICourseDao;

@Repository("courseDao")
public class CourseDaoImpl implements ICourseDao {

	private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getRecommendCourse() {
		List<Map<String, Object>> retList1 = null;
		List<Map<String, Object>> retList2 = null;
		try {
			String sql = "select is_recommend, id, snapshot, course.cost as total, sum(course_2.time) as sub_time_total, "
					+ "course_2.*, sum(course_2.cost) as sub_total, "
					+ "ABS(course.cost-sum(course_2.cost)) as discount, "
					+ "count(course.parent_id) as sub_count from course LEFT JOIN "
					+ "(select course.parent_id,course.cost, course.time from course) "
					+ "as course_2 on course_2.parent_id=course.id where "
					+ "course.is_series=1 and course.is_recommend=1 group by parent_id limit 0,1";
			retList1 = jdbcTemplate.queryForList(sql);
			String sql2 = "select is_recommend, id, snapshot, course.cost as total, sum(course_2.time) as sub_time_total, "
					+ "course_2.*, sum(course_2.cost) as sub_total, "
					+ "ABS(course.cost-sum(course_2.cost)) as discount, "
					+ "count(course.parent_id) as sub_count from course LEFT JOIN "
					+ "(select course.parent_id,course.cost, course.time from course) "
					+ "as course_2 on course_2.parent_id=course.id where "
					+ "course.is_series=1 and course.is_recommend=2 group by parent_id limit 0,2";
			retList2 = jdbcTemplate.queryForList(sql2);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return ListUtils.union(retList1, retList2);
	}

	@Override
	public List<Map<String, Object>> getAllCourse() {
		List<Map<String, Object>> result = null;
		String sql = "select * from courses order by create_time desc";
		try {
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getCourseByTag(String tag) {
		List<Map<String, Object>> result = null;
		String sql = "select final_data.id, final_data.parent_id, final_data.cost, "
				+ "final_data.title, final_data.final_tea, final_data.final_position, "
				+ "final_data.snapshot from (select *,IFNULL(course_2.tea,course.teacher) "
				+ "as final_tea, IFNULL(course_2.tea_position, course.teacher_position) as final_position "
				+ "from course LEFT JOIN (select id as id1, teacher as tea, teacher_position as tea_position from course) as "
				+ "course_2 on course.parent_id=course_2.id1 where course.tag='"+tag+"') as final_data";
		logger.debug("getCourseByTag sql : {}", sql);
		try {
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getSeriesCourseCourseName() {
		List<Map<String, Object>> result = null;
		String sql = "select id, title from course where tag='系列课'";
		try {
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return result;
	}

	@Override
	public long addCourse(Map<String, String[]> paramsMap) {
		long courseId = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					String sql = "insert into course (parent_id, tag, is_series, title, banner, snapshot, abstract, "
							+ "cost, teacher, teacher_position, teacher_abstract, teacher_image, help, about, description, "
							+ "outline, info, crowds,  is_recommend, is_nav_recommend, video_src, playtime, create_time, time) "
							+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, Integer.valueOf(paramsMap.get("parent_id")==null?"0":paramsMap.get("parent_id")[0]));
					ps.setString(2, paramsMap.get("tag")==null?"系列课":paramsMap.get("tag")[0]);
					ps.setInt(3, Integer.valueOf(paramsMap.get("is_series")==null?"1":paramsMap.get("is_series")[0]));
					ps.setString(4, paramsMap.get("title")[0]);
					ps.setString(5, paramsMap.get("banner")==null?"":paramsMap.get("banner")[0]);
					ps.setString(6, paramsMap.get("snapshot")[0]);
					ps.setString(7, paramsMap.get("abstract")==null?"":paramsMap.get("abstract")[0]);
					ps.setString(8, paramsMap.get("cost")[0]);
					ps.setString(9, paramsMap.get("teacher")==null?"":paramsMap.get("teacher")[0]);
					ps.setString(10, paramsMap.get("teacher_position")==null?"":paramsMap.get("teacher_position")[0]);
					ps.setString(11, paramsMap.get("teacher_abstract")==null?"":paramsMap.get("teacher_abstract")[0]);
					ps.setString(12, paramsMap.get("teacher_image")==null?"":paramsMap.get("teacher_image")[0]);
					ps.setString(13, paramsMap.get("help")==null?"":paramsMap.get("help")[0]);
					ps.setString(14, paramsMap.get("about")==null?"":paramsMap.get("about")[0]);
					ps.setString(15, paramsMap.get("description")==null?"":paramsMap.get("description")[0]);
					ps.setString(16, paramsMap.get("outline")==null?"":paramsMap.get("outline")[0]);
					ps.setString(17, paramsMap.get("info")==null?"":paramsMap.get("info")[0]);
					ps.setString(18, paramsMap.get("crowds")==null?"":paramsMap.get("crowds")[0]);
					ps.setInt(19, Integer.valueOf(paramsMap.get("is_recommend")[0]));
					ps.setInt(20, Integer.valueOf(paramsMap.get("is_nav_recommend")[0]));
					ps.setString(21, paramsMap.get("video_src")==null?"":paramsMap.get("video_src")[0]);
					ps.setString(22, paramsMap.get("playtime")==null?"":paramsMap.get("playtime")[0]);
					ps.setTimestamp(23, new Timestamp(System.currentTimeMillis()));
					ps.setString(24, paramsMap.get("time")==null?"":paramsMap.get("time")[0]);
					return ps;
				}
			}, keyHolder);
			courseId = keyHolder.getKey().longValue();
		} catch(Exception e) {
			logger.error("addCourse error, exception : {}", e.toString());
		}
		return courseId;
	}

	@Override
	public List<Map<String, Object>> getCourseBriefBak() {
		List<Map<String, Object>> result = null;
		String sql = "select id, parent_id, title, is_series, cost, is_recommend, is_nav_recommend, DATE_FORMAT(create_time, '%Y-%m-%d') as readable_date from course";
		try {
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return result;
	}

	@Override
	public boolean deleteCourseById(Long course_id) {
		String sql = "delete course where id=" + course_id + " or parent_id=" + course_id;
		logger.debug("deleteCourse sql : {}", sql);
		try {
			int affectedRows = jdbcTemplate.update(sql);
			return affectedRows > 0;
		} catch(Exception e) {
			logger.error("deteleCourse exception : {}", e.toString());
		}
		return false;
	}
	
	@Override
	public Map<String, Object> getCourseById(Long course_id) {
		Map<String, Object> result = null;
		String sql = "select * from course where id=?";
		try {
			result = jdbcTemplate.queryForMap(sql, course_id);
		} catch(Exception e) {
			logger.error("getCourseById error : {}", e.toString());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getAllSeriesBrief() {
		List<Map<String, Object>> result = null;
		String sql = "select id, snapshot, course.cost as total, sum(course_2.time) as sub_time_total, "
				+ "course_2.*, sum(course_2.cost) as sub_total, "
				+ "ABS(course.cost-sum(course_2.cost)) as discount, "
				+ "count(course.parent_id) as sub_count from course LEFT JOIN "
				+ "(select course.parent_id,course.cost, course.time from course) "
				+ "as course_2 on course_2.parent_id=course.id where course.is_series=1 group by parent_id";
		try {
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("getAllSeriesBrief error : {}", e.toString());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getAllSubCourseBrief() {
		List<Map<String, Object>> result = null;
		String sql = "select final_data.id, final_data.parent_id, final_data.cost, final_data.tag, final_data.time, "
				+ "final_data.title, final_data.final_tea, final_data.final_position, "
				+ "final_data.snapshot from (select *,IFNULL(course_2.tea,course.teacher) "
				+ "as final_tea, IFNULL(course_2.tea_position, course.teacher_position) as final_position "
				+ "from course LEFT JOIN (select id as id1, teacher as tea, teacher_position as tea_position from course) as "
				+ "course_2 on course.parent_id=course_2.id1 where course.is_series!=1) as final_data";
		try {
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("getAllSubCourseBrief error : {}", e.toString());
		}
		return result;
	}

	@Override
	public Map<String, Object> getSeriesDetailById(Long id, String user_id) {
		Map<String, Object> result = null;
		String sql = "select * from (select course.*, course.cost as total, sum(course_2.tim) as sub_time_total, "
				+ "course_2.*, sum(course_2.cos) as sub_total, ABS(course.cost-sum(course_2.cos)) " 
				+ "as discount, count(course.parent_id) as sub_count from course LEFT JOIN (select " 
				+ "course.parent_id as pd,course.cost as cos, course.time as tim from course) as course_2 on " 
				+ "course_2.pd=course.id where course.id=? group by parent_id) as detail "
				+ "LEFT JOIN user_course on user_course.user_id=? and (user_course.course_id=detail.id "
				+ "or detail.parent_id=user_course.course_id) and user_course.pay_status=1";
		logger.debug("sql : {}", sql);
		try {
			result = jdbcTemplate.queryForMap(sql, id, user_id);
		} catch(Exception e) {
			logger.error("getSeriesDetailById error : {}", e.toString());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getSeriesSubCourseById(Long id) {
		List<Map<String, Object>> result = null;
		String sql = "select id, title, time, snapshot, cost, abstract from course where parent_id = " + id;
		logger.debug("getSeriesSubCourseById sql : {}", sql);
		try {
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("getSeriesSubCourseById error : {}", e.toString());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getNavRecommendCourse() {
		List<Map<String, Object>> result1 = null;
		List<Map<String, Object>> result2 = null;
		String sql1 = "select is_series, is_recommend, id, snapshot, course.cost as total, sum(course_2.time) as sub_time_total, "
				+ "course_2.*, sum(course_2.cost) as sub_total, "
				+ "ABS(course.cost-sum(course_2.cost)) as discount, "
				+ "count(course.parent_id) as sub_count from course LEFT JOIN "
				+ "(select course.parent_id,course.cost, course.time from course) "
				+ "as course_2 on course_2.parent_id=course.id where "
				+ "course.is_series=1 and course.is_nav_recommend=1 group by parent_id limit 0,2";
		String sql2 = "select final_data.is_series, final_data.id, final_data.parent_id, final_data.cost, final_data.tag, final_data.time, "
				+ "final_data.title, final_data.final_tea, final_data.final_position, "
				+ "final_data.snapshot from (select *,IFNULL(course_2.tea,course.teacher) "
				+ "as final_tea, IFNULL(course_2.tea_position, course.teacher_position) as final_position "
				+ "from course LEFT JOIN (select id as id1, teacher as tea, teacher_position as tea_position from course) as "
				+ "course_2 on course.parent_id=course_2.id1 where course.is_series!=1 and "
				+ "course.is_nav_recommend=1) as final_data limit 0,4";
		try {
			result1 = jdbcTemplate.queryForList(sql1);
			result2 = jdbcTemplate.queryForList(sql2);
		} catch(Exception e) {
			logger.error("getNavRecommendCourse error : {}", e.toString());
		}
		return ListUtils.union(result1, result2);
	}

	@Override
	public List<Map<String, Object>> getPaidCourse(String user_id) {
		List<Map<String, Object>> result = null;
		String sql = "select * from user_course right join (select final_data.id, "
				+ "final_data.parent_id, final_data.cost, final_data.tag, final_data.time, "
				+ "final_data.title, final_data.final_tea, final_data.final_position, "
				+ "final_data.snapshot from (select *,IFNULL(course_2.tea,course.teacher) "
				+ "as final_tea, IFNULL(course_2.tea_position, course.teacher_position) as "
				+ "final_position from course LEFT JOIN (select id as id1, teacher as tea, "
				+ "teacher_position as tea_position from course) as course_2 on "
				+ "course.parent_id=course_2.id1 where course.is_series!=1) as final_data) "
				+ "as course_detail on ((course_detail.id=user_course.course_id or "
				+ "user_course.course_id=course_detail.parent_id) and user_course.pay_status=1) "
				+ "where user_course.user_id='"+user_id+"'";
		try{
			result = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("getPaidCourse error : {}", e.toString());
		}
		return result;
	}

	@Override
	public boolean addUserCourse(String user_id, String course_id, String tradeNo) {
		String sql = "replace into user_course (user_id, course_id, trade_no, create_time) values (?, ?, ?, ?)";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, user_id, course_id, tradeNo, new Timestamp(System.currentTimeMillis()));
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public Map<String, Object> getCourseByTradeNo(String trade_no) {
		Map<String, Object> resultMap = null;
		String sql = "SELECT * FROM user_course LEFT JOIN course on user_course.course_id = course.id where user_course.trade_no=?";
		try {
			resultMap = jdbcTemplate.queryForMap(sql, trade_no);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return resultMap;
	}

	@Override
	public boolean updateUserCourseByTradeNo(String trade_no, int status) {
		String sql = "update user_course set pay_status=? where trade_no=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, status, trade_no);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public Map<String, Object> getSubcourseDetailById(int id, String user_id) {
		Map<String, Object> resultMap = null;
		String sql = "select * from (select *,IFNULL(course_2.tea,course.teacher) " 
				+ "as final_tea, IFNULL(course_2.tea_position, course.teacher_position) as final_position, "
				+ "IFNULL(course_2.tea_abstract, course.teacher_abstract) as final_abstract, " 
				+ "IFNULL(course_2.tea_image, course.teacher_image) as final_image, "
				+ "IFNULL(course_2.help_, course.help) as final_help, "
				+ "IFNULL(course_2.about_, course.about) as final_about "
				+ "from course LEFT JOIN (select id as id1, help as help_, about as about_, teacher as tea, teacher_position as tea_position, "
				+ "teacher_abstract as tea_abstract, teacher_image as tea_image from course) as " 
				+ "course_2 on course.parent_id=course_2.id1 where course.id=?) as final_data "
				+ "LEFT JOIN user_course on user_course.user_id=? and (user_course.course_id=final_data.id or final_data.parent_id=user_course.course_id) and user_course.pay_status=1";
		try {
			resultMap = jdbcTemplate.queryForMap(sql, id, user_id);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return resultMap;
	}
}
