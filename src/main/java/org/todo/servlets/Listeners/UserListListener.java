package org.todo.servlets.Listeners;

import org.todo.business.TodoUser;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.ArrayList;

@WebListener()
public class UserListListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    // Public constructor is required by servlet spec
    public UserListListener() {
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
      // get the ServletContext from the event, that is created from the listener

        //Todo Es soll eine Datei ausgelesen werden, wo die Userliste gespeichert ist,
        //Todo Alternativ, wenn noch keine Datei existiert, soll eine neue Userliste erstellt werden.
//        userList = userLaden();

        ServletContext sc = sce.getServletContext();
        ArrayList<TodoUser> userList = (ArrayList<TodoUser>) sc.getAttribute("users"); // This should retrieve a null object, as it should be empty
        if (userList == null) { // ist es sowieso
            System.out.println("Noch keine User erstellt. User werden erstellt");
            userList = new ArrayList<>();

            // create a bunch of Samples, uncomment if necessary
            userList.add(new TodoUser("Freddy Dummy", "1"));

            userList.get(0).addTodo(0,"Grossmutter besuchen", "Freizeit", "2018-11-24", false, false);
            userList.get(0).addTodo(1,"Grossmutter befragen", "Freizeit", "2018-12-24", true, false);
            userList.get(0).addTodo(2,"Von Wolf flüchten", "Panik", "2018-11-13", false, true);
            userList.get(0).addTodo(3,"Im Wald stolpern", "Dumm", "2018-11-26", false, false);
            userList.get(0).addTodo(4,"Todo ohne Datum", "Freizeit","",true, false);
            userList.get(0).addTodo(5,"Wolf töten", "Panik", "2018-11-12", true, true);
            userList.get(0).addTodo(6,"Salami essen", "Panik", "2019-11-12", false, false);
            userList.get(0).addTodo(7,"Joggen", "Freizeit", "2019-08-12", true, false);

            // save them in the ServletContext
            sc.setAttribute("users", userList);

        }


    }

    public void contextDestroyed(ServletContextEvent sce) {
//      userSpeichern();
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
}
