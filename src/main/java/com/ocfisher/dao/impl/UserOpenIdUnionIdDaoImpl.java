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
import com.ocfisher.dao.IUserOpenIdUnionIdDao;

@Repository("userOpenIdUnionIdDao")
public class UserOpenIdUnionIdDaoImpl implements IUserOpenIdUnionIdDao {

	private static final Logger logger = LoggerFactory
			.getLogger(UserOpenIdUnionIdDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> getUserByOpenIdAndUnionId(String openId, String unionId) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from user_openid_unionid where open_id=? and union_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{openId, unionId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getUserByUnionId(String unionId) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from user_openid_unionid where union_id=?";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public Map<String, Object> getUserByOpenId(String openId) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from user_openid_unionid where open_id?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{openId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public boolean addUser(String openId, String unionId) {
		try{
			String sql= "replace into user_openid_unionid(open_id, union_id, create_time) values (?,?,?)";
			int num = jdbcTemplate.update(sql, 
					openId, unionId, new Timestamp(System.currentTimeMillis()));
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	
}