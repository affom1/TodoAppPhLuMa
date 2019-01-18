package org.todo.servlets.RestServlets;



import org.todo.business.Todo;
import org.todo.business.TodoUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.HashMap;

import static org.todo.servlets.RestServlets.JaxbHelper.getJAXBContext;

@WebServlet("/api/todos")
public class RESTapiTodosGetPost extends HttpServlet {

    private HashMap<String, TodoUser> userHashMap;
    //private ServletContext sc;
     private TodoUser todoUser;

    public void init() {

     //  userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
     //   sc = this.getServletContext();
    }

    //print the todos of de current user thrue REST Interface
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        TodoUser currentUser = (TodoUser) request.getAttribute("currentuser");
        System.out.println(currentUser + " das ist der aktuelle user der die todos aus der liste holt");



         Marshaller marshaller = null;
        try {
            marshaller = getJAXBContext().createMarshaller();
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
        try {
            marshaller.marshal(currentUser.getTodoList(), response.getWriter());
            System.out.println("sollte gekommen sein");

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    // create a resource
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("neue Todos werden nun in die Liste geladen!");
        TodoUser currentUser = null;
        currentUser = (TodoUser) request.getAttribute("currentuser");
        System.out.println(currentUser + " das ist der aktuelle user der neue  todos in die liste gibt");

        Todo todo = null;

        try {
            Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
            todo = unmarshaller.unmarshal(new StreamSource(request.getInputStream()), Todo.class).getValue();

        } catch (JAXBException e) {
            System.out.println("konnte nicht unmarshallen");
            response.sendError(400, "invalid todo date");
            e.printStackTrace();
        }
        if (todo.getTitle() == null) {
            response.sendError(406,"unsupported accept type" );
            System.out.println("kein titel eingegeben");
        }

        todo.setId(todoUser.determineHighestId());

        System.out.println(todo);

        userHashMap.get(currentUser).addTodo(todo);
        response.sendError(200, "todo added");
        System.out.println("todo added");

        // kommt noch ausgabe hin.. zurück in boddy

    }

}




