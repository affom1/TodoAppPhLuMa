package org.todo.business;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class TodoUser implements Serializable {


    private String name;
    private String password;
    private  ArrayList<Todo> todoList;


    public TodoUser(){

    }

    public TodoUser(String name, String password) {
        this.name = name;
        this.password = password;
        this.todoList = new ArrayList<>();
    }

    public void addTodo(int id, String title, String category, String datum, boolean important, boolean completed){
        if (category.isEmpty()) category = "no specific category";
        if (datum.isEmpty()) {
            // With date
            todoList.add(new Todo (id, title, category, important, completed));
        } else {
            //Without date
            todoList.add(new Todo (id, title, category, datum, important, completed));
        }

        System.out.println("New Todo is created.");
    }

    //war Sting habe ich auf Arraylist geädnert hat dies konsequenzen?
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


    // when you send the Id make add +1
    public int determineHighestId() {
        int highest = 0; // wenn noch kein Todos exisitert wird die ID 0.
        for (Todo todo : todoList) {
            if (todo.getId() > highest) highest = todo.getId();
        }
        return highest;
    }

    // Momentan nur für RestServlet Post
    public void addTodo(Todo todo) {
        todoList.add(todo);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
