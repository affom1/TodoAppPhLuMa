package org.todo.business;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

public class Todo implements Serializable {

    private int id;
    private String title;
    private String category;
    private LocalDate dueDate;
    private boolean important;
    private boolean completed;

    // Konstruktor für jaxb
    public Todo() {

    }

    // Konstruktor mit Datum
    public Todo(int id, String title, String category, String datum, boolean important, boolean completed) {
        this.id = id;
        this.title = title;
        this.category = category;
        // Datumskreation
        DateTimeFormatter marcFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        this.dueDate = LocalDate.parse(datum, marcFormatter);
        this.important = important;
        this.completed = completed;
    }
    // Konstruktor ohne Datum.
    public Todo(int id, String title, String category, boolean important, boolean completed) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.important = important;
        this.completed = completed;
    }
    //Kon

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        if (category == null) return "no category";
        return category;
    }
    public String getEmptyCategoryIfEmpty(){
        if (category == null) return "";
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void updateEverythingButId (String title, String category, String datum, boolean important) {
        this.title=title;
        if (category.isEmpty()) category = "no specific category";
        this.category=category;
        if (datum.isEmpty()) {
            this.dueDate=null;
        } else {
            DateTimeFormatter marcFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            this.dueDate = LocalDate.parse(datum, marcFormatter);
        }
        this.important=important;
    }
    public LocalDate getDueDate() {
        if (dueDate==null){
            return LocalDate.now();
        }
        return dueDate;
    }
    public String getInternationalFormattedDate() {
        if (dueDate==null) return "";

        return dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

    }

    public String getImportantOnIfChecked(){
        if (important) return "checked";
        return "";
    }




    public String getFormattedDate() {
        if (dueDate!=null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.LL.yyyy");
            return dueDate.format(formatter);
        } else {
            return  "No date specified";
        }
    }

    public boolean isImportant() {
        return important;
    }

    public boolean isImportantAndOverdue() {
        if ((important) && (isOverdue() )) {
            return true;
        }

        return false;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isOverdue() {

        if (dueDate==null)return false;
        if (this.dueDate.isEqual(LocalDate.now())) return false; // am gleichen Tag
        if (this.dueDate.isAfter(LocalDate.now())) return false; // das Due Date ist nach heute --> somit NICHT overdue
        return true;
    }

    public String stringIsOverdue() {
        if(dueDate != null) {
            if (dueDate.isBefore(LocalDate.now())) {
                return "true";
            }
        }
        return "false";



    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Todo{" + "id=" + id + ", title='" + title + '\'' + ", category='" + category + '\'' +
                ", dueDate=" + dueDate + ", important=" + important + ", completed=" + completed + '}';
    }

}
