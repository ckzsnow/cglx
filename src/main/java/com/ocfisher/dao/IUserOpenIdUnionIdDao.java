package com.ocfisher.dao;

import java.util.List;
import java.util.Map;

public interface IUserOpenIdUnionIdDao {
	
	public Map<String, Object> getUserByOpenIdAndUnionId(String openId, String unionId);
	
	public List<Map<String, Object>> getUserByUnionId(String unionId);
	
	public Map<String, Object> getUserByOpenId(String openId);
	
	public boolean addUser(String openId, String unionId);
	
}
