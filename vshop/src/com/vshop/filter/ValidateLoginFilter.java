package com.vshop.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ValidateLoginFilter implements Filter {

	private ServletContext servletContext;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		HttpServletResponse resp = (HttpServletResponse) response;
		
		// 1.可以进行配置(用户访问的页面：web.xml配置)

		// 获取用户登录的页面地址
		String login_page = servletContext.getInitParameter("login_page");
		// System.out.println("登录页面："+login_page);
		// 获取用户需要登录才能访问的地址
		String validate_page = servletContext.getInitParameter("validate_page");
		// 获取用户不需要登录就能访问的地址
		String common_page = servletContext.getInitParameter("common_page");
		String current_page = req.getServletPath();

		if (common_page.indexOf(current_page) != -1) {
			chain.doFilter(request, response);
		} else if (validate_page.indexOf(current_page) != -1
				&& session.getAttribute("username") != null) {
			chain.doFilter(request, response);
		} else {
			resp.sendRedirect(req.getContextPath() + login_page);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		servletContext = filterConfig.getServletContext();
	}

}
