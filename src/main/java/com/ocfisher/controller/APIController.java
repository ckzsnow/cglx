package com.ocfisher.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.ocfisher.common.UserPwdMD5Encrypt;
import com.ocfisher.dao.ICglxDao;
import com.ocfisher.model.BannerModel;
import com.ocfisher.model.StoryItem;
import com.ocfisher.model.StoryItemDetail;

@Controller
public class APIController {

	private static final Logger logger = LoggerFactory.getLogger(APIController.class);

	@Autowired
	private ICglxDao cglxDao;

	@RequestMapping("/")
	public String getRootHtml() {
		return "redirect:/view/homepage.html";
	}

	@RequestMapping("/story/getAllStory")
	@ResponseBody
	public List<Map<String, Object>> getAllStory(HttpServletRequest request) {
		String bIndex = request.getParameter("begin_index");
		String length = request.getParameter("length");
		int bIndex_ = 0;
		int length_ = 5;
		try {
			bIndex_ = Integer.valueOf(bIndex);
			length_ = Integer.valueOf(length);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		List<Map<String, Object>> retList = cglxDao.getAllStory(bIndex_, length_);
		return retList;
	}

	@RequestMapping("/story/getStoryByTag")
	@ResponseBody
	public List<Map<String, Object>> getStoryByTag(HttpServletRequest request) {
		List<Map<String, Object>> retList = null;
		String tagStr = request.getParameter("tag");
		String bIndex = request.getParameter("begin_index");
		String length = request.getParameter("length");
		int bIndex_ = 0;
		int length_ = 5;
		try {
			bIndex_ = Integer.valueOf(bIndex);
			length_ = Integer.valueOf(length);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		if (tagStr != null && !tagStr.isEmpty()) {
			tagStr = tagStr.replaceAll(" ", "");
			String[] tags = tagStr.split(";");
			List<Integer> tagList = new ArrayList<>();
			for (String tag : tags) {
				try {
					tagList.add(Integer.valueOf(tag));
				} catch (Exception ex) {
					logger.error(ex.toString());
				}
			}
			retList = cglxDao.getStoryByTag(tagList, bIndex_, length_);
		} else {
			retList = cglxDao.getAllStory(bIndex_, length_);
		}
		return retList;
	}

	@RequestMapping("/story/getStoryDetailByItemId")
	@ResponseBody
	public Map<String, Object> getStoryDetailByItemId(HttpServletRequest request) {
		Map<String, Object> retMap = null;
		String story_item_id = request.getParameter("story_item_id");
		int itemId = 0;
		try {
			itemId = Integer.valueOf(story_item_id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		retMap = cglxDao.getStoryDetailByItemId(itemId);
		return retMap;
	}

	@RequestMapping("/story/getAllStudyAbroad")
	@ResponseBody
	public List<Map<String, Object>> getAllStudyAbroad(HttpServletRequest request) {
		List<Map<String, Object>> retList = cglxDao.getAllStudyAbroad();
		return retList;
	}

	@RequestMapping("/story/getStudyAbroadByItemId")
	@ResponseBody
	public Map<String, Object> getStudyAbroadByItemId(HttpServletRequest request) {
		Map<String, Object> retMap = null;
		String story_item_id = request.getParameter("studyabroad_item_id");
		int itemId = 0;
		try {
			itemId = Integer.valueOf(story_item_id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		retMap = cglxDao.getStoryDetailByItemId(itemId);
		return retMap;
	}

	@RequestMapping("/story/addStory")
	@ResponseBody
	public Map<String, String> addStory(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/story";
		List<String> snapshotImageList = new ArrayList<>();
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				String fileName = getGernarateFileName(multifile);
				try {
					FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
					snapshotImageList.add(fileName);
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
			}
		}
		if (snapshotImageList.size() != 2) {
			retMap.put("msg", "上传图片失败！");
			retMap.put("error", "1");
			return retMap;
		}
		String title = request.getParameter("title");
		String baseInfo = request.getParameter("base_info");
		String item_detail = request.getParameter("item_detail");
		String student_name = request.getParameter("student_name");
		String student_background = request.getParameter("student_background");
		String enroll_major = request.getParameter("enroll_major");
		String enroll_else = request.getParameter("enroll_else");
		String apply_advantage = request.getParameter("apply_advantage");
		String apply_difficulty = request.getParameter("apply_difficulty");
		String details_info = request.getParameter("details_info");
		StoryItem si = new StoryItem();
		si.setBaseInfo(baseInfo);
		si.setCreate_time(new Timestamp(System.currentTimeMillis()));
		si.setDetailInfo(item_detail);
		si.setSnapshot(snapshotImageList.get(0));
		si.setTitle(title);
		long pId = cglxDao.addStory(si);
		if (pId == -1) {
			retMap.put("msg", "保存StoryItem失败！");
			retMap.put("error", "1");
			return retMap;
		}
		StoryItemDetail sid = new StoryItemDetail();
		sid.setApply_difficulty(apply_difficulty);
		sid.setApplyAdvantage(apply_advantage);
		sid.setCreateTime(new Timestamp(System.currentTimeMillis()));
		sid.setDetailsInfo(details_info);
		sid.setEnrollElse(enroll_else);
		sid.setEnrollMajor(enroll_major);
		sid.setSnapshot(snapshotImageList.get(1));
		sid.setStoryItemId(pId);
		sid.setStudentBackground(student_background);
		sid.setStudentName(student_name);
		sid.setTitle(title);
		pId = cglxDao.addStoryDetail(sid);
		if (pId == -1) {
			retMap.put("msg", "保存StoryItemDetail失败！");
			retMap.put("error", "1");
			return retMap;
		}
		retMap.put("msg", "保存数据成功！");
		retMap.put("error", "0");
		return retMap;
	}

	private String getGernarateFileName(MultipartFile file) {
		Random rdm = new Random(System.currentTimeMillis());
		String extendName = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".") + 1);
		return System.currentTimeMillis() + Math.abs(rdm.nextInt()) % 1000
				+ (extendName == null ? ".unknown" : "." + extendName);
	}

	// banner
	@RequestMapping("/banner/addBanner")
	@ResponseBody
	public Map<String, String> addBanner(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/files/imgs";
		/*String forId = request.getParameter("forId");
		String tag = request.getParameter("tag");*/
		String url = request.getParameter("url");
		String title = request.getParameter("title");
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				String fileName = getGernarateFileName(multifile);
				try {
					FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
					BannerModel bm = new BannerModel();
					bm.setCreate_time(new Timestamp(System.currentTimeMillis()));
					bm.setFileName(fileName);
					bm.setTitle(title);
					bm.setUrl(url);
					cglxDao.addBanner(bm);
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
			}
			retMap.put("msg", "添加成功");
			retMap.put("error", "0");
			return retMap;
		} else {
			retMap.put("msg", "上传文件为空！");
			retMap.put("error", "1");
			return retMap;
		}
	}

	@RequestMapping("/banner/deleteBanner")
	@ResponseBody
	public Map<String, String> deleteBanner(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String id = request.getParameter("id");
		try {
			if (cglxDao.delBanner(Long.valueOf(id))) {
				retMap.put("msg", "删除成功！");
				retMap.put("error", "0");
				return retMap;
			} else {
				retMap.put("msg", "删除失败！");
				retMap.put("error", "1");
				return retMap;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			retMap.put("msg", ex.toString());
			retMap.put("error", "1");
			return retMap;
		}
	}

	@RequestMapping("/banner/getAllBanner")
	@ResponseBody
	public List<Map<String, Object>> getAllBanner(HttpServletRequest request) {
		return cglxDao.getAllBanner();
	}

	// articles
	@RequestMapping("/articles/getRecommendArticle")
	@ResponseBody
	public List<Map<String, Object>> getRecommendArticle(HttpServletRequest request) {
		return cglxDao.getRecommendArticle();
	}

	@RequestMapping("/articles/getArticlesByTag")
	@ResponseBody
	public List<Map<String, Object>> getArticlesByTag(HttpServletRequest request) {
		String page = request.getParameter("page");
		String length = request.getParameter("length");
		String tag_id = request.getParameter("tag_id");
		int page_ = 1;
		int length_ = 5;
		int tag_id_ = 1;
		try {
			page_ = Integer.valueOf(page);
			length_ = Integer.valueOf(length);
			tag_id_ = Integer.valueOf(tag_id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getArticlesByTag(tag_id_, (page_ - 1) * length_, length_);
	}

	@RequestMapping("/articles/getAllArticlesForHomepage")
	@ResponseBody
	public List<Map<String, Object>> getAllArticlesForHomepage(HttpServletRequest request) {
		String page = request.getParameter("page");
		String length = request.getParameter("length");
		int page_ = 1;
		int length_ = 5;
		try {
			page_ = Integer.valueOf(page);
			length_ = Integer.valueOf(length);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getAllArticles((page_ - 1) * length_, length_);
	}
	
	@RequestMapping("/articles/getArticleDetail")
	@ResponseBody
	public Map<String, Object> getArticleDetail(HttpServletRequest request) {
		String id = request.getParameter("id");
		int id_ = 0;
		try {
			id_ = Integer.valueOf(id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getArticlesDetail(id_);
	}

	@RequestMapping("/articles/getArticlesByTagExcludeId")
	@ResponseBody
	public List<Map<String, Object>> getArticlesByTagExcludeId(HttpServletRequest request) {
		String exculde_id = request.getParameter("exculde_id");
		String tag_id = request.getParameter("tag_id");
		int exculde_id_ = 1;
		int tag_id_ = 1;
		try {
			exculde_id_ = Integer.valueOf(exculde_id);
			tag_id_ = Integer.valueOf(tag_id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getArticlesByTagExcludeId(tag_id_, exculde_id_);
	}
	
	@RequestMapping("/articles/getAllArticles")
	@ResponseBody
	public List<Map<String, Object>> getAllArticles(HttpServletRequest request) {
		return cglxDao.getAllArticle();
	}

	@RequestMapping("/articles/addArticle")
	@ResponseBody
	public Map<String, String> addArticle(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String article_id = request.getParameter("article_id");
		String flag = request.getParameter("flag");
		String fileNameListStr = request.getParameter("fileNameList");
		String[] fileNames = fileNameListStr.split(";");
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		System.out.println(paramsMap.toString());
		Map<String, String[]> articleParamsMap = new HashMap<>();
		Map<String, String[]> articleDetailParamsMap = new HashMap<>();
		articleParamsMap.put("article_id", paramsMap.get("article_id"));
		articleParamsMap.put("title", paramsMap.get("title"));
		articleParamsMap.put("abstract", paramsMap.get("abstract"));
		articleParamsMap.put("tag_id", paramsMap.get("tag_id"));
		articleParamsMap.put("author", paramsMap.get("author"));
		articleParamsMap.put("recommend", paramsMap.get("recommend"));
		articleDetailParamsMap.put("article_id", paramsMap.get("article_id"));
		articleDetailParamsMap.put("title", paramsMap.get("title"));
		articleDetailParamsMap.put("video_src", paramsMap.get("video_src"));
		articleDetailParamsMap.put("contents", paramsMap.get("contents"));
		String realPath = "/data/cglx/files/imgs";
		String notContentImagefileName = "";
		int index = 0;
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				String fileName = "";
				try {
					if(("1").equals(flag)) {
						notContentImagefileName = getGernarateFileName(multifile);
						FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, notContentImagefileName));
						flag = "0";
					} else {
						fileName = fileNames[index];
						index++;
						FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
					}
					
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
			}
		}
		if(article_id == null || article_id.isEmpty() || ("null").equals(article_id)) {
			articleParamsMap.put("image", new String[] { notContentImagefileName });
			articleDetailParamsMap.put("image", new String[] { notContentImagefileName });
			long id_ = cglxDao.addArticle(articleParamsMap);
			articleDetailParamsMap.put("article_id", new String[]{String.valueOf(id_)});
			cglxDao.addArticleDetail(articleDetailParamsMap);
		} else {
			if(!notContentImagefileName.isEmpty()) {
				articleParamsMap.put("image", new String[] { notContentImagefileName });
				articleDetailParamsMap.put("image", new String[] { notContentImagefileName });
			}
			cglxDao.updateArticle(articleParamsMap);
			cglxDao.updateArticleDetail(articleDetailParamsMap);
		}
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}

	@RequestMapping("/articles/updateArticle")
	@ResponseBody
	public Map<String, String> updateArticle(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
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
			}
		}
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (fileName != null && !fileName.isEmpty())
			paramsMap.put("image", new String[] { fileName });
		cglxDao.updateArticle(paramsMap);
		retMap.put("msg", "更新成功！");
		retMap.put("error", "0");
		return retMap;
	}

	@RequestMapping("/articles/deleteArticle")
	@ResponseBody
	public Map<String, String> deleteArticle(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String id = request.getParameter("id");
		try {
			if (cglxDao.deleteArticle(Long.valueOf(id))) {
				retMap.put("msg", "删除成功！");
				retMap.put("error", "0");
				return retMap;
			} else {
				retMap.put("msg", "删除失败！");
				retMap.put("error", "1");
				return retMap;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			retMap.put("msg", ex.toString());
			retMap.put("error", "1");
			return retMap;
		}
	}

	@RequestMapping("/articles/addArticleDetail")
	@ResponseBody
	public Map<String, String> addArticleDetail(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
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
			}
		}
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (fileName != null && !fileName.isEmpty())
			paramsMap.put("image", new String[] { fileName });
		if (cglxDao.addArticleDetail(paramsMap) > 0) {
			retMap.put("msg", "添加成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "写入数据库失败！");
			retMap.put("error", "1");
		}
		return retMap;
	}

	@RequestMapping("/articles/updateArticleDetail")
	@ResponseBody
	public Map<String, String> updateArticleDetail(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
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
			}
		}
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (fileName != null && !fileName.isEmpty())
			paramsMap.put("image", new String[] { fileName });
		if (cglxDao.updateArticleDetail(paramsMap)) {
			retMap.put("msg", "更新成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "写入数据库失败！");
			retMap.put("error", "1");
		}
		return retMap;
	}

	@RequestMapping("/user/userForgetPwd")
	@ResponseBody
	public Map<String, String> weixinForgetPwd(HttpSession httpSession, HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String user_id = request.getParameter("user_id");
		String user_pwd = request.getParameter("user_pwd");
		String check_code = request.getParameter("check_code");
		String sessionCheckCode = (String) httpSession.getAttribute("check_code");
		if (user_id == null || user_pwd == null || check_code == null) {
			retMap.put("error", "1");
			retMap.put("msg", "输入信息不能为空，请检查！");
			return retMap;
		}
		if (!check_code.equals(sessionCheckCode)) {
			retMap.put("error", "1");
			retMap.put("msg", "验证码不正确，请检查！");
			return retMap;
		}
		Map<String, Object> userMap = cglxDao.getUserByUserId(user_id);
		if (userMap == null || userMap.isEmpty()) {
			retMap.put("error", "1");
			retMap.put("msg", "该号码还没有注册，请先注册！");
			return retMap;
		} else {
			if (cglxDao.updateUserPwdByUserId(user_id, UserPwdMD5Encrypt.getPasswordByMD5Encrypt(user_pwd))) {
				retMap.put("error", "0");
				retMap.put("msg", "更改成功！");
				return retMap;
			} else {
				retMap.put("error", "1");
				retMap.put("msg", "更改密码失败，请稍后重试！");
				return retMap;
			}
		}
	}

	@RequestMapping("/user/userGetCheckCode")
	@ResponseBody
	public Map<String, String> userGetCheckCode(HttpSession httpSession, HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String phone = request.getParameter("user_id");
		sendSMSCode(retMap, phone);
		if (retMap.containsKey("send_status") && ("success").equals(retMap.get("send_status"))) {
			retMap.put("error", "0");
			retMap.put("msg", "验证码发送成功！");
			httpSession.setAttribute("check_code", retMap.get("check_code"));
		} else {
			retMap.put("error", "1");
			retMap.put("msg", "服务器暂时不可用，请稍后重试！");
		}
		return retMap;
	}

	@RequestMapping("/user/userLogout")
	public String userLogout(HttpSession httpSession, HttpServletRequest request) {
		httpSession.removeAttribute("user_id");
		httpSession.removeAttribute("user_type");
		httpSession.removeAttribute("is_admin");
		String htmlView = request.getParameter("view");
		return "redirect:/view/" + htmlView + ".html";
	}

	@RequestMapping("/user/userLogoutAjax")
	@ResponseBody
	public Map<String, String> userLogoutAjax(HttpSession httpSession, HttpServletRequest request) {
		httpSession.removeAttribute("user_id");
		httpSession.removeAttribute("user_type");
		httpSession.removeAttribute("is_admin");
		return null;
	}

	@RequestMapping("/user/getUserInfo")
	@ResponseBody
	public Map<String, Object> getUserInfo(HttpSession httpSession, HttpServletRequest request) {
		String user_id = (String) httpSession.getAttribute("user_id");
		if (user_id == null || user_id.isEmpty()) {
			return null;
		} else {
			Map<String, Object> retMap = cglxDao.getUserByUserId(user_id);
			if (retMap != null)
				retMap.remove("password");
			return retMap;
		}
	}
	
	@RequestMapping("/user/getAllUserInfo")
	@ResponseBody
	public List<Map<String, Object>> getAllUserInfo(HttpSession httpSession, HttpServletRequest request) {
		String user_id = (String) httpSession.getAttribute("user_id");
		String isAdmin = (String) httpSession.getAttribute("is_admin");
		if (user_id == null || user_id.isEmpty() || isAdmin == null || isAdmin.isEmpty()) {
			return null;
		} else {
			List<Map<String, Object>> retMap = cglxDao.getAllUser(0, 10000);
			if (retMap != null)
				retMap.remove("password");
			return retMap;
		}
	}
	
	@RequestMapping("/user/deleteUserInfo")
	@ResponseBody
	public Map<String, Object> deleteUserInfo(HttpSession httpSession, HttpServletRequest request) {
		String user_id = (String) httpSession.getAttribute("user_id");
		String isAdmin = (String) httpSession.getAttribute("is_admin");
		String phone = request.getParameter("phone");
		if (user_id != null && !user_id.isEmpty() && isAdmin != null && !isAdmin.isEmpty() &&
				phone != null && !phone.isEmpty()) {
			cglxDao.deleteUserById(phone);
		}
		return null;
	}

	@RequestMapping("/user/userRegister")
	@ResponseBody
	public Map<String, String> userRegister(HttpSession httpSession, HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String user_id = request.getParameter("user_id");
		String user_pwd = request.getParameter("user_pwd");
		String check_code = request.getParameter("check_code");
		String sessionCheckCode = (String) httpSession.getAttribute("check_code");
		if (user_id == null || user_pwd == null || check_code == null) {
			retMap.put("error", "1");
			retMap.put("msg", "输入信息不能为空，请检查！");
			return retMap;
		}
		if (!check_code.equals(sessionCheckCode)) {
			retMap.put("error", "1");
			retMap.put("msg", "验证码不正确，请检查！");
			return retMap;
		}
		Map<String, Object> userMap = cglxDao.getUserByUserId(user_id);
		if (userMap != null && ((String) userMap.get("user_id")).equals(user_id)) {
			retMap.put("error", "1");
			retMap.put("msg", "该手机号码已注册，请直接登陆！");
			return retMap;
		}
		String encryptPwd = UserPwdMD5Encrypt.getPasswordByMD5Encrypt(user_pwd);
		if (cglxDao.addUser(user_id, encryptPwd)) {
			retMap.put("error", "0");
			retMap.put("msg", "注册成功，请登录！");
		} else {
			retMap.put("error", "1");
			retMap.put("msg", "写数据库失败！");
		}
		return retMap;
	}

	@RequestMapping("/user/userLogin")
	@ResponseBody
	public Map<String, String> userLogin(HttpSession httpSession, HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String user_id = request.getParameter("user_id");
		String user_pwd = request.getParameter("user_pwd");
		if (user_id == null || user_pwd == null || user_id.isEmpty() || user_pwd.isEmpty()) {
			retMap.put("error", "1");
			retMap.put("error_msg", "账号或密码为空，请检查！");
			return retMap;
		}
		Map<String, Object> userMap = cglxDao.getUserByUserId(user_id);
		if (userMap != null) {
			if (UserPwdMD5Encrypt.getPasswordByMD5Encrypt(user_pwd).equals((String) userMap.get("password"))) {
				retMap.put("error", "0");
				retMap.put("error_msg", "");
				retMap.put("user_id", user_id);
				retMap.put("user_type", "");
				httpSession.setAttribute("user_id", user_id);
				if(("1").equals(String.valueOf(userMap.get("user_type")))) {
					httpSession.setAttribute("is_admin", "true");
					retMap.put("user_type", "1");
				}
				return retMap;
			} else {
				retMap.put("error", "1");
				retMap.put("error_msg", "您输入的密码不正确，请检查！");
				return retMap;
			}
		} else {
			retMap.put("error", "1");
			retMap.put("error_msg", "该手机号码还没有注册，请先注册！");
			return retMap;
		}
	}

	@RequestMapping("/user/userRegisterDetailInfo")
	@ResponseBody
	public Map<String, String> userRegisterDetailInfo(HttpSession httpSession, HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String user_id = (String) request.getSession().getAttribute("user_id");
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (user_id == null || user_id.isEmpty()) {
			retMap.put("error_code", "1");
			retMap.put("error_msg", "当前会话已超时，请重新进入！");
			return retMap;
		}
		paramsMap.put("user_id", new String[] { user_id });
		if (cglxDao.addUserDetailInfo(paramsMap)) {
			retMap.put("error_code", "0");
			retMap.put("error_msg", "提交个人信息成功！");

		} else {
			retMap.put("error_code", "1");
			retMap.put("error_msg", "服务器暂时不可用，请稍后重试！");
		}
		return retMap;
	}

	@RequestMapping("/user/getUserEvalInfo")
	@ResponseBody
	public Map<String, Object> getUserEvalInfo(HttpSession httpSession, HttpServletRequest request) {
		String user_id = (String) httpSession.getAttribute("user_id");
		if (user_id == null || user_id.isEmpty()) {
			return null;
		} else {
			Map<String, Object> retMap = cglxDao.getUserEvalByUserId(user_id);
			return retMap;
		}
	}

	@RequestMapping("/user/updateUserEvalInfo")
	@ResponseBody
	public Map<String, String> updateUserEvalInfo(HttpSession httpSession, HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String user_id = (String) request.getSession().getAttribute("user_id");
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (user_id == null || user_id.isEmpty()) {
			retMap.put("error_code", "1");
			retMap.put("error_msg", "当前会话已超时，请重新进入！");
			return retMap;
		}
		paramsMap.put("user_id", new String[] { user_id });
		if (cglxDao.updateUserEvalByUserId(paramsMap)) {
			retMap.put("error_code", "0");
			retMap.put("error_msg", "提交评估信息成功！");

		} else {
			retMap.put("error_code", "1");
			retMap.put("error_msg", "服务器暂时不可用，请稍后重试！");
		}
		return retMap;
	}
	
	

	private void sendSMSCode(Map<String, String> retMap, String phone) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://106.ihuyi.com/webservice/sms.php?method=Submit");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
		int mobile_code = (int) ((Math.random() * 9 + 1) * 1000);
		retMap.put("check_code", String.valueOf(mobile_code));
		String content = new String("欢迎您注册DIY研习社，验证码：" + mobile_code + "。请勿将验证码泄露他人。");
		NameValuePair[] data = { // 提交短信
				new NameValuePair("account", "cf_ckzsnow"),
				new NameValuePair("password", "52d39430ddb918ddc0f57d5fc6717b24"), // 密码可以使用明文密码或使用32位MD5加密
				new NameValuePair("mobile", phone), new NameValuePair("content", content), };
		method.setRequestBody(data);
		try {
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();
			String code = root.elementText("code");
			if ("2".equals(code)) {
				retMap.put("send_status", "success");
			} else {
				retMap.put("send_status", "fail");
			}
		} catch (IOException e) {
			logger.error(e.toString());
		} catch (DocumentException e) {
			logger.error(e.toString());
		}
	}

	// cases
	@RequestMapping("/cases/getRecommendCase")
	@ResponseBody
	public List<Map<String, Object>> getRecommendCase(HttpServletRequest request) {
		return cglxDao.getRecommendCaseLimit();
	}

	@RequestMapping("/cases/getCaseListByTag")
	@ResponseBody
	public List<Map<String, Object>> getCaseListByTag(HttpServletRequest request) {
		String page = request.getParameter("page");
		String length = request.getParameter("length");
		String categoryTag = request.getParameter("category_tag");
		String countryTag = request.getParameter("country_tag");
		int page_ = 1;
		int length_ = 5;
		try {
			page_ = Integer.valueOf(page);
			length_ = Integer.valueOf(length);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getCaseListByTag(categoryTag, countryTag, (page_ - 1) * length_, length_);
	}
	
	@RequestMapping("/cases/getRelatedCase")
	@ResponseBody
	public List<Map<String, Object>> getRelatedCase(HttpServletRequest request) {
		String page = request.getParameter("page");
		String length = request.getParameter("length");
		String categoryTag = request.getParameter("category_tag");
		String countryTag = request.getParameter("country_tag");
		int page_ = 1;
		int length_ = 5;
		try {
			page_ = Integer.valueOf(page);
			length_ = Integer.valueOf(length);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getRelatedCase(categoryTag, countryTag, (page_ - 1) * length_, length_);
	}

	@RequestMapping("/cases/getCaseById")
	@ResponseBody
	public Map<String, Object> getCaseById(HttpServletRequest request) {
		String id = request.getParameter("id");
		int id_ = 0;
		try {
			id_ = Integer.valueOf(id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getCaseById(id_);
	}
	
	@RequestMapping("/cases/getCaseDetail")
	@ResponseBody
	public Map<String, Object> getCaseDetail(HttpServletRequest request) {
		String id = request.getParameter("id");
		int id_ = 0;
		try {
			id_ = Integer.valueOf(id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getCaseDetail(id_);
	}
	
	@RequestMapping("/articles/addCaseTotal")
	@ResponseBody
	public Map<String, String> addCaseTotal(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String case_id = request.getParameter("case_id");
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		System.out.println(paramsMap.toString());
		Map<String, String[]> caseParamsMap = new HashMap<>();
		Map<String, String[]> caseDetailParamsMap = new HashMap<>();
		
		String indexStr = request.getParameter("index");
		int index = -1;
		try{
			index = Integer.valueOf(indexStr);
		} catch(Exception e) {
			logger.error(e.toString());
		}
		caseParamsMap.put("case_id", paramsMap.get("case_id"));
		caseParamsMap.put("tag_category_name", paramsMap.get("tag_category_name"));
		caseParamsMap.put("tag_country_name", paramsMap.get("tag_country_name"));
		caseParamsMap.put("title", paramsMap.get("title"));
		caseParamsMap.put("sub_title", paramsMap.get("sub_title"));
		caseParamsMap.put("is_recommend", paramsMap.get("recommend"));
		caseParamsMap.put("is_nav_recommend", paramsMap.get("nav_recommend"));
		
		caseDetailParamsMap.put("case_id", paramsMap.get("case_id"));
		caseDetailParamsMap.put("title", paramsMap.get("title"));
		caseDetailParamsMap.put("background_name", paramsMap.get("background_name"));
		caseDetailParamsMap.put("background_major", paramsMap.get("background_major"));
		caseDetailParamsMap.put("background_college", paramsMap.get("background_college"));
		caseDetailParamsMap.put("background_apply_major", paramsMap.get("background_apply_major"));
		caseDetailParamsMap.put("background_apply_college", paramsMap.get("background_apply_college"));
		caseDetailParamsMap.put("background_enroll_time", paramsMap.get("background_enroll_time"));
		caseDetailParamsMap.put("background_enter_time", paramsMap.get("background_enter_time"));
		caseDetailParamsMap.put("background_info", paramsMap.get("background_info"));
		caseDetailParamsMap.put("train_target", paramsMap.get("train_target"));
		//caseDetailParamsMap.put("major_info", paramsMap.get("major_info"));
		caseDetailParamsMap.put("major_chi", paramsMap.get("major_chi"));
		caseDetailParamsMap.put("major_eng", paramsMap.get("major_eng"));
		
		
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
		
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
				case 0: caseParamsMap.put("image", new String[] { fileName });
				break;
				case 1: caseDetailParamsMap.put("image", new String[] { fileName });
				break;
				/*case 2: caseDetailParamsMap.put("college_image", new String[] { fileName });
					break;	*/
				}
				count ++;
			}
		}
		if(case_id == null || case_id.isEmpty() || ("null").equals(case_id)) {
			long id_ = cglxDao.addCase(caseParamsMap);
			caseDetailParamsMap.put("case_id", new String[]{String.valueOf(id_)});
			cglxDao.addCaseDetail(caseDetailParamsMap);
		} else {
			cglxDao.updateCase(caseParamsMap);
			cglxDao.updateCaseDetail(caseDetailParamsMap);
		}
		retMap.put("msg", "添加成功！");
		retMap.put("error", "0");
		return retMap;
	}
	
	@RequestMapping("/cases/addCase")
	@ResponseBody
	public Map<String, String> addCase(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/files/imgs";
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				String fileName = getGernarateFileName(multifile);
				try {
					FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
					Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
					paramsMap.put("image", new String[] { fileName });
					cglxDao.addCase(paramsMap);
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
			}
			retMap.put("msg", "添加成功！");
			retMap.put("error", "0");
			return retMap;
		} else {
			retMap.put("msg", "上传文件为空！");
			retMap.put("error", "1");
			return retMap;
		}
	}

	@RequestMapping("/cases/updateCase")
	@ResponseBody
	public Map<String, String> updateCase(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
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
			}
		}
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (fileName != null && !fileName.isEmpty())
			paramsMap.put("image", new String[] { fileName });
		cglxDao.updateCase(paramsMap);
		retMap.put("msg", "更新成功！");
		retMap.put("error", "0");
		return retMap;
	}

	@RequestMapping("/cases/deleteCase")
	@ResponseBody
	public Map<String, String> deleteCase(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String id = request.getParameter("id");
		try {
			if (cglxDao.deleteCase(Long.valueOf(id))) {
				retMap.put("msg", "删除成功！");
				retMap.put("error", "0");
				return retMap;
			} else {
				retMap.put("msg", "删除失败！");
				retMap.put("error", "1");
				return retMap;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			retMap.put("msg", ex.toString());
			retMap.put("error", "1");
			return retMap;
		}
	}

	@RequestMapping("/cases/addCaseDetail")
	@ResponseBody
	public Map<String, String> addCaseDetail(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String realPath = "/data/cglx/files/imgs";
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				String fileName = getGernarateFileName(multifile);
				try {
					FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
					paramsMap.put("image", new String[] { fileName });
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
			}
		}
		if (cglxDao.addCaseDetail(paramsMap) > 0) {
			retMap.put("msg", "添加成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "写入数据库失败！");
			retMap.put("error", "1");
		}
		return retMap;
	}

	@RequestMapping("/cases/updateCaseDetail")
	@ResponseBody
	public Map<String, String> updateCaseDetail(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		String realPath = "/data/cglx/files/imgs";
		String fileName = "";
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
			}
		}
		if (fileName != null && !fileName.isEmpty())
			paramsMap.put("image", new String[] { fileName });
		if (cglxDao.updateCaseDetail(paramsMap)) {
			retMap.put("msg", "更新成功！");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "写入数据库失败！");
			retMap.put("error", "1");
		}
		return retMap;
	}

	@RequestMapping("/cases/getNavRecommend")
	@ResponseBody
	public List<Map<String, Object>> getNavRecommend(HttpServletRequest request) {
		return cglxDao.getNavRecommend();
	}
	
	// medias
	@RequestMapping("/medias/getRecommendMedia")
	@ResponseBody
	public List<Map<String, Object>> getRecommendMedia(HttpServletRequest request) {
		String id = request.getParameter("id");
		int id_ = 0;
		try {
			id_ = Integer.valueOf(id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getRecommendMedia(id_);
	}

	@RequestMapping("/medias/getMediaList")
	@ResponseBody
	public List<Map<String, Object>> getMediaList(HttpServletRequest request) {
		String page = request.getParameter("page");
		String length = request.getParameter("length");
		int page_ = 1;
		int length_ = 5;
		try {
			page_ = Integer.valueOf(page);
			length_ = Integer.valueOf(length);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getMediaList((page_ - 1) * length_, length_);
	}

	@RequestMapping("/medias/getMediaById")
	@ResponseBody
	public Map<String, Object> getMediaById(HttpServletRequest request) {
		String id = request.getParameter("id");
		int id_ = 0;
		try {
			id_ = Integer.valueOf(id);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return cglxDao.getMediaById(id_);
	}

	@RequestMapping("/medias/addMedia")
	@ResponseBody
	public Map<String, String> addMedia(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String flag = request.getParameter("flag");
		String fileNameListStr = request.getParameter("fileNameList");
		String[] fileNames = fileNameListStr.split(";");
		String realPath = "/data/cglx/files/imgs";
		String notContentImagefileName = "";
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		paramsMap.put("image", new String[] { "" });
		int index = 0;
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				String fileName = "";
				try {
					if(("1").equals(flag)) {
						notContentImagefileName = getGernarateFileName(multifile);
						FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, notContentImagefileName));
						flag = "0";
						paramsMap.put("image", new String[] { notContentImagefileName });
					} else {
						fileName = fileNames[index];
						index++;
						FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
					}
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
			}
			retMap.put("msg", "添加成功！");
			cglxDao.addMedia(paramsMap);
			retMap.put("error", "0");
			return retMap;
		} else {
			retMap.put("msg", "上传文件为空！");
			retMap.put("error", "1");
			return retMap;
		}
	}

	@RequestMapping("/medias/updateMedia")
	@ResponseBody
	public Map<String, String> updateMedia(DefaultMultipartHttpServletRequest multipartRequest,
			HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String flag = request.getParameter("flag");
		String fileNameListStr = request.getParameter("fileNameList");
		String[] fileNames = fileNameListStr.split(";");
		String realPath = "/data/cglx/files/imgs";
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		paramsMap.remove("flag");
		paramsMap.remove("fileNameList");
		int index = 0;
		String notContentImagefileName = "";
		if (multipartRequest != null) {
			Iterator<String> iterator = multipartRequest.getFileNames();
			while (iterator.hasNext()) {
				MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
				try {
					if(("1").equals(flag)) {
						notContentImagefileName = getGernarateFileName(multifile);
						FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, notContentImagefileName));
						flag = "0";
						paramsMap.put("image", new String[] { notContentImagefileName });
					} else {
						String fileName = fileNames[index];
						index++;
						FileUtils.copyInputStreamToFile(multifile.getInputStream(), new File(realPath, fileName));
					}
					
				} catch (IOException e) {
					logger.error("Failed in saving file, exception : {}", e.toString());
				}
			}
		}
		cglxDao.updateMedia(paramsMap);
		retMap.put("msg", "更新成功！");
		retMap.put("error", "0");
		return retMap;
	}

	@RequestMapping("/medias/deleteMedia")
	@ResponseBody
	public Map<String, String> deleteMedia(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String id = request.getParameter("id");
		try {
			if (cglxDao.deleteMedia(Long.valueOf(id))) {
				retMap.put("msg", "删除成功！");
				retMap.put("error", "0");
				return retMap;
			} else {
				retMap.put("msg", "删除失败！");
				retMap.put("error", "1");
				return retMap;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			retMap.put("msg", ex.toString());
			retMap.put("error", "1");
			return retMap;
		}
	}

	@RequestMapping("/search/searchArticleAndMedia")
	@ResponseBody
	public List<Map<String, Object>> searchArticleAndMedia(HttpServletRequest request) {
		String key = request.getParameter("key");
		if (key == null)
			key = "";
		return cglxDao.searchArticleAndMedia(key);
	}
	
	@RequestMapping("/applyReport/getAllApplyReport")
	@ResponseBody
	public List<Map<String, Object>> getAllApplyReport(HttpServletRequest request) {
		return cglxDao.getAllApplyReport();
	}
	
	@RequestMapping("/applyReport/updateApplyReport")
	@ResponseBody
	public Map<String, String> updateApplyReport(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		Map<String, String[]> paramsMap = new HashMap<>(request.getParameterMap());
		if (cglxDao.updateApplyReport(paramsMap)) {
			retMap.put("msg", "更新成功");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "写入数据库失败！");
			retMap.put("error", "1");
		}
		return retMap;
	}
	
	@RequestMapping("/applyReport/deleteApplyReport")
	@ResponseBody
	public Map<String, String> deleteApplyReport(HttpServletRequest request) {
		Map<String, String> retMap = new HashMap<>();
		String id = request.getParameter("id");
		try {
			if (cglxDao.deleteApplyReport(Long.valueOf(id))) {
				retMap.put("msg", "删除成功！");
				retMap.put("error", "0");
				return retMap;
			} else {
				retMap.put("msg", "删除失败！");
				retMap.put("error", "1");
				return retMap;
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			retMap.put("msg", ex.toString());
			retMap.put("error", "1");
			return retMap;
		}
	}
	
	@RequestMapping("/eval/getAllEval")
	@ResponseBody
	public List<Map<String, Object>> getAllEval(HttpServletRequest request) {
		return cglxDao.getAllEval();
	}
	
	@RequestMapping("/eval/updateEval")
	@ResponseBody
	public Map<String, String> updateEval(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map<String, String> retMap = new HashMap<>();
		if(id == null || id.isEmpty()) {
			retMap.put("msg", "id为空");
			retMap.put("error", "1");
			return retMap;
		}
		if(cglxDao.updateEval(new HashMap<>(request.getParameterMap()))) {
			retMap.put("msg", "提交报告成功");
			retMap.put("error", "0");
		} else {
			retMap.put("msg", "写数据库失败！");
			retMap.put("error", "1");
		}
		return retMap;
	}
	
	@RequestMapping("/tag/getAllTag")
	@ResponseBody
	public List<Map<String, Object>> getAllTag(HttpServletRequest request) {
		return cglxDao.getAllTag();
	}
	
}