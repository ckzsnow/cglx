package com.document.dao;

import java.util.List;
import java.util.Map;

public interface IDocInviteCardDao {

	List<Map<String, Object>> getInviteCardDoc();

	boolean delDocInviteCard(String id);

	boolean updateDocInviteCardPublishStatus(String doc_id, String status);

	boolean adddocInviteCard(String id, String templateName, String needInvitePersonCount, String spreadTemplateName, String copywriter);

	Map<String, Object> getInviteCardByDocId(String doc_id);

	boolean addPushRecord(String unionid, String doc_id);

	int getDocInviteRecordByFriendOpendIdAndDocId(String srcUnion_id, String union_id, String doc_id);

	boolean addDocInviteRecord(String srcUnionId, String friendUnionId, String doc_id);

	int getDocInviteSupportTotal(String srcUnionId, String doc_id);

	Map<String, Object> getUserAndUserDocByUserOpenId(String srcUnionId, String doc_id);
	
}
