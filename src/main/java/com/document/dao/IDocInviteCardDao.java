package com.document.dao;

import java.util.List;
import java.util.Map;

public interface IDocInviteCardDao {

	List<Map<String, Object>> getInviteCardDoc();

	boolean delCourseCard(String id);

	boolean updateCourseCardPublishStatus(String course_id, String status);

	boolean adddocInviteCard(String id, String templateName, String needInvitePersonCount, String spreadTemplateName, String copywriter);
	
}
