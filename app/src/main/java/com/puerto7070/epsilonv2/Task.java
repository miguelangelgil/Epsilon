package com.puerto7070.epsilonv2;

public class Task {
    String name;
    String project;

   long hours_worked_on;
   int  minutes_worked_on;


    Task(String _name, String _project)
    {
        _name             = name;
        _project          = project;
        hours_worked_on   = 0;
        minutes_worked_on = 0;
    }


    public void UpdateHours(int hours, int minutes)
    {
        hours_worked_on += hours;
        if(minutes_worked_on + minutes > 60)
        {
            hours_worked_on++;
            minutes_worked_on = (minutes_worked_on + minutes)-60;
        }
        else minutes_worked_on += minutes;
    }

}
