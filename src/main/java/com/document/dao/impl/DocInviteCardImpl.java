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

import com.document.dao.IDocInviteCardDao;

@Repository("docInviteCardDao")
public class DocInviteCardImpl implements IDocInviteCardDao {

	private static final Logger logger = LoggerFactory.getLogger(IDocInviteCardDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getInviteCardDoc() {
		String sql = "select dic.id,title,template_name,spread_template_name,need_invite_person_count,DATE_FORMAT(doc_invite_card.create_time,'%Y-%m-%d') as readable_date from doc_invite_card dic left join document doc on dic.doc_id=doc.id";
		List<Map<String, Object>> retList = new ArrayList<>();
		try{
			retList = jdbcTemplate.queryForList(sql);
		}catch(Exception e){
			logger.error("getAllDocumentByKey error : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean delCourseCard(String id) {
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
	public boolean updateCourseCardPublishStatus(String course_id, String status) {
		String sql = "update doc_invite_card set publish_status=? where id=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, status, course_id);
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
	
}
