package org.todo.servlets;

import org.todo.business.Todo;
import org.todo.business.TodoUser;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;


@WebServlet("/newTodoWithInputs.do")
public class CreateTodoWithInputsServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ServletContext sc;

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");

        // Todos erstellen, gemäss Input aus HTML
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        boolean important = false;
        String stringImportant = request.getParameter("important");
        String date = request.getParameter("dueDate");
        // important ist einfach Null wenn nicht angekreut. Mühsam...

        System.out.println(title);
        System.out.println(category);
        System.out.println(stringImportant);
        System.out.println("Datum?");
        System.out.println(date);

        try {
            stringImportant = request.getParameter("important");
            if (stringImportant.equals("on")) important = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        // creation of Todos and save them.
        currentUser.addTodo(determineHighestId()+1, title, category, date, important, false);

        // send him back to the List
        response.sendRedirect(request.getContextPath() + "/todoListNew.do");
    }

    private int determineHighestId() {
        int highest = 0; // wenn noch kein Todos exisitert wird die ID 0.
        for (Todo todo : currentUser.getTodoList()) {
            if (todo.getId() > highest) highest = todo.getId();
        }
        return highest;
    }
}
