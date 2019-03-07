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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static org.todo.servlets.RestServlets.JaxbHelper.getJAXBContext;

@WebServlet("/api/categories")
public class RESTgetCategories extends HttpServlet {

    private HashMap<String, TodoUser> userHashMap;
    private TodoUser currentUser;


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // request the currentUser from the request
        currentUser = (TodoUser) request.getAttribute("currentuser");

        response.setContentType("application/json");


        // Kategorienliste erstellen
        ArrayList<String> categoryList = new ArrayList<>();
        if (!currentUser.getTodoList().isEmpty() && currentUser.getTodoList().get(0).getCategory()!=null) {
            categoryList.add(currentUser.getTodoList().get(0).getCategory()); //erste Kategorie hinzufügen wenn nicht null
        }
        Iterator<Todo> iterUserlist = currentUser.getTodoList().iterator();
        boolean doesExistTwice = false;
        while (iterUserlist.hasNext()) {
            Todo todo = iterUserlist.next(); // hier wird schon weiter iterriert.
            // hinzufügen wenn sie nicht bereits existiert
            if (todo.getCategory()!=null) { // Category kann null sein
                for (String s : categoryList) {
                    if (todo.getCategory().equals(s)) {
                        doesExistTwice = true;
                    }
                }
                if (!doesExistTwice) categoryList.add(todo.getCategory()); // wenn es nicht zweimal vorkommt hinzufügen
            }
            doesExistTwice=false; // und wieder richtig setzen.
        }

        //Marshallen und Ok senden.
        Marshaller marshaller = null;
        try {
            marshaller = getJAXBContext().createMarshaller();
        } catch (JAXBException e1) {
            response.sendError(406, "unsupported accept type");
            e1.printStackTrace();
        }
        try {
            marshaller.marshal(categoryList, response.getWriter());
            response.sendError(200, "ok");
        } catch (JAXBException e) {
            response.sendError(406, "unsupported accept type");
            e.printStackTrace();
        }

    }
}
