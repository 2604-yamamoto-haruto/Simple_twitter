package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(urlPatterns = {"/setting", "/edit"})
public class LoginFilter implements Filter {

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest loginRequest = (HttpServletRequest) request;
		HttpServletResponse sendResponse = (HttpServletResponse) response;

		HttpSession session = loginRequest.getSession(false);
		List<String> errorMessages = new ArrayList<String>();


		if(session != null && session.getAttribute("loginUser") == null) {
			errorMessages.add("ログインしてください");
			session.setAttribute("errorMessages", errorMessages);
			sendResponse.sendRedirect("login.jsp");
			return;
		}
		chain.doFilter(request, response); // サーブレットを実行
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
}
