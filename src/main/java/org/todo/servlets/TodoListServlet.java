package org.todo.servlets;

import org.todo.business.Todo;
import org.todo.business.TodoUser;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


@WebServlet("/todoListNew.do")
public class TodoListServlet extends HttpServlet {
    ArrayList<TodoUser> userList;
    TodoUser currentUser;
    ArrayList<Todo> todoListe = new ArrayList<>();
    String choosenCategory;

    public void init () {
        // Todo: schick User zurück zu Login wenn keine Session vorhanden. evtl. mit Fehlermessage, melde dich an.
        // Anmerkung, evt. muss das in doGet gemacht werden, da diese Methode sonst trotzdem öffnet.
    }

    // Wird nur benötigt um Kategorie zu wählen
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("doGet() von TodoListServlet wird aufgerufen");
        doPost(request, response);


        //        // Session holen und User holen.
//        HttpSession session = request.getSession();
//        currentUser  = (TodoUser) session.getAttribute("currentUser");
//
//        // Todos sortieren
//        Collections.sort(currentUser.getTodoList(), new Comparator<Todo>() {
//            @Override
//            public int compare(Todo o1, Todo o2) {
//                if (o1.getDueDate() == null) {
//                    return (o2.getDueDate() == null) ? 0 : 1;
//                }
//                if (o2.getDueDate() == null) {
//                    return -1;
//                }
//                return o1.getDueDate().compareTo(o2.getDueDate());
//            }
//        });
//
//        // Todoliste speichern in der Session
//        todoListe = currentUser.getTodoList();
//        session.setAttribute("todoList", todoListe);
//        request.getRequestDispatcher("/todoList_2.jsp").forward(request, response);


    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {

        // Session holen und User holen.
        HttpSession session = request.getSession();
        currentUser  = (TodoUser) session.getAttribute("currentUser");

        // gewählte Kategory holen (Standardmässig all, danach gewählt bis Ende Session)
        choosenCategory = (String) session.getAttribute("choosenCategory");

        // Gewählte Kategorie aus dem request holen
        try {
            choosenCategory = request.getParameter("category"); // category ist Null wenn nichts gewählt wird,
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WEnn null, dann erneut ziehen von choosen Category
        if (choosenCategory==null) choosenCategory = (String) session.getAttribute("choosenCategory");

        // todoListe mit Todos der choosen category füllen
        try {
            if (choosenCategory.equals("all")) {
                // Wenn null dann todoListe mit allen Todos abfüllen
                todoListe = currentUser.getTodoList();
            } else {
                // ansosnten nur mit den Todos der gewählten Kategorie abfüllen
                todoListe = null;
                todoListe = new ArrayList<>();
                for (Todo todo : currentUser.getTodoList()) {
                    if (todo.getCategory().equals(choosenCategory)) { // evtl. noch null Handling notwendig?
                        todoListe.add(todo);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fehler anschauen! Try catch wurde ausgebrochen und wir nehmen gesamte TodoListe");
            todoListe = currentUser.getTodoList();
        }

        // Kategorie an das JSP schicken, vorher sortieren
        Collections.sort(todoListe, new Comparator<Todo>() {
            @Override
            public int compare(Todo o1, Todo o2) {
                if (o1.getDueDate() == null) {
                    return (o2.getDueDate() == null) ? 0 : 1;
                }
                if (o2.getDueDate() == null) {
                    return -1;
                }
                return o1.getDueDate().compareTo(o2.getDueDate());
            }
        });
        //Todo: todoListe mit comparateor NullsLast sortieren
        //        todoListe.sort(Comparator.nullsLast(LocalDate::compareTo).compare(dateOne, dateTwo));

        // Ganzer Teil Kategorienliste (nur für die mögliche Auswahl der Liste, woraus die choosenCategory die schlussendlich gewählt wird)
        kategorienListeErstellen(request);

        // Todoliste und choosenCategory in der Session speichern und ans JSP weiterleiten.
        request.setAttribute("todoList", todoListe);
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("choosenCategory", choosenCategory);
     //   request.setAttribute("choosenCategory", choosenCategory);
        request.getRequestDispatcher("/todoList_2.jsp").forward(request, response);
    }

    private void kategorienListeErstellen(HttpServletRequest request) {
        // Kategorienliste erstellen, die tendenziell eine Teilmenge der Todoliste ist.
        HttpSession session = request.getSession();
        ArrayList<String> categoryList = new ArrayList<>();
        if (!currentUser.getTodoList().isEmpty() && currentUser.getTodoList().get(0).getCategory()!=null) {
            categoryList.add(currentUser.getTodoList().get(0).getCategory()); //erste Kategorie hinzufügen wenn nicht null
        }
        Iterator<Todo> iterUserlist = currentUser.getTodoList().iterator();
        Iterator<String> iterCategorylist = categoryList.iterator();
        while (iterUserlist.hasNext()) {
           Todo todo = iterUserlist.next(); // hier wird schon weiter iterriert.
            if (todo.getCategory()!=null) { // Category kann null sein
                boolean doesExistTwice = false; // hinzufügen wenn sie nicht bereits existiert
                for (String s : categoryList) {
                    if (todo.getCategory() == s) {
                        doesExistTwice = true;
                    }
                }
                if (!doesExistTwice) categoryList.add(todo.getCategory()); // wenn es nicht zweimal vorkommt hinzufügen
                doesExistTwice=false; // und wieder richtig setzen.
            }
        }

        // Debugginggeschichte
//        System.out.println("und nun die Kategorieliste durchlaufen");
//        System.out.println("grosse der liste"+categoryList.size());
//        System.out.println("grosse der Todo liste"+currentUser.getTodoList().size());
//        for (int i =0;i<categoryList.size();i++) {
//            System.out.println(categoryList.get(i));
//        }

        // Kategorienliste in der Session speichern, JSP greift auf diese zu.
        session.setAttribute("categoryList", categoryList);

    }


}
