package org.todo.servlets;

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

@WebServlet("/DeleteNew.do")
public class DeleteTodoNewServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ServletContext sc;

    public void init () {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");

        // Todos mit entprechender aus Userliste löschen.
        String id2 = request.getParameter("delete");
        int id = Integer.parseInt(id2);

        for (int i = 0;i<currentUser.getTodoList().size();i++) {
            if (currentUser.getTodoList().get(i).getId() == id) {
                currentUser.getTodoList().remove(i);
                System.out.println("Wir löschen Element: "+ id);
            }

        }

        // send him back to the List
        response.sendRedirect(request.getContextPath() + "/todoListNew.do");

    }
}
