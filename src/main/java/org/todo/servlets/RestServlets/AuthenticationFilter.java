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


@WebFilter(urlPatterns = "/api/todos")
public class AuthenticationFilter extends HttpFilter {

    private HashMap<String, TodoUser> userHashMap;
    private ServletContext sc;


    public void init() {
        sc = this.getServletContext();
        userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getMethod().equals("GET") || request.getMethod().equals("POST")) {
            try {
                String authHeader = request.getHeader("Authorization");
                //  if (authHeader != null) {
                //        response.sendError(409, "invalid user data");
                //   }
                String scheme = authHeader.split(" ")[0];
                // if (authHeader != null) {
                //       response.sendError(415, "unsupported content type");
                //   }
                if (!scheme.equals("Basic")) throw new Exception();
                String credentials = authHeader.split(" ")[1];
                credentials = new String(DatatypeConverter.parseBase64Binary(credentials), ISO_8859_1);
                String username = credentials.split(":")[0];
                String password = credentials.split(":")[1];
                if (!password.equals(username)) ;
                if (userHashMap.containsKey(username)) {
                    if (userHashMap.get(username).getPassword().equals(password)) {
                        request.setAttribute("currentuser", userHashMap.get(username));
                        System.out.println(username + " : ist der currentuser");
                    }
                }
            } catch (Exception ex) {
                System.out.println("wenn hier im catch...");
                response.setStatus(SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
