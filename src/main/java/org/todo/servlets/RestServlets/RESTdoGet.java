package org.todo.servlets.RestServlets;


import org.eclipse.persistence.exceptions.JAXBException;
import org.todo.business.Todo;
import org.todo.business.TodoUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/todos")
public class RESTdoGet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ArrayList<Todo> todoListe = new ArrayList<>();
    String choosenCategory;

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException{

        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");


        for (Todo todo : todoListe){
            System.out.println(todo.getId() + todo.getTitle() + todo.getCategory() + todo.getDueDate()); // fehlt important und completed
            try {
                String s = marshal(todo);
                System.out.println(s);
            } catch (javax.xml.bind.JAXBException e) {
                e.printStackTrace();
            }
        }

   }


    public static String marshal(Todo todo) throws JAXBException, javax.xml.bind.JAXBException {
        Writer writer = new StringWriter();
        Marshaller marshaller = getJAXBContext().createMarshaller();
        marshaller.marshal(todo, writer);
        return writer.toString();
    }

    public static Todo unmarshal(String s) throws JAXBException, javax.xml.bind.JAXBException {
        Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
        return unmarshaller.unmarshal(new StreamSource(new StringReader(s)), Todo.class).getValue();
    }

    private static JAXBContext getJAXBContext() throws JAXBException, javax.xml.bind.JAXBException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("eclipselink.media-type", "application/json");
        properties.put("eclipselink.json.include-root", false);
        return JAXBContext.newInstance(new Class[]{Todo.class}, properties);
    }
}




