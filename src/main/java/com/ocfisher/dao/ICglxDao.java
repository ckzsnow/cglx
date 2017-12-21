package com.ocfisher.dao;

import java.util.List;
import java.util.Map;

import com.ocfisher.model.BannerModel;
import com.ocfisher.model.StoryItem;
import com.ocfisher.model.StoryItemDetail;

public interface ICglxDao {

	public List<Map<String, Object>> getAllStory(int beginIndex, int length);
	
	public List<Map<String, Object>> getStoryByTag(List<Integer> tagIdList, int beginIndex, int length);
	
	public Map<String, Object> getStoryDetailByItemId(long storyItemId);
	
	public List<Map<String, Object>> getAllStudyAbroad();
	
	public Map<String, Object> getStudyAbroadByItemId(long studyabroadItemId);
	
	public long addStory(StoryItem item);
	
	public long addStoryDetail(StoryItemDetail itemDetail);
	
	//banner
	public boolean addBanner(BannerModel bannerModel);
	
	public boolean delBanner(long id);
	
	public List<Map<String, Object>> getAllBanner();
	
	//articles
	public List<Map<String, Object>> getRecommendArticle();
	
	public List<Map<String, Object>> getArticlesByTag(int tag, int beginIndex, int length);
	
	public List<Map<String, Object>> getAllArticles(int beginIndex, int length);
	
	public Map<String, Object> getArticlesDetail(int id);
	
	public List<Map<String, Object>> getArticlesByTagExcludeId(int tag, int exculdeId);
	
	public long addArticle(Map<String, String[]> articleMap);
	
	public boolean updateArticle(Map<String, String[]> articleMap);
	
	public boolean deleteArticle(long id);
	
	public long addArticleDetail(Map<String, String[]> articleDetailMap);
	
	public boolean updateArticleDetail(Map<String, String[]> articleDetailMap);
	
	public List<Map<String, Object>> getAllArticle();
	
	//user
	public Map<String, Object> getUserById(String id);
	
	public Map<String, Object> getUserByPhone(String phone);
	
	public Map<String, Object> getUserByOpenId(String openId);
	
	public boolean updateUserPwdByUserPhone(String phone, String pwd);
	
	public boolean updateUserInfoByOpenId(String openId, String name, String headimage);
	
	public boolean addUser(String phone, String pwd);
	
	public long addUserByOpenid(String openid, String nickname, String headimage);
	
	public boolean deleteUserById(String id);
	
	public boolean addUserDetailInfo(Map<String, String[]> userDetailInfoMap);
	
	public List<Map<String, Object>> getAllUser(int beginIndex, int length);
	
	public Map<String, Object> getUserEvalByUserId(String phone);
	
	public boolean updateUserEvalByUserId(Map<String, String[]> paramsMap);
	
	//case
	public List<Map<String, Object>> getRecommendCase();
	public List<Map<String, Object>> getRecommendCaseLimit();
	
	public List<Map<String, Object>> getCaseListByTag(String categoryTag, String countryTag, int beginIndex, int length);
	
	public Map<String, Object> getCaseDetail(long id);
	
	public long addCase(Map<String, String[]> caseMap);
	
	public boolean updateCase(Map<String, String[]> caseMap);
	
	public boolean deleteCase(long id);
	
	public long addCaseDetail(Map<String, String[]> caseDetailMap);
	
	public boolean updateCaseDetail(Map<String, String[]> caseDetailMap);
	
	//media
	public Map<String, Object> getMediaById(long id);
	
	public List<Map<String, Object>> getRecommendMedia(long id);
		
	public List<Map<String, Object>> getMediaList(int beginIndex, int length);
	
	public long addMedia(Map<String, String[]> mediaMap);
		
	public boolean updateMedia(Map<String, String[]> mediaMap);
		
	public boolean deleteMedia(long id);
	
	//search
	public List<Map<String, Object>> searchArticleAndMedia(String key);
	
	//nav recommend
	public List<Map<String, Object>> getNavRecommend();
	
	//apply report
	public List<Map<String, Object>> getAllApplyReport();
	
	public boolean updateApplyReport(Map<String, String[]> applyReportMap);
	
	public boolean deleteApplyReport(long id);	
	
	public List<Map<String, Object>> getAllEval();
	
	public boolean updateEval(Map<String, String[]> evalMap);
	
	public List<Map<String, Object>> getAllTag();

	public Map<String, Object> getCaseById(int id_);
	
	public List<Map<String, Object>> getRelatedCase(String categoryTag, String countryTag, int beginIndex, int length);

	public Map<String, Object> getUserSubscribeByUionId(String unionid);
	
	public boolean updateJoinGroup(String fileName, int id);
	
	public List<Map<String, Object>> getJoinGroupInfo(int id);

	public boolean bindPhone(String user_id, String phone);
}
