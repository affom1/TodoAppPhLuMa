package org.todo.servlets;

import org.todo.business.Todo;
import org.todo.business.TodoUser;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

@WebServlet("/update.do")
public class UpdateTodoServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ServletContext sc;
    Todo currentTodo;


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");

        //get current Todo
        int id = Integer.parseInt(request.getParameter("update"));
        for (int i = 0;i<currentUser.getTodoList().size();i++) {
            if (currentUser.getTodoList().get(i).getId() == id) {
                currentTodo = currentUser.getTodoList().get(i);
            }
        }

        //save current Todos in the Session
        session.setAttribute("currentTodo", currentTodo);

        // Weiterleiten ans JSP
        response.sendRedirect(request.getContextPath() + "/UpdateTodo.jsp");

    }
}
