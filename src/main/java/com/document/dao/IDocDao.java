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

}
