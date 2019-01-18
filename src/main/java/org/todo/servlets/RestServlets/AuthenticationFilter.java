package org.todo.servlets.RestServlets;

import org.todo.business.TodoUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@WebFilter(urlPatterns = "/api/todos")
public class AuthenticationFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request.getMethod().equals("POST")) {
            try {
                String authHeader = request.getHeader("Authorization");
                    if (authHeader != null) {
                        response.sendError(409, "invalid user data");
                    }
                String scheme = authHeader.split(" ")[0];
                    if (authHeader != null) {
                        response.sendError(415, "unsupported content type");
                    }
                if (!scheme.equals("Basic")) throw new Exception();
                String credentials = authHeader.split(" ")[1];
                credentials = new String(DatatypeConverter.parseBase64Binary(credentials), ISO_8859_1);
                String username = credentials.split(":")[0];
                String password = credentials.split(":")[1];


                if (!password.equals(username)) throw new Exception();


            } catch (Exception ex) {
                response.setStatus(SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }



}
