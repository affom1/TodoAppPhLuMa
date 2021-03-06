package org.todo.servlets;

import org.todo.business.SaveHelper;
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

@WebServlet("/MarkUncompletedNew.do")
public class MarkUncompletedServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ServletContext sc;

    public void init () {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");

        // Todos mit entprechender ID als Uncmpleted markieren.
        int id = Integer.parseInt(request.getParameter("complete"));
        for (Todo todo : currentUser.getTodoList()) {
            if (todo.getId()== id) {
                todo.setCompleted(false);
            }
        }
        // speichern
        ServletContext sc = this.getServletContext(); // holt den ServletContext
        SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper"); // holt den SaveHelper aus dem Kontect, welche die UserHashmap als Attribute hat.
        helper.saveUsers(); // speichert sie in der User Datei

        // send him back to the List
        response.sendRedirect(request.getContextPath() + "/todoListNew.do");

    }
}
