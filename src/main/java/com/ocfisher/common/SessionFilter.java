package com.ocfisher.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionFilter extends OncePerRequestFilter {

	/*private static final Logger logger = LoggerFactory
			.getLogger(SessionFilter.class);*/
	
	private String[] filterUrls;

	public SessionFilter() {
		filterUrls = new String[] { "/view/personalInfo.html", "/view/collection.html", "/view/estimate.html"};
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String uri = request.getRequestURI();
		boolean doFilter = false;
		boolean doFilterAdmin = false;
		for (String url : filterUrls) {
			if (url.equals(uri)) {
				doFilter = true;
			}
			if(uri.contains("/background/")) {
				doFilterAdmin = true;
				doFilter = true;
			}
		}
		if(uri.indexOf("dispatcher-servlet.xml") != -1 || uri.indexOf("web.xml") != -1 ||
				uri.indexOf("log4j.properties") != -1) {
			boolean isAjaxRequest = isAjaxRequest(request);
			if (isAjaxRequest) {
				response.setCharacterEncoding("UTF-8");
				response.sendError(HttpStatus.UNAUTHORIZED.value(),
						"Unauthorized!");
				return;
			}
			response.sendRedirect("/");
			return;
		}
		if (doFilter) {
			String userId = (String)request.getSession().getAttribute("user_id");
			String isAdmin = (String)request.getSession().getAttribute("is_admin");
			if (null == userId || userId.isEmpty()) {
				logger.debug("user id is null.");
				boolean isAjaxRequest = isAjaxRequest(request);
				if (isAjaxRequest) {
					response.setCharacterEncoding("UTF-8");
					response.sendError(HttpStatus.UNAUTHORIZED.value(),
							"Unauthorized!");
					return;
				}
				response.sendRedirect("/");
				return;
			}
			if(doFilterAdmin && (isAdmin == null || isAdmin.isEmpty())) {
				response.sendRedirect("/");
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equals(header))
			return true;
		else
			return false;
	}

}