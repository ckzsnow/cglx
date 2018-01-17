package com.document.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.document.dao.IDocInviteCardDao;

@Repository("docInviteCardDao")
public class DocInviteCardImpl implements IDocInviteCardDao {

	private static final Logger logger = LoggerFactory.getLogger(IDocInviteCardDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getInviteCardDoc() {
		String sql = "select dic.id,title,template_name,spread_template_name,need_invite_person_count,doc_id,DATE_FORMAT(dic.create_time,'%Y-%m-%d') as readable_date, publish_status from doc_invite_card dic left join document doc on dic.doc_id=doc.id";
		List<Map<String, Object>> retList = new ArrayList<>();
		try{
			retList = jdbcTemplate.queryForList(sql);
		}catch(Exception e){
			logger.error("getAllDocumentByKey error : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean delDocInviteCard(String id) {
		String sql = "delete from doc_invite_card where id=?";
		int affectedRows = 0;
		try{
			affectedRows = jdbcTemplate.update(sql, id);
		}catch(Exception e){
			logger.error("getAllDocumentByKey error : {}", e.toString());
		}
		return affectedRows!=0;
	}

	@Override
	public boolean updateDocInviteCardPublishStatus(String doc_id, String status) {
		String sql = "update doc_invite_card set publish_status=? where id=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, status, doc_id);
		} catch(Exception ex) {
			logger.error(ex.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public boolean adddocInviteCard(String id, String templateName, String needInvitePersonCount, String spreadTemplateName, String copywriter) {
		String sql = "replace into doc_invite_card (doc_id, template_name, need_invite_person_count, spread_template_name, create_time, copywriter) values (?,?,?,?,?,?)";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, id, templateName, needInvitePersonCount, spreadTemplateName, new Timestamp(System.currentTimeMillis()), copywriter);
		} catch(Exception ex) {
			logger.error(ex.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public Map<String, Object> getInviteCardByDocId(String doc_id) {
		String sql = "select * from doc_invite_card where doc_id=?";
		Map<String, Object> retMap = new HashMap<>();
		try{
			retMap = jdbcTemplate.queryForMap(sql, doc_id);
		}catch(Exception e){
			logger.error("getInviteCardByDocId error : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public boolean addPushRecord(String unionid, String doc_id) {
		String sql = "replace into doc_invite_card_unionid (unionid, doc_id, create_time) values (?,?,?)";
		int affectedRows = 0;
		try{
			affectedRows = jdbcTemplate.update(sql, unionid, doc_id, new Timestamp(System.currentTimeMillis()));
		}catch(Exception e){
			logger.error("getInviteCardByDocId error : {}", e.toString());
		}
		return affectedRows!=0;
	}

	@Override
	public int getDocInviteRecordByFriendOpendIdAndDocId(String srcUnion_id, String union_id, String doc_id) {
		int count = 0;
		try {
			String sql = "select count(*) from user_doc_invite_record where src_open_id=? and friend_open_id=? and doc_id=?";
			count = jdbcTemplate.queryForObject(sql, new Object[]{srcUnion_id, union_id, doc_id}, Integer.class);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return count;
	}

	@Override
	public boolean addDocInviteRecord(String srcUnionId, String friendUnionId, String doc_id) {
		try{
			String sql= "insert into user_doc_invite_record(src_open_id, friend_open_id, doc_id, create_time) values (?,?,?,?)";
			int num = jdbcTemplate.update(sql, 
					srcUnionId, friendUnionId, doc_id, new Timestamp(System.currentTimeMillis()));
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public int getDocInviteSupportTotal(String srcUnionId, String doc_id) {
		int supportTotal = 0;
		try {
			String sql = "select count(*) from user_doc_invite_record where src_open_id=? and doc_id=?";
			supportTotal = jdbcTemplate.queryForObject(sql, new Object[]{srcUnionId, doc_id}, Integer.class);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return supportTotal;
	}

	@Override
	public Map<String, Object> getUserAndUserDocByUserOpenId(String srcUnionId, String doc_id) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select u.id as user_id, ud.id as user_doc_id, ud.pay_status from user as u LEFT JOIN user_document as ud on u.id=ud.user_id and ud.doc_id=? where u.open_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{doc_id, srcUnionId});
		} catch (Exception e) {
			logger.error("getUserAndUserDocByUserOpenId exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getPublishedDocInviteCard() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select dic.*, d.title from doc_invite_card dic left join document d on dic.doc_id=d.id where dic.publish_status=1 order by dic.doc_id";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("getPublishedDocInviteCard exception : {}", e.toString());
		}
		return retList;
	}
	
}
