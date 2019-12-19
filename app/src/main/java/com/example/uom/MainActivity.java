package com.example.uom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usrText = findViewById(R.id.username_textview);
        final EditText passwdText = findViewById(R.id.password_textview);
        final TextView userText = findViewById(R.id.user_text);
        final Button loginButton = findViewById(R.id.login_button);
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final WebView  webView = findViewById(R.id.webView);

        new AllTrustingTrustManager();

        class Login extends AsyncTask<Void, Void, Document> {
            private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            private Document doc;
            private String username;
            private String password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                username = usrText.getText().toString();
                password = passwdText.getText().toString();

                progressDialog.show();

                //easter egg
                if (username.contains("42069")) startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/dQw4w9WgXcQ")));
            }

            @Override
            protected Document doInBackground(Void... voids) {
                String url="https://compus.uom.gr/modules/auth/login.php";
                try {
                    Connection.Response response = Jsoup.connect(url)
                            .data("uname",username)
                            .data("pass",password)
                            .data("login","submit")
                            .method(Connection.Method.POST)
                            .execute();
                    doc = Jsoup.parse(response.body());
                } catch (IOException e){
                    e.printStackTrace();
                }
                return doc;
            }

            @Override
            protected void onPostExecute(Document doc) {
                super.onPostExecute(doc);
                try {

                    String html = doc.toString();

                    if (html.contains("Τα Μαθήματά Μου")) {
                        //Show User Name
                        String user = Jsoup.parse(html).select("td[class=info_user]").text();
                        userText.setText(user);

                        //Parse Courses
                        final ArrayList<Course> courses = new ArrayList<>();
                        Elements coursesElements = doc.select("td[class=external_table]");
                        for (int i = 0; i < coursesElements.size(); i++)
                            courses.add(new Course(coursesElements.get(i)));

                        //Display Courses
                        CourseAdapter adapter = new CourseAdapter(getApplicationContext(),R.layout.course_item, courses);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);

                    } else if (html.contains("Η Είσοδος Απέτυχε")) {
                        Toast.makeText(getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();

            }

        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login login = new Login();
                login.execute();

            }
        });
    }
}
