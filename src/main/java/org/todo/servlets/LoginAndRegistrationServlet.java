package org.todo.servlets;

import org.todo.business.SaveHelper;
import org.todo.business.TodoUser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet("/LoginAndRegister.do")
public class LoginAndRegistrationServlet extends HttpServlet {
    HashMap<String, TodoUser> userHashMap;
    TodoUser currentUser;
    HttpSession session;
    ServletContext sc;

    public void init() {
        sc = this.getServletContext();
        // UserListe aus ServerContext ziehen.
        userHashMap = (HashMap<String, TodoUser>) sc.getAttribute("users");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fall = request.getParameter("login");
        // Fall Anmelden:
        if (fall.equals("Anmelden")) {
            // Parameters holen
            String name = request.getParameter("name_login");
            String password = request.getParameter("passwd_login");

            boolean korrektesLogin = false;

            if (userHashMap.containsKey(name)) {
                if (userHashMap.get(name).getPassword().equals(password)) {
                    System.out.println(userHashMap.get(name).getName() + " hat sich erfolgreich eingeloggt");
                    this.currentUser = userHashMap.get(name);
                    korrektesLogin = true;

                    // User in Session speichern
                    session = request.getSession();
                    session.setAttribute("currentUser", currentUser);

                    // Erstkategorie in Session speichern
                    session.setAttribute("choosenCategory", "all");

                    // User auf die Reise schicken
                    if (currentUser.getTodoList().isEmpty()) {
                        // Nach CreateServlet schicken wenn noch leer
                        request.getRequestDispatcher("/newTodo.do").forward(request, response);
                    } else {
                        // Nach TodosListe wenn nicht leer.
                        request.getRequestDispatcher("/todoListNew.do").forward(request, response);
                    }
                }
            }
            if (!korrektesLogin) {
                request.setAttribute("errorMessage", "Invalid user or password, please try again");
                RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
                rd.forward(request, response);
            }

        //Fall Registrieren
        } else if(fall.equals("Registrieren")) {
            // Parameter holen
            String name = request.getParameter("name_registration");
            String password = request.getParameter("passwd_registration");

            if (userHashMap.containsKey(name)) {
                if (userHashMap.get(name).equals(name)) { // Fall User existiert bereits
                    System.out.println(name + " existiert bereits und kann nicht gewählt werden");
                    request.setAttribute("errorMessage", "user "+name+" already exists, please try again in section Registration");
                    RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
                    rd.forward(request, response);
                }

            } else { // Neuen User anlegen und weiterleiten auf Create Todos
                currentUser = new TodoUser(name, password);
                userHashMap.put(currentUser.getName(),currentUser);
                // User in Session speichern
                session = request.getSession();
                session.setAttribute("currentUser", currentUser);
                // Angelegten User speichern
                SaveHelper helper = (SaveHelper) sc.getAttribute("saveHelper");
                helper.saveUsers();
                // Weiterleiten nach neues Todos erstellen und ausbrechen aus Schleife.
                System.out.println("User wurde angelegt, weiterleiten auf neues Todo erstellen");
                request.getRequestDispatcher("/newTodo.do").forward(request, response);

            }
        }
    }

        protected void doGet (HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {


        }


}
