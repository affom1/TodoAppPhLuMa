package org.todo.examples;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/hello.do")
public class HelloServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String name = request.getParameter("name");
		if (name == null || name.isEmpty()) {
			name = "Servlet";
		}
		String greeting = "Hello " + name;
		response.setContentType("text/html");
		try (PrintWriter out = response.getWriter()) {
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("<head>");
			out.println("  <title>Hello Servlet</title>");
			out.println("  <style>* { font-family: sans-serif }</style>");
			out.println("</head>");
			out.println("<body>");
			out.println("  <h1>" + greeting + "</h1>");
			out.println("  <form action='hello.do' method='get'>");
			out.println("    Your Name: <input type='text' name='name'/>");
			out.println("    <br/><br/>");
			out.println("    <input type='submit' value='Say Hello'/>");
			out.println("  </form>");
			out.println("</body>");
			out.println("</html>");
		}
	}
}
