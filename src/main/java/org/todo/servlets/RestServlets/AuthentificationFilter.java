package org.todo.servlets.RestServlets;

import org.todo.business.TodoUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;


@WebFilter(urlPatterns = { "/api/todos/*","/api/categories"})
public class AuthentificationFilter extends HttpFilter {

    private HashMap<String, TodoUser> userHashMap;


    public void init() {
        ServletContext sc = this.getServletContext();
        userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {

            String authHeader = request.getHeader("Authorization");
            String scheme = authHeader.split(" ")[0];

            if (!scheme.equals("Basic")) throw new Exception();
            String credentials = authHeader.split(" ")[1];
            credentials = new String(DatatypeConverter.parseBase64Binary(credentials), ISO_8859_1);
            String username = credentials.split(":")[0];
            String password = credentials.split(":")[1];
            //  if (!password.equals(username)) ;
            if (userHashMap.containsKey(username)) {
                if (userHashMap.get(username).getPassword().equals(password)) {
                    request.setAttribute("currentuser", userHashMap.get(username));
                } else {
                    System.out.println("Falsches Passwort");
                    throw new Exception();
                }
            } else throw new Exception(); //User nicht in Map
        } catch (Exception ex) {
            response.sendError(SC_UNAUTHORIZED, "user not authorized");
            return;
        }
        // sent him to the Servlets
        chain.doFilter(request, response);
    }
}
