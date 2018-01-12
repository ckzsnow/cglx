package com.document.dao;

import java.util.List;
import java.util.Map;

public interface IDocDao {

	boolean addDocument(Map<String, String[]> paramsMap);

	boolean updateDocument(Map<String, String[]> paramsMap);

	List<Map<String, Object>> getAllDocument();

	boolean deleteDocumentById(String id);

	Map<String, Object> getDocDetailById(String id);

	List<Map<String, Object>> getDocByIndexAndKey(int start, int length, String key);

	List<Map<String, Object>> getAllDocumentByKey(String key);

	boolean updateUserDocById(String id);

	boolean addUserDocAndPayStatus(String srcUnionId, String doc_id, String tradeNo);

	Map<String, Object> getUserDocByUserIdAndDocId(String user_id, String doc_id);

	boolean addUserDoc(String userId, String doc_id, String orderId);

	boolean updateUserDocByTradeNo(String outTradeNo, int i);

	int getPayStatusByOrderIdAndCourseId(String doc_id, String orderId);

}
