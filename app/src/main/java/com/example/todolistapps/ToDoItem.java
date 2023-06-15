package com.example.todolistapps;

public class ToDoItem {
    private int id;
    private String what;
    private String time;

    public ToDoItem(int id, String what, String time) {
        this.id = id;
        this.what = what;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getWhatToDo() {
        return what;
    }

    public String getTime() {
        return time;
    }
}
