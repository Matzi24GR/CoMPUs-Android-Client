package com.example.uom;

import android.util.Log;

import org.jsoup.nodes.Element;

public class Course {
    private String Title;
    private String Profs;
    private int Semester;
    private boolean Active;
    private String Url;

    Course(Element element) {
        Title = element.select("a").text();
        Profs = element.select("b").text();
        Url = "http://compus.uom.gr/"+element.select("a").attr("href");
        Semester = Integer.parseInt(element.select("i").text().replaceAll("\\D+",""));
        Active = element.select("img").attr("alt").contains("Το μάθημα αυτό είναι ενεργοποιημένο");
        Log.i("Course", "Title: "+Title +"  |  Profs: "+Profs+"  |  URL: "+Url+"  |  Semester: " + Semester+"  |  Active?: "+Active);
    }

    String  getTitle()    {return Title;}
    String  getProfs()    {return Profs;}
    String  getmUrl()     {return Url;}
    int     getSemester() {return Semester;}
    boolean isActive()    {return Active;}

}
