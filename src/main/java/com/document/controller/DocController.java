package com.document.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.course.controller.CourseController;
import com.document.dao.IDocDao;

@Controller
public class DocController {

	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
	
	@Autowired
	private IDocDao docDao;
	
	@RequestMapping("/document/addDocument")
	@ResponseBody
	public Map<String, Object> addDocument(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, Object> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		int index = 0;
		if(multipartRequest != null) {
			Iterator<String> ite = multipartRequest.getFileNames();
			while(ite.hasNext()) {
				MultipartFile file = multipartRequest.getFile(ite.next());
				fileName = getGernarateFileName(file);
				try {
					FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
				} catch(Exception e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				switch(index) {
				case 0:
					paramsMap.put("snapshot", new String[] {fileName});
					break;
				case 1:
					paramsMap.put("document", new String[] {fileName});
					break;
				}
				index ++;
			}
		}
		
		if(docDao.addDocument(paramsMap)) {
			retMap.put("msg", "添加成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "添加失败，请重试！");
			retMap.put("error", "1");
		}
		return retMap;
	}
	
	@RequestMapping("/document/updateDocumentById")
	@ResponseBody
	public Map<String, Object> updateDocumentById(HttpServletRequest request, DefaultMultipartHttpServletRequest multipartRequest) {
		Map<String, Object> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		
		String indexStr = request.getParameter("index");
		int index = -1;
		try{
			index = Integer.valueOf(indexStr);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		
		int count = 0;
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				fileName = getGernarateFileName(multifile);
				try {
					FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
				if(index != 2) {
					count = index;
				}
				switch(count) {
				case 0: paramsMap.put("snapshot", new String[] { fileName });
				break;
				case 1: paramsMap.put("document", new String[] { fileName });
				break;
				}
				count ++;
			}
		}
		
		if(docDao.updateDocument(paramsMap)) {
			retMap.put("msg", "更新成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "更新失败，请重试！");
			retMap.put("error", "1");
		}
		return retMap;
		
	}
	
	@RequestMapping("/document/getAllDocument")
	@ResponseBody
	public List<Map<String, Object>> getAllDocument(HttpServletRequest request) {
		List<Map<String, Object>> retList = new ArrayList<>();
		retList = docDao.getAllDocument();
		return retList;
	}
	
	@RequestMapping("/document/getDocumentList")
	@ResponseBody
	public List<Map<String, Object>> getDocumentList(HttpServletRequest request) {
		List<Map<String, Object>> retList = new ArrayList<>();
		
		return retList;
	}
	
	@RequestMapping("/document/getDocDetailById")
	@ResponseBody
	public Map<String, Object> getDocDetailById(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String id = request.getParameter("id");
		retMap = docDao.getDocDetailById(id);
		return retMap;
	}
	
	@RequestMapping("/document/deleteDocumentById")
	@ResponseBody
	public Map<String, Object> deleteDocumentById(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<>();
		String id = request.getParameter("id");
		if(docDao.deleteDocumentById(id)) {
			retMap.put("msg", "删除成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "删除失败，请重试！");
			retMap.put("error", "1");
		}
		return retMap;
	}
	
	@RequestMapping("/document/getDocByIndexAndKey")
	@ResponseBody
	public List<Map<String, Object>> getDocByIndexAndKey(HttpServletRequest request) {
		int index = 1;
		int length = 10;
		String key = "";
		List<Map<String, Object>> retList = null;
		try {
			index = Integer.valueOf(request.getParameter("index"));
			key = request.getParameter("key");
			retList = docDao.getDocByIndexAndKey((index-1)*length, length, key);
		} catch (Exception e) {
			logger.error("getDocByIndexAndKey error : {}", e.toString());
		}
		return retList;
	}
	
	@RequestMapping("/document/getDocTotalPageByKey")
	@ResponseBody
	public int getDocTotalPageByKey(HttpServletRequest request) {
		int length = 10;
		int total = 0;
		int totalPage = 0;
		String key = "";
		try {
			length = Integer.valueOf(request.getParameter("length"));
			key = request.getParameter("key");
			total = docDao.getAllDocumentByKey(key).size();
			if(total != 0) {
				if((total%length) == 0) {
					totalPage = (total/length);
				} else {
					totalPage = (total/length) + 1;
				}
			}
		} catch(Exception e) {
			e.toString();
		}
		return totalPage;
	}
	
	private String getGernarateFileName(MultipartFile file) {
		String extendName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
		return UUID.randomUUID().toString() + (extendName == null ? ".unknown" : "." + extendName);
	}
}
