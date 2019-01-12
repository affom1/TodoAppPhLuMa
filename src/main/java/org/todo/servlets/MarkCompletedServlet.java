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
import java.util.ArrayList;

@WebServlet("/MarkCompletedNew.do")
public class MarkCompletedServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ServletContext sc;

    public void init () {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");

        // Todos mit entprechender ID als Completed markieren.
        int id = Integer.parseInt(request.getParameter("complete"));
        System.out.println("Wir setzen auf kompletiert bei Element"+ id);

        for (Todo todo : currentUser.getTodoList()) {
            if (todo.getId()== id) {
                todo.setCompleted(true);
            }
        }

        // send him back to the List
        response.sendRedirect(request.getContextPath() + "/todoListNew.do");
    }
}
