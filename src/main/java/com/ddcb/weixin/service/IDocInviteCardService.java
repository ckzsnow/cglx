package com.ddcb.weixin.service;

import java.util.Map;

public interface IDocInviteCardService {

	void generateDocInviteCardAndPushToUser(Map<String, Object> docMap, String open_id);
	void addDocPushRecord(String open_id, String doc_id);
	void pushDocInviteNotify(String srcOpenId, String friendOpenId, String doc_id);
}
