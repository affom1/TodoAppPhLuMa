package org.todo.servlets.RestServlets;

import org.todo.business.Todo;
import org.todo.business.TodoUser;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

public class JaxbHelper {

    public static JAXBContext getJAXBContext() throws JAXBException {
        Map<String, Object> properties = new HashMap<>();
        properties.put("eclipselink.media-type", "application/json");
        properties.put("eclipselink.json.include-root", false);
        return JAXBContext.newInstance(new Class[]{TodoUser.class, Todo.class}, properties);
    }
}
