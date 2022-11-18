package webapp.rating;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemoveStudent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String name = request.getParameter("studentName");
		Connection conn = JDBCConnection.getConnection();
		PrintWriter out = response.getWriter();
		out.println("<head>\r\n"
				+ "<meta charset=\"ISO-8859-1\">\r\n"
				+ "<title>Remove Student Data</title>\r\n"
				+ "<style type=\"text/css\">\r\n"
				+ ".h2_1{\r\n"
				+ "color: black;\r\n"
				+ "font-weight: bold;\r\n"
				+ "text-decoration: none;\r\n"
				+ "text-align: center;\r\n"
				+ "}\r\n"
				+ "form{\r\n"
				+ "	border: 3px solid #f1f1f1;\r\n"
				+ "}\r\n"
				+ "button{\r\n"
				+ "	background-color: black;\r\n"
				+ "	color: white;\r\n"
				+ "	padding: 14px 20px;\r\n"
				+ "	margin: 8px 0;\r\n"
				+ "	border: none;\r\n"
				+ "	cursor: pointer;\r\n"
				+ "	width: 100%;\r\n"
				+ "}\r\n"
				+ "button:hover{\r\n"
				+ "	opacity: 0.9;\r\n"
				+ "}\r\n"
				+ "</style>");
		String str = App.deleteData(conn, name);
		out.println("<body style=\"background-color: buttonface;\">\r\n"
				+ "	<div class = \"h2_1\">\r\n"
				+ "	<h1>Artha Educational Institute</h1>\r\n"
				+ "	</div>\r\n"
				+ "	<br>\r\n"
				+ "	<h2 style=\"text-align: center;\">Message: " + str + "</h2>\r\n"
				+ "	<br>");
		out.print("<br>");
		out.println("<form action=\"http://localhost:8081/Java WebApp/Index.html\"><button type = \"submit\">Go Home</button></form></body>");
	}

}
