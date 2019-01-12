package org.todo.servlets;

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

@WebServlet("/LoginAndRegister.do")
public class LoginAndRegistrationServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    HttpSession session;

    public void init() {
        ServletContext sc = this.getServletContext();
        // UserListe aus ServerContext ziehen.
        userList = (ArrayList<TodoUser>) sc.getAttribute("users");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String fall = request.getParameter("login");
        // Fall Anmelden:
        if (fall.equals("Anmelden")) {

            String name = request.getParameter("name_login");
            String password = request.getParameter("passwd_login");

            boolean korrektesLogin = false;
            int zähler = 0;
            while (!korrektesLogin && zähler<  userList.size()) {
                if (userList.get(zähler).getName().equals(name) && userList.get(zähler).getPassword().equals(password)) {
                    System.out.println(userList.get(zähler).getName() + " hat sich erfolgreich eingeloggt");
                    this.currentUser = userList.get(zähler);
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
                        break;
                    } else {
                        // Nach TodosListe wenn nicht leer.
                        request.getRequestDispatcher("/todoListNew.do").forward(request, response);
                        break;
                    }
                }
                zähler++;
            }
            if (!korrektesLogin) {
                request.setAttribute("errorMessage", "Invalid user or password, please try again");
                RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
                rd.forward(request, response);
            }

        } else if(fall.equals("Registrieren")) { // Fall registrieren
            // Todo Registrieren erstellen inkl, User gibt es schon
            String name = request.getParameter("name_registration");
            String password = request.getParameter("passwd_registration");

            int zähler = 0;
            while (zähler<  userList.size()) {
                if (userList.get(zähler).getName().equals(name)) { // Fall User existiert bereits
                    System.out.println(userList.get(zähler).getName() + " existiert bereits und kann nicht gewählt werden");
                    request.setAttribute("errorMessage", "user already exists, please try again in section Registration");
                    RequestDispatcher rd = request.getRequestDispatcher("/Login.jsp");
                    rd.forward(request, response);
                }
                else { // Neuen User anlegen und weiterleiten auf Create Todos
                    currentUser = new TodoUser(name, password);
                    userList.add(currentUser);
                    // User in Session speichern
                    session = request.getSession();
                    session.setAttribute("currentUser", currentUser);
                    // Weiterleiten nach neues Todos erstellen und ausbrechen aus Schleife.
                    // Todo: CreateTodo als JSP erstellen und schön machen.
                    RequestDispatcher rd = request.getRequestDispatcher("/newTodo.do");
                    rd.forward(request, response);
                    break;
                }
                zähler++;
            }
        }
    }

        protected void doGet (HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {


        }


}
