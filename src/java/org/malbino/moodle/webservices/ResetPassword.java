/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.malbino.moodle.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.malbino.orion.entities.Estudiante;
import org.malbino.orion.entities.Usuario;

/**
 *
 * @author malbino
 */
public class ResetPassword implements Runnable {

    private static final String MOODLEWSRESTFORMAT = "json";

    String login;
    String webservice;
    String username;
    String password;
    String servicename;

    Estudiante estudiante;
    String contraseña;

    public ResetPassword(String login, String webservice, String username, String password, String servicename, Estudiante estudiante, String contraseña) {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        }

        this.login = login;
        this.webservice = webservice;
        this.username = username;
        this.password = password;
        this.servicename = servicename;

        this.estudiante = estudiante;
        this.contraseña = contraseña;
    }

    public String token() throws MalformedURLException, IOException {
        String stringurl = String.format("%s?username=%s&password=%s&service=%s", login, username, password, servicename);

        URL url = new URL(stringurl);//your url i.e fetch data from .
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
            response.append('\r');
        }
        conn.disconnect();

        JSONObject json = new JSONObject(response.toString());

        return json.getString("token");
    }

    public JSONArray getUser(String token, Usuario usuario) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&"
                + "criteria[0][key]=%s&"
                + "criteria[0][value]=%s",
                webservice,
                token,
                "core_user_get_users",
                MOODLEWSRESTFORMAT,
                "idnumber",
                usuario.getId_persona()
        );

        URL url = new URL(stringurl);//your url i.e fetch data from .

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
            response.append('\r');
        }
        conn.disconnect();

        JSONObject jsonObject = new JSONObject(response.toString());
        JSONArray users = jsonObject.getJSONArray("users");

        return users;
    }

    public void editUser(String token, int userid, String contraseña) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&"
                + "users[0][id]=%s&"
                + "users[0][password]=%s&",
                webservice,
                token,
                "core_user_update_users",
                MOODLEWSRESTFORMAT,
                userid,
                URLEncoder.encode(contraseña, "UTF-8"));

        URL url = new URL(stringurl);//your url i.e fetch data from .

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                    + conn.getResponseCode());
        }
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
            response.append('\r');
        }
        conn.disconnect();

        System.out.println(response.toString());
    }

    public void editarUsuario() {
        try {
            //token
            String token = token();

            JSONArray users = getUser(token, estudiante);
            System.out.println("get users -> " + users.length());
            if (!users.isEmpty()) {
                JSONObject user = users.getJSONObject(0);
                editUser(token, user.getInt("id"), contraseña);
                System.out.println("edit user -> ");
            }
        } catch (IOException | JSONException e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }

    @Override
    public void run() {
        editarUsuario();
    }

}
