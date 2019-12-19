package com.example.uom;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText usrText = findViewById(R.id.username_textview);
        final EditText passwdText = findViewById(R.id.password_textview);
        final Button loginButton = findViewById(R.id.login_button);
        final ListView lessonsListView = findViewById(R.id.lessonsListView);

        Document doc;
        class Login extends AsyncTask<Void, Void, Document> {

            Document doc;
            String url="https://compus.uom.gr/modules/auth/login.php";
            String username;
            String password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                username = usrText.getText().toString();
                password = passwdText.getText().toString();
                //easter egg
                if (username.contains("42069")) startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/dQw4w9WgXcQ")));
            }

            @Override
            protected Document doInBackground(Void... voids) {



                /*/////////////////////////////////////////////////////////////////////////////
                *
                * Probably a very bad solution
                *
                * */
                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                } };


                // Install the all-trusting trust manager
                try {
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                ///////////////////////////////////////////////////////////////////////////////



                try {
                    Connection.Response loginForm = Jsoup
                            .connect(url)
                            .method(Connection.Method.GET)
                            .execute();

                    Connection.Response response = Jsoup.connect(url)
                            .data("uname",username)
                            .data("pass",password)
                            .data("login","submit")
                            .cookies(loginForm.cookies())
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
                        String user = Jsoup.parse(html).select("td[class=info_user]").text();
                        Toast.makeText(getApplicationContext(), user, Toast.LENGTH_LONG).show();


                        ArrayList<Course> courses = new ArrayList<>();
                        Elements coursesElements = doc.select("td[class=external_table]");
                        for (int i = 0; i < coursesElements.size(); i++)
                            courses.add(new Course(coursesElements.get(i)));

                        CourseAdapter adapter = new CourseAdapter(getApplicationContext(), courses);
                        lessonsListView.setAdapter(adapter);

                    } else if (html.contains("Η Είσοδος Απέτυχε")) {
                        Toast.makeText(getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
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
