package org.todo.servlets.RestServlets;



import org.todo.business.Todo;
import org.todo.business.TodoUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTMLDocument;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.todo.servlets.RestServlets.JaxbHelper.getJAXBContext;

@WebServlet("/api/todos")
public class RESTapiTodosGetPost extends HttpServlet {

    private HashMap<String, TodoUser> userHashMap;
    private TodoUser todoUser;
    private ArrayList<Todo> todoListe = new ArrayList<>();
    private String category =null;

    /**
     *
     * doGet gives from the currentUser from the AuthenticationFilter all Todos or only
     * the choosen Params Categroy="choosen category" to the Body of the Http Get in JASON.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // request the currentUser form AuthenticatinFilter
        TodoUser currentUser = (TodoUser) request.getAttribute("currentuser");
        System.out.println(currentUser + " das ist der aktuelle user der die todos aus der liste holt");

        // get the params from the REST GET, it is the paramter null then gives out null
        String categoryParam = request.getQueryString();
        System.out.println("hier ist die gewählte Kategorie " + categoryParam);

        //starts the Marshaller to change after the todos to Jason
        Marshaller marshaller = null;
        try {
            marshaller = getJAXBContext().createMarshaller();
        } catch (JAXBException e1) {
            response.sendError(406, "unsupported accept type");
            e1.printStackTrace();
        }
        // gives all todos to the marshaller
        if (categoryParam == null) {
            try {
                marshaller.marshal(currentUser.getTodoList(), response.getWriter());
                System.out.println("Es kommen alle Todos da category null");
                response.sendError(200, "the todos of the specified category, or all todos if no category is specified");
            } catch (JAXBException e) {
                response.sendError(406, "unsupported accept type");
                e.printStackTrace();
            }

            // only the todos of the choosen categores will be marshallerd to JASON
        } else
            try {
                // splits the Query to the category
                String category = categoryParam.split("=")[1];
                System.out.println("hier ist die gewählte Kategorie " + category);

                // saves the tods from the choosen category to a new list
                todoListe = null;
                todoListe = new ArrayList<>();
                for (Todo todo : currentUser.getTodoList()) {
                    if (todo.getCategory().equals(category)) {
                        todoListe.add(todo);
                    }
                }
                // prints the JASON to the Body of the HTTP Request
                marshaller.marshal(todoListe, response.getWriter());
                System.out.println("Es kommen nur die Todos der Katgeorie in parametern");
                response.sendError(200, "the todos of the specified category, or all todos if no category is specified");
            } catch (JAXBException e) {
                response.sendError(406, "unsupported accept type");
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
        Unmarshaller unmarshaller = null;
        try {
            unmarshaller = getJAXBContext().createUnmarshaller();
        } catch (JAXBException e) {
            System.out.println("getJAXBContext wurde abgefagen");
            response.sendError(400, "invalid todo date");
            e.printStackTrace();
        }
        try {
            todo = unmarshaller.unmarshal(new StreamSource(request.getInputStream()), Todo.class).getValue();

        } catch (JAXBException e) {
            System.out.println("todo InputStram todo.class wurde abgefangen");
            response.sendError(400, "invalid todo data");
            e.printStackTrace();
        }

        // Checks if tite is set because it is required
        if (todo.getTitle() == null) {
            response.sendError(406, "unsupported accept type");
            System.out.println("kein titel eingegeben");
            return;
        }


        // Before saving the ID must be the highest
        todo.setId(todoUser.determineHighestId());

        System.out.println(todo.getId());

        userHashMap.get(currentUser).addTodo(todo);
        response.sendError(200, "todo added");
        System.out.println("todo added");

        Marshaller marshaller = null;
        try {
            marshaller = getJAXBContext().createMarshaller();
        } catch (JAXBException e1) {
            System.out.println("konnte es nicht zurück geben getJAXBContext");
            e1.printStackTrace();
        }
        // gives all todos to the marshaller
        try {
            marshaller.marshal(todo, response.getWriter());
            System.out.println("geaded todos werden ausgeben");
        } catch (JAXBException e) {
            System.out.println("konnte die daten nciht ausgeben");
            e.printStackTrace();
        }
    }

}




