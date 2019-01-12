package org.todo.business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TodoUser {

    private String name;
    private String password;
    private ArrayList<Todo> todoList;
    private static final AtomicInteger count = new AtomicInteger(0);

    public TodoUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.todoList = new ArrayList<>();
    }

    public void addTodo(int id, String title, String category, String datum, boolean important, boolean completed){
        if (datum.isEmpty()) {
            // With date
            todoList.add(new Todo (id, title, category, important, completed));
        } else {
            //Without date
            todoList.add(new Todo (id, title, category, datum, important, completed));
        }
                System.out.println("New Todo is created.");
    }

    public ArrayList<Todo> getTodoList() {
        return this.todoList;
    }

    public String getPassword() {
        return password;
    }

    public String getName () {
        return name;
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categoryList = new ArrayList<>();
        for (Todo todo : todoList) {
            categoryList.add(todo.getCategory());
        }
        return categoryList;
    }


}
