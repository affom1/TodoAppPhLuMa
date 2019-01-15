package org.todo.business;

import java.io.*;
import java.util.HashMap;

public class SaveHelper {

    HashMap<String, TodoUser> userHashMap;
    final static String ABSOLUTER_SPEICHERORT = System.getProperty("user.home")+"/TodoApp/users.todo";



    public SaveHelper() {
        userHashMap = new HashMap();
    }

    // This method was  used initially for creating sample todos.
    public void createSampleUserWithSampleTodos() {
        //User
        TodoUser user = new TodoUser("Freddy Dummy", "1");
        userHashMap.put(user.getName(), user);
        // Todos
        userHashMap.get(user.getName()).addTodo(0,"Grossmutter besuchen", "Freizeit", "2018-11-24", false, false);
        userHashMap.get(user.getName()).addTodo(1,"Grossmutter befragen", "Freizeit", "2018-12-24", true, false);
        userHashMap.get(user.getName()).addTodo(2,"Von Wolf flüchten", "Panik", "2018-11-13", false, true);
        userHashMap.get(user.getName()).addTodo(3,"Im Wald stolpern", "Dumm", "2018-11-26", false, false);
        userHashMap.get(user.getName()).addTodo(4,"Todo ohne Datum", "Freizeit","",true, false);
        userHashMap.get(user.getName()).addTodo(5,"Wolf töten", "Panik", "2018-11-12", true, true);
        userHashMap.get(user.getName()).addTodo(6,"Salami essen", "Panik", "2019-11-12", false, false);
        userHashMap.get(user.getName()).addTodo(7,"Joggen", "Freizeit", "2019-08-12", true, false);

    }

    // used when saving for the first time
    public void saveUsers(HashMap<String, TodoUser> userHashMap) {
        this.userHashMap = userHashMap;
        String speicherort = System.getProperty("user.home");
        String directoryName = speicherort.concat("/TodoApp");
        String fileName = "users.todo";

        File directory = new File(directoryName);
        // creates the folder "TodoApp" if it doesen't exist
        if (! directory.exists()){
            directory.mkdir();
            // use directory.mkdirs(); here instead.
        }
        // das ist der absolute Speicherort
        File file = new File(directoryName + "/" + fileName);
        try{
            FileOutputStream fos =
                    new FileOutputStream(file.getAbsoluteFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userHashMap);
            oos.close();
            fos.close();
            System.out.printf("Saving successfully @ "+file.getAbsolutePath());
        }
        catch (IOException e){
            System.err.println("Failed to save users");
            e.printStackTrace();
        }
    }

    // used when saving for the next times
    public void saveUsers() {
        String speicherort = System.getProperty("user.home");
        String directoryName = speicherort.concat("/TodoApp");

        String fileName = "users.todo";

        File directory = new File(directoryName);
        // creates the folder "TodoApp" if it doesen't exist
        if (! directory.exists()){
            directory.mkdir();
            // use directory.mkdirs(); here instead.
        }
        // das ist der absolute Speicherort
        File file = new File(directoryName + "/" + fileName);
        try{
            FileOutputStream fos =
                    new FileOutputStream(file.getAbsoluteFile());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userHashMap);
            oos.close();
            fos.close();
            System.out.printf("Saving successfully @ "+file.getAbsolutePath());
        }
        catch (IOException e){
            System.err.println("Failed to save users");
            e.printStackTrace();
        }
    }
    public HashMap<String, TodoUser> loadUsers() {

        try
        {
            FileInputStream fis = new FileInputStream(ABSOLUTER_SPEICHERORT);
            ObjectInputStream ois = new ObjectInputStream(fis);
            userHashMap = (HashMap) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return userHashMap;
    }
}




