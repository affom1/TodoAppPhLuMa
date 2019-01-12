package org.todo.servlets;

import org.todo.business.SaveHelper;
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

@WebServlet("/UpdateTodoWithInputs.do")
public class UpdateTodoWithInputsServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ServletContext sc;

    public void init () {


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");

        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        boolean important = false;
        String date = request.getParameter("dueDate");

        if (request.getParameter("important")!=null) {
            important=true;
        }

        // update of Todos and save them.
        for (int i = 0;i<currentUser.getTodoList().size();i++) {
            if (currentUser.getTodoList().get(i).getId() == id) {
                currentUser.getTodoList().get(i).updateEverythingButId(title, category,date,important);
            }
        }
        ServletContext sc = this.getServletContext();
        SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
        helper.saveUsers();

        // send him back to the List
        response.sendRedirect(request.getContextPath() + "/todoListNew.do");

    }
}
