package com.ocfisher.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ocfisher.dao.ICglxDao;
import com.ocfisher.model.BannerModel;
import com.ocfisher.model.StoryItem;
import com.ocfisher.model.StoryItemDetail;

@Repository("cglxDao")
public class CglxDaoImpl implements ICglxDao {

	private static final Logger logger = LoggerFactory
			.getLogger(CglxDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getAllStory(int beginIndex, int length) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from (select si.*, GROUP_CONCAT(st.name SEPARATOR ';') as tag from story_item as si left join story_item_tag as sit on sit.story_item_id=si.id LEFT JOIN story_tag as st on st.id=sit.story_tag_id group by si.id) final_result order by create_time desc limit ?, ?";
			retList = jdbcTemplate.queryForList(sql, new Object[]{beginIndex, length});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public List<Map<String, Object>> getStoryByTag(List<Integer> tagIdList, int beginIndex, int length) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from (select si.*, GROUP_CONCAT(st.name SEPARATOR ';') as tag from story_item as si INNER JOIN story_item_tag as sit on sit.story_item_id=si.id INNER JOIN story_tag as st on st.id=sit.story_tag_id and (######) group by si.id) final_result order by create_time desc limit ?, ?";
			StringBuilder sb = new StringBuilder();
			if(tagIdList != null && tagIdList.size() != 0) {
				Integer integer = tagIdList.remove(0);
				sb.append("st.id=");
				sb.append(String.valueOf(integer));
			}
			for(Integer integer : tagIdList) {
				sb.append(" or st.id=");
				sb.append(String.valueOf(integer));
			}
			sql = sql.replace("######", sb.toString());
			retList = jdbcTemplate.queryForList(sql, new Object[]{beginIndex, length});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public Map<String, Object> getStoryDetailByItemId(long storyItemId) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from story_item_detail where story_item_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{storyItemId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getAllStudyAbroad() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from studyabroad_item";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public Map<String, Object> getStudyAbroadByItemId(long studyabroadItemId) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from studyabroad_item_detail where studyabroad_item_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{studyabroadItemId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public long addStory(StoryItem item) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					String sql = "insert into story_item (snapshot,title,base_info,detail_info,create_time)"
							+ " values (?, ?, ?, ?, ?)";
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, item.getSnapshot());
					ps.setString(2, item.getTitle());
					ps.setString(3, item.getBaseInfo());
					ps.setString(4, item.getDetailInfo());
					ps.setTimestamp(5, item.getCreate_time());
					return ps;
				}
			}, keyHolder);
			logger.debug("addStory primary key : {}", keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
		} catch (DataAccessException e) {
			logger.error("addStory error." + e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public long addStoryDetail(StoryItemDetail itemDetail) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					String sql = "insert into story_item_dtail (story_item_id, title, snapshot,student_name,"
							+ "student_background,enroll_major,enroll_else,apply_advantage,"
							+ "apply_difficulty,details_info,create_time)"
							+ " values (?, ?, ?, ?, ?)";
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, itemDetail.getStoryItemId());
					ps.setString(2, itemDetail.getTitle());
					ps.setString(3, itemDetail.getSnapshot());
					ps.setString(4, itemDetail.getStudentName());
					ps.setString(5, itemDetail.getStudentBackground());
					ps.setString(6, itemDetail.getEnrollMajor());
					ps.setString(7, itemDetail.getEnrollElse());
					ps.setString(8, itemDetail.getApplyAdvantage());
					ps.setString(9, itemDetail.getApply_difficulty());
					ps.setString(10, itemDetail.getDetailsInfo());
					ps.setTimestamp(11, itemDetail.getCreateTime());
					return ps;
				}
			}, keyHolder);
			logger.debug("addStoryDetail primary key : {}", keyHolder.getKey().longValue());
			return keyHolder.getKey().longValue();
		} catch (DataAccessException e) {
			logger.error("addStoryDetail error." + e.getMessage());
			e.printStackTrace();
		}
		return -1;
	}

	//banner
	@Override
	public boolean addBanner(BannerModel bannerModel) {
		try{
			String sql= "insert into banner(file_name, foreign_key, create_time, url, tag, title) values (?,?,?,?,?,?)";
			int num = jdbcTemplate.update(sql, bannerModel.getFileName(), 
					bannerModel.getForeignKey(),
					bannerModel.getCreate_time(),
					bannerModel.getUrl(),
					bannerModel.getTag(),
					bannerModel.getTitle());
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getAllBanner() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select id, DATE_FORMAT(create_time, '%Y-%m-%d') as readable_date, url, tag, title, file_name, foreign_key from banner";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean delBanner(long id) {
		try{
			String sql= "delete from banner where id=?";
			int num = jdbcTemplate.update(sql, id);
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getRecommendArticle() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select articles.*, tag.name, DATE_FORMAT(articles.create_time, '%Y-%m-%d') as readable_date from articles as articles INNER JOIN tag as tag on tag.id=articles.tag_id where recommend=1 order by articles.create_time desc limit 0, 3";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}
	
	@Override
	public List<Map<String, Object>> getArticlesByTag(int tag, int beginIndex, int length) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select articles.*, tag.name, DATE_FORMAT(articles.create_time, '%Y-%m-%d') as readable_date from articles as articles INNER JOIN tag as tag on tag.id=articles.tag_id and tag.id = ? order by articles.create_time desc limit ?, ?";
			retList = jdbcTemplate.queryForList(sql, new Object[]{tag, beginIndex, length});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}
	
	@Override
	public Map<String, Object> getArticlesDetail(int id) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select articles.recommend, articles.abstract, articles.image as snapshot, articles.tag_id, articles.author, DATE_FORMAT(articles.create_time, '%Y-%m-%d') as readable_date, ad.*, tag.name from articles as articles INNER JOIN articles_detail as ad on articles.id=ad.article_id and articles.id=? INNER JOIN tag as tag on tag.id=articles.tag_id";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{id});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}
	
	@Override
	public List<Map<String, Object>> getArticlesByTagExcludeId(int tag, int exculdeId) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select articles.*, tag.name, DATE_FORMAT(articles.create_time, '%Y-%m-%d') as readable_date from articles as articles INNER JOIN tag as tag on tag.id=articles.tag_id and tag.id = ? where articles.id != ? order by articles.create_time desc limit 0, 3";
			retList = jdbcTemplate.queryForList(sql, new Object[]{tag, exculdeId});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public long addArticle(Map<String, String[]> articleMap) {
		long articleId = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					String sql= "insert into articles(title, abstract, tag_id, "
							+ "image, author, recommend, create_time) values (?,?,?,?,?,?,?)";
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, articleMap.get("title")[0]);
					ps.setString(2, articleMap.get("abstract")[0]);
					ps.setLong(3, Long.valueOf(articleMap.get("tag_id")[0]));
					ps.setString(4, articleMap.get("image")[0]);
					ps.setString(5, articleMap.get("author")[0]);
					ps.setInt(6, Integer.valueOf(articleMap.get("recommend")[0]));
					ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
					return ps;
				}
			}, keyHolder);
			articleId = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error("addArticle error, exception : {}", e.toString());
		}
		return articleId;
	}

	@Override
	public boolean updateArticle(Map<String, String[]> articleMap) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<>();
		sb.append("update articles set ");
		Long articleId = Long.valueOf(articleMap.remove("article_id")[0]);
		for(Map.Entry<String, String[]> entry : articleMap.entrySet()) {
			sb.append(" ");
			sb.append(entry.getKey());
			sb.append("=?,");
			if(("recommend").equals(entry.getKey()) || ("tag_id").equals(entry.getKey())) {
				String num = entry.getValue()[0];
				args.add(Long.valueOf(num));
			} else {
				args.add(entry.getValue()[0]);
			}
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(" where id=?");
		args.add(articleId);
		String sql = sb.toString();
		int num = 0;
		try{
			num = jdbcTemplate.update(sql, args.toArray());
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return num > 0;
	}

	@Override
	public boolean deleteArticle(long id) {
		try{
			String sql= "delete articles, articles_detail from articles "
					+ "INNER JOIN articles_detail on articles.id = articles_detail.article_id "
					+ "where articles.id = ?";
			int num = jdbcTemplate.update(sql, id);
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public long addArticleDetail(Map<String, String[]> articleDetailMap) {
		long articleDetailId = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					String sql= "insert into articles_detail(article_id, title, "
							+ "video_src, image, contents, create_time) values (?,?,?,?,?,?)";
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, Long.valueOf(articleDetailMap.get("article_id")[0]));
					ps.setString(2, articleDetailMap.get("title")[0]);
					ps.setString(3, articleDetailMap.get("video_src")[0]);
					ps.setString(4, articleDetailMap.get("image")[0]);
					ps.setString(5, articleDetailMap.get("contents")[0]);
					ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					return ps;
				}
			}, keyHolder);
			articleDetailId = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error("addArticle error, exception : {}", e.toString());
		}
		return articleDetailId;
	}

	@Override
	public boolean updateArticleDetail(Map<String, String[]> articleDetailMap) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<>();
		sb.append("update articles_detail set ");
		Long articleId = Long.valueOf(articleDetailMap.remove("article_id")[0]);
		for(Map.Entry<String, String[]> entry : articleDetailMap.entrySet()) {
			sb.append(" ");
			sb.append(entry.getKey());
			sb.append("=?,");
			args.add(entry.getValue()[0]);
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(" where article_id=?");
		args.add(articleId);
		String sql = sb.toString();
		int num = 0;
		try{
			num = jdbcTemplate.update(sql, args.toArray());
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return num > 0;
	}

	@Override
	public Map<String, Object> getUserByUserId(String phone) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from user where user_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{phone});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public boolean updateUserPwdByUserId(String phone, String pwd) {
		String sql = "update user set password=? where user_id=?";
		int affectedRows = 0;
		try {
			affectedRows = jdbcTemplate.update(sql, pwd, phone);
		} catch(Exception ex) {
			logger.error(ex.toString());
		}
		return affectedRows != 0;
	}

	@Override
	public boolean addUser(String phone, String pwd) {
		try{
			String sql= "insert into user(user_id, password, create_time) values (?,?,?)";
			int num = jdbcTemplate.update(sql, 
					phone, pwd, new Timestamp(System.currentTimeMillis()));
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public boolean addUserDetailInfo(Map<String, String[]> userDetailInfoMap) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<>();
		sb.append("update user set ");
		String userId = userDetailInfoMap.remove("user_id")[0];
		for(Map.Entry<String, String[]> entry : userDetailInfoMap.entrySet()) {
			sb.append(" ");
			sb.append(entry.getKey());
			sb.append("=?,");
			args.add(entry.getValue()[0]);
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(" where user_id=?");
		args.add(userId);
		String sql = sb.toString();
		int num = 0;
		try{
			num = jdbcTemplate.update(sql, args.toArray());
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return num > 0;
	}

	@Override
	public List<Map<String, Object>> getAllUser(int beginIndex, int length) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select id,user_id,name,school,major,grade,language,apply_major,user_type,create_time from user order by create_time desc limit ?, ?";
			retList = jdbcTemplate.queryForList(sql, new Object[]{beginIndex, length});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public Map<String, Object> getUserEvalByUserId(String phone) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from user_eval where user_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{phone});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public boolean updateUserEvalByUserId(Map<String, String[]> paramsMap) {
		StringBuilder sbName = new StringBuilder();
		StringBuilder sbWen = new StringBuilder();
		List<Object> args = new ArrayList<>();
		for(Entry<String, String[]> entry : paramsMap.entrySet()) {
			args.add(entry.getValue()[0]);
			sbName.append(entry.getKey() + ",");
			sbWen.append("?,");
		}
		sbName.append("create_time");
		sbWen.append("?");
		args.add(new Timestamp(System.currentTimeMillis()));
		try {
			String sql = "replace into user_eval ("+sbName.toString()+") values("+sbWen.toString()+")";
			jdbcTemplate.update(sql, args.toArray());
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return true;
	}

	@Override
	public List<Map<String, Object>> getRecommendCase() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from cases where is_recommend != 0 order by is_recommend asc, create_time desc";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRecommendCaseLimit() {
		List<Map<String, Object>> retList1 = null;
		List<Map<String, Object>> retList2 = null;
		try {
			String sql = "select * from cases where is_recommend=1 order by create_time desc limit 0,1";
			retList1 = jdbcTemplate.queryForList(sql);
			String sql2 = "select * from cases where is_recommend=2 order by create_time desc limit 0,2";
			retList2 = jdbcTemplate.queryForList(sql2);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return ListUtils.union(retList1, retList2);
	}
	
	
	@Override
	public List<Map<String, Object>> getCaseListByTag(String categoryTag, String countryTag, int beginIndex,
			int length) {
		List<Map<String, Object>> retList = null;
		List<Object> args = new ArrayList<>();
		StringBuilder condition = new StringBuilder();
		if(categoryTag != null && !categoryTag.isEmpty()) {
			condition.append(" and tag_category_name='" + categoryTag + "' ");
		}
		if(countryTag != null && !countryTag.isEmpty()) {
			condition.append(" and tag_country_name='" + countryTag + "' ");
		}
		args.add(beginIndex);
		args.add(length);
		try {
			String sql = "select *,DATE_FORMAT(create_time, '%Y-%m-%d') as readable_date from cases where is_recommend=0 #### order by create_time desc limit ?, ?";
			sql = sql.replaceAll("####", condition.toString());
			retList = jdbcTemplate.queryForList(sql, args.toArray());
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public Map<String, Object> getCaseDetail(long id) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from cases_detail where case_id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{id});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public long addCase(Map<String, String[]> caseMap) {
		long caseId = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					String sql= "insert into cases(tag_category_name, tag_country_name, "
							+ "title, sub_title, image, is_recommend, is_nav_recommend, create_time) values (?,?,?,?,?,?,?,?)";
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, caseMap.get("tag_category_name")[0]);
					ps.setString(2, caseMap.get("tag_country_name")[0]);
					ps.setString(3, caseMap.get("title")[0]);
					ps.setString(4, caseMap.get("sub_title")[0]);
					ps.setString(5, caseMap.get("image")[0]);
					ps.setInt(6, Integer.valueOf(caseMap.get("is_recommend")[0]));
					ps.setInt(7, Integer.valueOf(caseMap.get("is_nav_recommend")[0]));
					ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
					return ps;
				}
			}, keyHolder);
			caseId = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error("addCase error, exception : {}", e.toString());
		}
		return caseId;
	}

	@Override
	public boolean updateCase(Map<String, String[]> caseMap) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<>();
		sb.append("update cases set ");
		Long case_id = Long.valueOf(caseMap.remove("case_id")[0]);
		for(Map.Entry<String, String[]> entry : caseMap.entrySet()) {
			sb.append(" ");
			sb.append(entry.getKey());
			sb.append("=?,");
			args.add(entry.getValue()[0]);
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(" where id=?");
		args.add(case_id);
		String sql = sb.toString();
		int num = 0;
		try{
			num = jdbcTemplate.update(sql, args.toArray());
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return num > 0;
	}

	@Override
	public boolean deleteCase(long id) {
		try{
			String sql= "delete cases, cases_detail from cases "
					+ "INNER JOIN cases_detail on cases.id = cases_detail.case_id "
					+ "where cases.id = ?";
			int num = jdbcTemplate.update(sql, id);
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public long addCaseDetail(Map<String, String[]> caseDetailMap) {
		long caseDetailId = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					String sql= "insert into cases_detail (case_id, title, "
							+ "background_name, background_major, background_college, background_apply_major, background_apply_college, "
							+ "background_enroll_time, background_enter_time,"
							+ "background_info,train_target,image,"
							+ "major_chi, major_eng,"
							+ "create_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, Long.valueOf(caseDetailMap.get("case_id")[0]));
					ps.setString(2, caseDetailMap.get("title")[0]);
					ps.setString(3, caseDetailMap.get("background_name")[0]);
					ps.setString(4, caseDetailMap.get("background_major")[0]);
					ps.setString(5, caseDetailMap.get("background_college")[0]);
					ps.setString(6, caseDetailMap.get("background_apply_major")[0]);
					ps.setString(7, caseDetailMap.get("background_apply_college")[0]);
					ps.setString(8, caseDetailMap.get("background_enroll_time")[0]);
					ps.setString(9, caseDetailMap.get("background_enter_time")[0]);
					ps.setString(10, caseDetailMap.get("background_info")[0]);
					ps.setString(11, caseDetailMap.get("train_target")[0]);
					ps.setString(12, caseDetailMap.get("image")[0]);
					//ps.setString(13, caseDetailMap.get("major_info")[0]);
					ps.setString(13, caseDetailMap.get("major_chi")[0]);
					ps.setString(14, caseDetailMap.get("major_eng")[0]);
					//ps.setString(16, caseDetailMap.get("college_image")[0]);
					ps.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
					return ps;
				}
			}, keyHolder);
			caseDetailId = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error("addCaseDetail error, exception : {}", e.toString());
		}
		return caseDetailId;
	}

	@Override
	public boolean updateCaseDetail(Map<String, String[]> caseDetailMap) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<>();
		sb.append("update cases_detail set ");
		Long caseId = Long.valueOf(caseDetailMap.remove("case_id")[0]);
		for(Map.Entry<String, String[]> entry : caseDetailMap.entrySet()) {
			sb.append(" ");
			sb.append(entry.getKey());
			sb.append("=?,");
			args.add(entry.getValue()[0]);
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(" where case_id=?");
		args.add(caseId);
		String sql = sb.toString();
		int num = 0;
		try{
			num = jdbcTemplate.update(sql, args.toArray());
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return num > 0;
	}

	@Override
	public List<Map<String, Object>> getRecommendMedia(long id) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from medias where id != ? order by publish_time desc limit 0, 3";
			retList = jdbcTemplate.queryForList(sql, new Object[]{id});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public List<Map<String, Object>> getMediaList(int beginIndex, int length) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select id, video_src, title, sub_title, name, publish_time, DATE_FORMAT(create_time, '%Y-%m-%d') as readable_date from medias order by publish_time desc limit ?, ?";
			retList = jdbcTemplate.queryForList(sql, new Object[]{beginIndex, length});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public long addMedia(Map<String, String[]> mediaMap) {
		long mediaId = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		try {
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					String sql= "insert into medias(image, video_src, "
							+ "title, sub_title, name, contents,"
							+ "publish_time, create_time) values (?,?,?,?,?,?,?,?)";
					PreparedStatement ps = connection.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, mediaMap.get("image")[0]);
					ps.setString(2, mediaMap.get("video_src")[0]);
					ps.setString(3, mediaMap.get("title")[0]);
					ps.setString(4, mediaMap.get("sub_title")[0]);
					ps.setString(5, mediaMap.get("name")[0]);
					ps.setString(6, mediaMap.get("contents")[0]);
					ps.setString(7, mediaMap.get("publish_time")[0]);
					ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
					return ps;
				}
			}, keyHolder);
			mediaId = keyHolder.getKey().longValue();
		} catch (Exception e) {
			logger.error("addMedia error, exception : {}", e.toString());
		}
		return mediaId;
	}

	@Override
	public boolean updateMedia(Map<String, String[]> mediaMap) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<>();
		sb.append("update medias set ");
		Long id = Long.valueOf(mediaMap.remove("id")[0]);
		for(Map.Entry<String, String[]> entry : mediaMap.entrySet()) {
			sb.append(" ");
			sb.append(entry.getKey());
			sb.append("=?,");
			args.add(entry.getValue()[0]);
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(" where id=?");
		args.add(id);
		String sql = sb.toString();
		int num = 0;
		try{
			num = jdbcTemplate.update(sql, args.toArray());
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return num > 0;
	}

	@Override
	public boolean deleteMedia(long id) {
		try{
			String sql= "delete from medias where id = ?";
			int num = jdbcTemplate.update(sql, id);
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public Map<String, Object> getMediaById(long id) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from medias where id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{id});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> searchArticleAndMedia(String key) {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "(select articles.*, tag.name, DATE_FORMAT(articles.create_time, '%Y-%m-%d') as readable_date, 'article' as data_type, '0' as publish_time from articles as articles INNER JOIN tag as tag on tag.id=articles.tag_id where articles.title like '%"+key+"%' or articles.abstract like '%"+key+"%' order by articles.create_time desc) union (select id, title, sub_title as abstract, 0 as tag_id, image, name as author, 0 as recommend, create_time, name, '0' as readable_date, 'media' as data_type, publish_time from medias where title like '%"+key+"%' or sub_title like '%"+key+"%' order by articles.create_time desc)";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public List<Map<String, Object>> getNavRecommend() {
		List<Map<String, Object>> retMap = null;
		try {
			String sql = "select * from cases where is_nav_recommend=1 limit 0,2";
			retMap = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getAllApplyReport() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select *, DATE_FORMAT(create_time, '%Y-%m-%d') as readable_date from apply_report order by create_time desc";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean updateApplyReport(Map<String, String[]> applyReportMap) {
		StringBuilder sbName = new StringBuilder();
		StringBuilder sbWen = new StringBuilder();
		List<Object> args = new ArrayList<>();
		for(Entry<String, String[]> entry : applyReportMap.entrySet()) {
			args.add(entry.getValue()[0]);
			sbName.append(entry.getKey() + ",");
			sbWen.append("?,");
		}
		sbName.append("create_time");
		sbWen.append("?");
		args.add(new Timestamp(System.currentTimeMillis()));
		try {
			String sql = "replace into apply_report ("+sbName.toString()+") values("+sbWen.toString()+")";
			jdbcTemplate.update(sql, args.toArray());
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return true;
	}

	@Override
	public boolean deleteApplyReport(long id) {
		try{
			String sql = "delete from apply_report where id = ?";
			int num = jdbcTemplate.update(sql, id);
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public boolean deleteUserById(String phone) {
		try{
			String sql = "delete from user where user_id = ?";
			int num = jdbcTemplate.update(sql, phone);
			return num > 0;
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getAllArticle() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select articles.*, tag.id as tag_id, tag.name as tag_name, DATE_FORMAT(articles.create_time, '%Y-%m-%d') as readable_date from articles as articles INNER JOIN tag as tag on tag.id=articles.tag_id order by articles.create_time desc limit 0, 30";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public List<Map<String, Object>> getAllEval() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select *, DATE_FORMAT(create_time, '%Y-%m-%d') as readable_date from user_eval";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public boolean updateEval(Map<String, String[]> evalMap) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new ArrayList<>();
		sb.append("update user_eval set ");
		Long case_id = Long.valueOf(evalMap.remove("id")[0]);
		for(Map.Entry<String, String[]> entry : evalMap.entrySet()) {
			sb.append(" ");
			sb.append(entry.getKey());
			sb.append("=?,");
			args.add(entry.getValue()[0]);
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(" where id=?");
		args.add(case_id);
		String sql = sb.toString();
		int num = 0;
		try{
			num = jdbcTemplate.update(sql, args.toArray());
		}catch(Exception e){
			logger.error("exception : {}", e.toString());
		}
		return num > 0;
	}

	@Override
	public List<Map<String, Object>> getAllTag() {
		List<Map<String, Object>> retList = null;
		try {
			String sql = "select * from tag";
			retList = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}

	@Override
	public Map<String, Object> getCaseById(int id) {
		Map<String, Object> retMap = null;
		try {
			String sql = "select * from cases where id=?";
			retMap = jdbcTemplate.queryForMap(sql, new Object[]{id});
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retMap;
	}

	@Override
	public List<Map<String, Object>> getRelatedCase(String categoryTag, String countryTag, int beginIndex, int length) {
 		List<Map<String, Object>> retList = null;
		List<Object> args = new ArrayList<>();
		StringBuilder condition = new StringBuilder();
		if(categoryTag != null && !categoryTag.isEmpty()) {
			condition.append(" and tag_category_name like '%" + categoryTag + "%' ");
		}
		if(countryTag != null && !countryTag.isEmpty()) {
			condition.append(" and tag_country_name like '%" + countryTag + "%' ");
		}
		args.add(beginIndex);
		args.add(length);
		try {
			String sql = "select *, DATE_FORMAT(create_time, '%Y-%m-%d') as readable_date from cases where (is_recommend=0 || is_recommend=1) #### order by create_time desc limit ?, ?";
			sql = sql.replaceAll("####", condition.toString());
			retList = jdbcTemplate.queryForList(sql, args.toArray());
		} catch (Exception e) {
			logger.error("exception : {}", e.toString());
		}
		return retList;
	}
}