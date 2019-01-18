package org.todo.servlets.RestServlets;

/**
 * Part of the REST Interface get JASON Format for Registration.
 */


import org.todo.business.SaveHelper;
import org.todo.business.TodoUser;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.HashMap;


import static org.todo.servlets.RestServlets.JaxbHelper.getJAXBContext;


@WebServlet("/api/users")
public class RESTpostUser extends HttpServlet {


    private HashMap<String, TodoUser> userHashMap;
    private TodoUser                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         currentUser;
    private ServletContext sc;

    public void init() {
        sc = this.getServletContext();
        userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
    }

    // create a new todo user
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Log in as user or it makes the new Registration as new user if he ist not registred
        TodoUser user = null;
        try {
            //import the Jason from the body and makes the stream to TodoUser returns a type TodoUser
            //name and passwort
            Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
            user = unmarshaller.unmarshal(new StreamSource(request.getInputStream()), TodoUser.class).getValue();
        } catch (JAXBException e) {
            response.sendError(415, "unsupported content type");
            e.printStackTrace();
        }

        if (user.getName().trim().isEmpty()) {
            System.out.println("Pflichtfeld Name");
            response.sendError(400, "invalid user data");
            return;
        }
        // check for password is null or empty
        if (user.getPassword().trim().isEmpty()){
            System.out.println("Pflichtfeld Passwort");
            response.sendError(400, "invalid user data");
            return;
        }
        // Name and password can not be equal
        if (user.getName().equals(user.getPassword())) {
            System.out.println("Passwoerter duerfen nicht mit dem user ubereinstimmen");
            response.sendError(400, "invalid user data");
            return;
        }
        // checks for dublicate name
        if (userHashMap.containsKey(user.getName())) {
            System.out.println(userHashMap.get(user.getName()) + " es gibt bereits einen user mit diesem Namen");
            response.sendError(409, "a user with the same name already exists");
            return;
        } else {
            // makes a new TodoUser
            currentUser = new TodoUser(user.getName(), user.getPassword());
            userHashMap.put(currentUser.getName(), currentUser);
            System.out.println(user.getName() + " Neuer user ist Registriert");
            response.sendError(201, "new user registered");

            // User saving
            SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
            helper.saveUsers();
            System.out.println("User saved");
        }
    }
}
