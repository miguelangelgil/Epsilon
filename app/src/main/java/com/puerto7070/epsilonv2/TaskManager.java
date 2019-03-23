package com.puerto7070.epsilonv2;

import java.util.ArrayList;

public class TaskManager {

    static ArrayList<Task> completed_task   = new ArrayList<Task>();
    static ArrayList<Task> uncompleted_task = new ArrayList<Task>();

    static void CompleteTask(Task _task_reference)
    {
        completed_task.add(_task_reference);
        uncompleted_task.remove(_task_reference);
    }

    public static void CreateTask(String _name, String _project)
    {
        uncompleted_task.add(new Task(_name, _project));
    }


}
