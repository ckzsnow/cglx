package com.document.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.course.dao.impl.CourseDaoImpl;
import com.document.dao.IDocDao;

@Repository("docDao")
public class DocDaoImpl implements IDocDao{

	private static final Logger logger = LoggerFactory.getLogger(CourseDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@Override
	public boolean addDocument(Map<String, String[]> paramsMap) {
		String sql = "insert into document (title, author, snapshot, abstract, price, tag, "
				+ "document, description, create_time) values (?,?,?,?,?,?,?,?,?)";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, paramsMap.get("title")[0], paramsMap.get("author")[0], 
					paramsMap.get("snapshot")[0], paramsMap.get("abstract")[0], paramsMap.get("price")[0], 
					paramsMap.get("tag")[0], paramsMap.get("document")[0], paramsMap.get("description")[0], 
					new Timestamp(System.currentTimeMillis()));
		} catch(Exception e) {
			logger.error("addDocument error : {}", e.toString());
		}
		return affectedRows!=0;
	}
	
	@Override
	public boolean updateDocument(Map<String, String[]> paramsMap) {
		String sql = "update document set title=?, author=?, snapshot=?, abstract=?, price=?, tag=?, document=?, description=? where id=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, paramsMap.get("title")[0], paramsMap.get("author")[0], 
					paramsMap.get("snapshot")[0], paramsMap.get("abstract")[0], paramsMap.get("price")[0], 
					paramsMap.get("tag")[0], paramsMap.get("document")[0], paramsMap.get("description")[0], 
					paramsMap.get("id")[0]);
		} catch(Exception e) {
			logger.error("updateDocument error : {}", e.toString());
		}
		return affectedRows!=0;
	}

	@Override
	public List<Map<String, Object>> getAllDocument() {
		String sql = "select *, DATE_FORMAT(create_time,'%Y-%m-%d') as readable_date from document order by create_time desc";
		List<Map<String, Object>> retList = null;
		try {
			retList = jdbcTemplate.queryForList(sql);
		} catch(Exception e) {
			logger.error("getAllDocument error : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean deleteDocumentById(String id) {
		String sql = "delete from document where id=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, id);
		} catch(Exception e) {
			logger.error("deleteDocumentById error : {}", e.toString());
		}
		return affectedRows!=0;
	}

	@Override
	public Map<String, Object> getDocDetailById(String id) {
		String sql = "select * from document where id=?";
		Map<String, Object> retMap = null;
		try {
			retMap = jdbcTemplate.queryForMap(sql, id);
		} catch(Exception e) {
			logger.error("getDocDetailById error : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getDocByIndexAndKey(int start, int length, String key) {
		List<Map<String, Object>> retList = null;
		String sql = "select *, DATE_FORMAT(create_time,'%Y-%m-%d') as readable_date from document where title like '%" + key +"%' order by create_time desc limit ?,?";
		try{
			retList = jdbcTemplate.queryForList(sql, start, length);
		}catch(Exception e){
			logger.error("getDocByIndexAndKey error : {}", e.toString());
		}
		return retList;
	}

	@Override
	public List<Map<String, Object>> getAllDocumentByKey(String key) {
		List<Map<String, Object>> retList = new ArrayList<>();
		String sql = "select *, DATE_FORMAT(create_time,'%Y-%m-%d') as readable_date from document where document like '%"+key+"%'";
		try{
			retList = jdbcTemplate.queryForList(sql);
		}catch(Exception e){
			logger.error("getAllDocumentByKey error : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean updateUserDocById(String id) {
		String sql = "update user_document set pay_status=1 where id=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, id);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public boolean addUserDocAndPayStatus(String srcUnionId, String doc_id, String tradeNo) {
		String sql = "replace into user_document (user_id, doc_id, pay_status, trade_no, create_time) values ((select id from user where open_id='"+srcUnionId+"'),?,?,?,?)";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, doc_id, 1, tradeNo, new Timestamp(System.currentTimeMillis()));
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public Map<String, Object> getUserDocByUserIdAndDocId(String user_id, String doc_id) {
		String sql = "select * from user_document where user_id=? and doc_id=?";
		Map<String, Object> retMap = null;
		try {
			retMap = jdbcTemplate.queryForMap(sql, user_id, doc_id);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return retMap;
	}

	@Override
	public boolean addUserDoc(String userId, String doc_id, String fee, String orderId) {
		String sql = "replace into user_document (user_id, doc_id, paid, pay_status, trade_no, create_time) values (?,?,?,?,?,?)";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, userId, doc_id, fee, 0, orderId, new Timestamp(System.currentTimeMillis()));
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public boolean updateUserDocByTradeNo(String outTradeNo, int status) {
		String sql = "update user_document set pay_status=? where trade_no=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, status, outTradeNo);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public int getPayStatusByOrderIdAndCourseId(String doc_id, String orderId) {
		int result = 0;
		String sql = "select pay_status from user_document where trade_no=? and doc_id=?";
		try{
			result = jdbcTemplate.queryForObject(sql, new String[] {orderId, doc_id}, Integer.class);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		return result;
	}

}
