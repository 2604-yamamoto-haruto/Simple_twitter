package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/edit")
public class EditServlet extends HttpServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	 * デフォルトコンストラクタ
	 * アプリケーションの初期化を実施する。
	 */
    public EditServlet() {
    	InitApplication application = InitApplication.getInstance();
		application.init();
	}


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();
		boolean isShowMessageForm = true;

		String EditId = request.getParameter("Edit");
		String check = new MessageService().check(EditId);

		if(!EditId.matches("^[0-9]*$")) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}else if(!EditId.equals(check)) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		String message = new MessageService().message(EditId);

		request.setAttribute("text", message);
		request.setAttribute("id", EditId);
		request.setAttribute("isShowMessageForm", isShowMessageForm);
		request.getRequestDispatcher("/edit.jsp").forward(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		String EditId = request.getParameter("EditId");
		String EditText = request.getParameter("text");

		if (!isValid(EditText, errorMessages)) {
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./edit?Edit=" + EditId);
			return;
		}

		new MessageService().editMessage(EditId, EditText);
		response.sendRedirect("./");
	}


	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
			" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}

}
