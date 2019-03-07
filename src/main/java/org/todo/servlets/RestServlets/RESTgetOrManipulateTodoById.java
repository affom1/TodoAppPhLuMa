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
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.HashMap;

import static org.todo.servlets.RestServlets.JaxbHelper.getJAXBContext;

@WebServlet("/api/todos/*")
public class RESTgetOrManipulateTodoById extends HttpServlet {

    private HashMap<String, TodoUser> userHashMap;
    private TodoUser currentUser;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // request the currentUser from the request
        currentUser = (TodoUser) request.getAttribute("currentuser");

        response.setContentType("application/json");


        // get the todo errors if it aint a number or todo does not exist
        int idOfTodo = -1;
        try {
            String id = request.getRequestURL().toString();
            idOfTodo = Integer.parseInt(id.split("todos/")[1]);
            System.out.println(idOfTodo);
        } catch (Exception e) {
            System.out.println( "bad Syntax");
            response.sendError(406, "unsupported accept type");
            return;
        }
        // try to get the todo
        Todo currentTodo = null;

        for (Todo todo : currentUser.getTodoList()) {
            if (todo.getId() == idOfTodo) {
                currentTodo = todo;
            }
        }
        if (currentTodo==null) {
            response.sendError(404, "todo not found");
            return;
        } else {
            // start Marshaller und schicke die Antwort
            Marshaller marshaller = null;
            try {
                marshaller = getJAXBContext().createMarshaller();
                marshaller.marshal(currentTodo, response.getWriter());
                response.sendError(200, "ok");
            } catch (JAXBException e1) {
                e1.printStackTrace();
                response.sendError(406, "unsupported accept type");
                return;
            }
        }

    } //End of doGet()

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // request the currentUser from the request
        currentUser = (TodoUser) request.getAttribute("currentuser");

        // get the todo errors if it aint a number or todo does not exist
        int idOfTodo = -1;
        try {
            String id = request.getRequestURL().toString();
            idOfTodo = Integer.parseInt(id.split("todos/")[1]);
            System.out.println(idOfTodo);
        } catch (Exception e) {
            System.out.println("bad Syntax");
            response.sendError(415, "unsupported content type");
            return;
        }
        // try to get the todo
        Todo currentTodo = null;
        for (Todo todo : currentUser.getTodoList()) {
            if (todo.getId() == idOfTodo) {
                currentTodo = todo;
            }

        }
        if (currentTodo == null) {
            System.out.println("Todo not found");
            response.sendError(404, "todo not found");
            return;
        } else {
            // create Marshaller
            Unmarshaller unmarshaller = null;
            try {
                unmarshaller = getJAXBContext().createUnmarshaller();
            } catch (JAXBException e) {
                e.printStackTrace();
                System.out.println("getJAXBContext wurde abgefagen");
                response.sendError(400, "invalid todo data");
            }
            // create a temporary Todos and Marshall it into
            Todo tempTodo = null;
            try {
                tempTodo = unmarshaller.unmarshal(new StreamSource(request.getInputStream()), Todo.class).getValue();
            } catch (JAXBException e) {
                e.printStackTrace();
                System.out.println("todo InputStram todo.class wurde abgefangen");
                response.sendError(400, "invalid todo data");
            }
            // Checks if title is set because it is required
            if (tempTodo.getTitle() == null) {
                response.sendError(400, "invalid todo data");
                System.out.println("kein titel eingegeben");
                return;
            }
            // Update and save
            currentTodo.setTitle(tempTodo.getTitle());
            currentTodo.setCategory(tempTodo.getCategory());
            currentTodo.setDueDate(tempTodo.getDueDate());
            currentTodo.setCompleted(tempTodo.isCompleted());
            currentTodo.setImportant(tempTodo.isImportant());
            ServletContext sc = this.getServletContext();
            SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
            helper.saveUsers();

            // send answer
            response.sendError(204, "todo updated");
        }
    }// End of doPut()

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // request the currentUser from the request
        currentUser = (TodoUser) request.getAttribute("currentuser");

        // get the todo errors if it aint a number or todo does not exist
        int idOfTodo = -1;
        try {
            String id = request.getRequestURL().toString();
            idOfTodo = Integer.parseInt(id.split("todos/")[1]);
            System.out.println(idOfTodo);
        } catch (Exception e) {
            System.out.println("bad Syntax");
            response.sendError(415, "unsupported content type");
            return;
        }
        // try to get the todo
        Todo currentTodo = null;

        for (int i = 0;i<currentUser.getTodoList().size();i++) {
            if (currentUser.getTodoList().get(i).getId() == idOfTodo) {
                currentTodo = currentUser.getTodoList().get(i);
                currentUser.getTodoList().remove(i);
                System.out.println("Wir löschen Todo der ID: "+idOfTodo);
                break;
            }
        }

        if (currentTodo==null) {
            System.out.println("Todo not found");
            response.sendError(404, "todo not found");
            return;
        }

        //save
        ServletContext sc = this.getServletContext();
        SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
        helper.saveUsers();

        // send answer
        response.sendError(204, "todo removed");
        return;
    }
}