package org.todo.servlets.RestServlets;


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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.todo.servlets.RestServlets.JaxbHelper.getJAXBContext;

@WebServlet("/api/todos")
public class RESTapiTodosGetPost extends HttpServlet {

    HashMap<String, TodoUser> userHashMap;


    ServletContext sc;

    public void init() {
        sc = this.getServletContext();
        userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            try {
               String username = (String)   sc.getAttribute("currentUser");
            if (username == null) {
                response.sendError(401, "user not autorized");
            }
            username = String.valueOf(userHashMap.get(username));
                System.out.println(username);
            Marshaller marshaller = getJAXBContext().createMarshaller();
     //       marshaller.marshal(username.  getTodoList(), response.getWriter());


        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    // create a resource
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("test todos");
        TodoUser currentUser = null;
        // session holen..
        try {
            HttpSession session = request.getSession();
            currentUser = userHashMap.get(session.getAttribute("currentUser"));
        } catch (Exception e) {
            response.sendError(401, "user not authorized");
            e.printStackTrace();
        }
        // ACHTUNG Title muss mandetory sein...
        // Todos erstellen, gemäss Input aus Jason
        Todo todo = null;
        try {
            Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
            todo = unmarshaller.unmarshal(new StreamSource(request.getInputStream()), Todo.class).getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        currentUser.addTodo(todo);


//            boolean important = false;
//            String title = null;
//            String category = null;
//            String date = null;
//            try {
//                title = request.getParameter("title");
//                category = request.getParameter("category");
//                important = Boolean.parseBoolean(request.getParameter("important"));
//                date = request.getParameter("dueDate");
//            } catch (Exception e) {
//                response.sendError(400, "invalid todo data");
//                e.printStackTrace();
//                System.out.println("hat nicht funktionert");
//            }
//            // creation of Todos and save them.
//            currentUser.addTodo(determineHighestId() + 1, title, category, date, important, false);
//            ServletContext sc = this.getServletContext();
//            SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
//            helper.saveUsers();
//
//            // send him back to the List
//            response.sendError(201, "todo added");


    }


}




