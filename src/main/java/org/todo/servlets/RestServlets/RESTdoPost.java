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
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.todo.servlets.RestServlets.JaxbHelper.getJAXBContext;


@WebServlet("/api/users")
public class RESTdoPost extends HttpServlet {


    HashMap<String, TodoUser> userHashMap;
    TodoUser currentUser;
    ServletContext sc;

    public void init() {
        sc = this.getServletContext();
        userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
    }

    // create a resource
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        PrintWriter out = response.getWriter();
//        out.println("TEST");
//        String lastPart = null;
//        try {
//            out.println(request.getRequestURI());
//            String[] urlParts = request.getRequestURL().toString().split("/");
//            Stream.of(urlParts).forEach(System.out::println);
//            lastPart = urlParts[urlParts.length - 1];
//            Stream.of(lastPart).forEach(System.out::println);
//        } catch (Exception e) {
//            response.sendError(415, "unsupported contend type"); // bin ich mir nicht sicher ob der hier hin gehörht..
//            e.printStackTrace();
//        }
        HttpSession session = request.getSession();

        //     try {
        TodoUser user = null;
        try {
            Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
            user = unmarshaller.unmarshal(new StreamSource(request.getInputStream()), TodoUser.class).getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        //  response.sendError(400, "invalied user data");

//
   ////     out.println("überprüfen ob user in liste ist");

        boolean korrektesLogin = false;
        if (userHashMap.containsKey(user.getName())) {
            if (userHashMap.get(user.getName()).getPassword().equals(user.getPassword())) {
                System.out.println(userHashMap.get(user.getName()).getName() + " hat sich erfolgreich eingeloggt");
                this.currentUser = userHashMap.get(user.getName());
                korrektesLogin = true;
                // User in Session speichern
                session.setAttribute("currentUser", currentUser.getName());
                response.sendError(201, "user registered");
            }
            if (!korrektesLogin) {
                System.out.println("nicht korrektes Login");
                response.sendError(409, "a user with the same name already exists");
            }
        }
        //Falls nicht vorhanden automatisch Registrieren
        else if (!(userHashMap.containsKey(user.getName()))) {
            // Neuen User anlegen und weiterleiten auf Create Todos
            currentUser = new TodoUser(user.getName(), user.getPassword());
            userHashMap.put(currentUser.getName(), currentUser);
            // User in Session speichern
            session.setAttribute("currentUser", currentUser.getName());
            // Angelegten User speichern
            SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
            helper.saveUsers();
            System.out.println("User wurde angelegt und ist in der session");
            response.sendError(201, "new user registered");
        }

    }


    public int determineHighestId() {
        int highest = 0; // wenn noch kein Todos exisitert wird die ID 0.
        for (Todo todo : currentUser.getTodoList()) {
            if (todo.getId() > highest) highest = todo.getId();
        }
        return highest;
    }


    private void login(String name, String password) {

    }

}



   /* public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{*/

// zuerst muss ich auf user überprüfen oder?

// danach müsste ich auf die userliste des user zugreifen

// dann kann ich die daten mitgeben

     /*   Todo todo = new Todo(1, "bla bla")


    String s = null;
    try {
        s = marshal(todo);
    } catch (JAXBException e) {
        e.printStackTrace();
    }
    System.out.println(s);
    try {
        todo = unmarshal(s);
    } catch (JAXBException e) {
        e.printStackTrace();
    }
    System.out.println(todo);

    }

    public static String marshal(Todo todo) throws JAXBException {
        Writer writer = new StringWriter();
        Marshaller marshaller = getJAXBContext().createMarshaller();
        marshaller.marshal(todo, writer);
        return writer.toString();
    }

    public static Todo unmarshal(String s) throws JAXBException {
        Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
        return unmarshaller.unmarshal(new StreamSource(new StringReader(s)), Todo.class).getValue();
    }

    private static JAXBContext getJAXBContext() throws JAXBException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("eclipselink.media-type", "application/json");
        properties.put("eclipselink.json.include-root", false);
        return JAXBContext.newInstance(new Class[]{Todo.class}, properties);
    }
}
















}
*/