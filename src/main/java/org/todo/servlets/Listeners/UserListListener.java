package org.todo.servlets.Listeners;

import org.todo.business.SaveHelper;
import org.todo.business.TodoUser;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;
import java.util.HashMap;

@WebListener()
public class UserListListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
    SaveHelper helper;
    HashMap<String, TodoUser> userHashMap;
    // Public constructor is required by servlet spec
    public UserListListener() {    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        helper = new SaveHelper();
        userHashMap = helper.loadUsers();
        // uncomment to create a Sample User called Freddy Dummy with PW = 1 and some Todos
//        helper.createSampleUserWithSampleTodos();
        ServletContext sc = sce.getServletContext();
        sc.setAttribute("users", userHashMap);
        sc.setAttribute("saveHelper", helper);

    }

    public void contextDestroyed(ServletContextEvent sce) {
        helper.saveUsers(userHashMap);
    }


}
