package org.todo.servlets;

import org.todo.business.TodoUser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/newTodo.do")
public class CreateTodoServlet extends HttpServlet {
    TodoUser currentUser;


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get User just for name on JSP.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");
        // and send him to where to create a new Todo
        response.sendRedirect(request.getContextPath() + "/CreateTodo_2.jsp");
    }
}
