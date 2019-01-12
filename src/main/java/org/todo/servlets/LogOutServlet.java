package org.todo.servlets;

import org.todo.business.SaveHelper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout.do")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //Todos speichern
        ServletContext sc = this.getServletContext();
        SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
        helper.saveUsers();
        // Session killen
        req.getSession().invalidate();
        // nach login schicken
        resp.sendRedirect(req.getContextPath() + "/Login.jsp");

    }
}
