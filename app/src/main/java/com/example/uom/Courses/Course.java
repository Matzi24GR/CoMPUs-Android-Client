package com.example.uom.Courses;

import android.util.Log;

import org.jsoup.nodes.Element;

public class Course {
    private String Title;
    private String Profs;
    private String Code;
    private int Semester;
    private boolean Active;
    private String Url;

    public Course(Element element) {
        Title = element.select("a").text();
        Profs = element.select("b").text();
        Url = "http://compus.uom.gr/"+element.select("a").attr("href");
        Semester = Integer.parseInt(element.select("i").text().replaceAll("\\D+",""));
        Active = element.select("img").attr("alt").contains("Το μάθημα αυτό είναι ενεργοποιημένο");
        Code = Url.replaceAll("http://compus.uom.gr/","").replaceAll("/index.php","");
        Log.i("Course", "Title: "+Title +"  |  Profs: "+Profs+"  |  URL: "+Url+"  |  Semester: " + Semester+"  |  Active?: "+Active+"  |  Code: "+Code);
    }

    public String  getTitle()    {return Title;}
    public String  getProfs()    {return Profs;}
    public String  getUrl()      {return Url;}
    public String  getCode()     {return Code;}
    public int     getSemester() {return Semester;}
    public boolean isActive()    {return Active;}

}
