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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.stream.Stream;


@WebServlet("/api/*")
public class RESTdoPost extends HttpServlet {


    HashMap<String, TodoUser> userHashMap;
    TodoUser currentUser;
    HttpSession session;
    ServletContext sc;

    public void init() {
        sc = this.getServletContext();
        userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
    }

    // create a resource
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("TEST");
        String lastPart = null;
        try {
            out.println(request.getRequestURI());
            String[] urlParts = request.getRequestURL().toString().split("/");
            Stream.of(urlParts).forEach(System.out::println);
            lastPart = urlParts[urlParts.length - 1];
            Stream.of(lastPart).forEach(System.out::println);
        } catch (Exception e) {
            response.sendError(415, "unsupported contend type"); // bin ich mir nicht sicher ob der hier hin gehörht..
            e.printStackTrace();
        }
        if (lastPart.equals("users")) {
            String name = null;
            String password = null;
            try {
                name = request.getParameter("name");
                password = request.getParameter("password");
            } catch (Exception e) {
                response.sendError(400, "invalied user data");
                e.printStackTrace();
            }
            out.println("überprüfen ob user in liste ist");
            boolean korrektesLogin = false;
            if (userHashMap.containsKey(name)) {
                if (userHashMap.get(name).getPassword().equals(password)) {
                    System.out.println(userHashMap.get(name).getName() + " hat sich erfolgreich eingeloggt");
                    this.currentUser = userHashMap.get(name);
                    korrektesLogin = true;
                    // User in Session speichern
                    session.setAttribute("currentUser", currentUser);
                    response.sendError(201, "user registered");
                }
                if (!korrektesLogin) {
                    System.out.println("nicht korrektes Login");
                    response.sendError(409, "a user with the same name already exists");
                }


                //Falls nicht vorhanden automatisch Registrieren
                else if (!(userHashMap.containsKey(name))) {

                    if (userHashMap.containsKey(name)) {
                        // Neuen User anlegen und weiterleiten auf Create Todos
                        currentUser = new TodoUser(name, password);
                        userHashMap.put(currentUser.getName(), currentUser);
                        // User in Session speichern
                        session.setAttribute("currentUser", currentUser);
                        // Angelegten User speichern
                        SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
                        helper.saveUsers();
                        System.out.println("User wurde angelegt und ist in der session");
                        response.sendError(201, "new user registered");
                    }
                }

            }
        }
        //post to do neus todo oder update todo
        if (lastPart.equals("todos")) {
            System.out.println("test todos");

            // session holen..
            try {
                HttpSession session = request.getSession();
                currentUser = (TodoUser) session.getAttribute("currentUser");
            } catch (Exception e) {
                response.sendError(401, "user not authorized");
                e.printStackTrace();
            }


            // ACHTUNG Title muss mandetory sein...
            // Todos erstellen, gemäss Input aus Jason
            boolean important = false;
            String title = null;
            String category = null;
            String date = null;
            try {
                title = request.getParameter("title");
                category = request.getParameter("category");
                important = Boolean.parseBoolean(request.getParameter("important"));
                date = request.getParameter("dueDate");
            } catch (Exception e) {
                response.sendError(400, "invalid todo data");
                e.printStackTrace();
                System.out.println("hat nicht funktionert");
            }
            // creation of Todos and save them.
            currentUser.addTodo(determineHighestId() + 1, title, category, date, important, false);
            ServletContext sc = this.getServletContext();
            SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
            helper.saveUsers();

            // send him back to the List
            response.sendError(201, "todo added");
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