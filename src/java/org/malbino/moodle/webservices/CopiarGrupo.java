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
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.malbino.orion.entities.Grupo;
import org.malbino.orion.entities.Nota;
import org.malbino.orion.entities.Usuario;

/**
 *
 * @author malbino
 */
public class CopiarGrupo implements Runnable {

    private static final String MOODLEWSRESTFORMAT = "json";

    private static final String PARENT_NAME = "FORMACION";
    private static final String PARENT_IDNUMBER = "f1";

    private static final int FALSE = 0;
    private static final int TRUE = 1;
    private static final String CITY = "Cochabamba";
    private static final String COUNTRY = "BO";

    private static final int EDITINGTEACHER_ROLEID = 3;
    private static final int TEACHER_ROLEID = 4;
    private static final int STUDENT_ROLEID = 5;

    private static final String USUARIO = "supervisor";
    private static final String CONTRASEÑA = "Pa$$w0rdSupervis0r";
    private static final String NOMBRE = "SUPERVISOR";
    private static final String APELLIDOS = "PLATAFORMA";
    private static final String EMAIL = "supervisor@cambiatuemail.com";
    private static final String PAIS = "BO";
    private static final Integer TELEFONO = null;
    private static final Integer CELULAR = null;
    private static final String DIRECCION = "";
    private static final String ID_NUMBER = "supervisor_dde";

    String login;
    String webservice;
    String username;
    String password;
    String servicename;

    Grupo grupo;
    List<Nota> notas;

    public CopiarGrupo(String login, String webservice, String username, String password, String servicename, Grupo grupo, List<Nota> notas) {

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

        this.grupo = grupo;
        this.notas = notas;
    }

    public String token() throws MalformedURLException, IOException {
        String stringurl = String.format("%s?username=%s&password=%s&service=%s", login, username, password, servicename);

        System.out.println("url: " + stringurl);

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

    public JSONArray getCategory(String token, String idnumber) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&addsubcategories=0&criteria[0][key]=%s&criteria[0][value]=%s",
                webservice,
                token,
                "core_course_get_categories",
                MOODLEWSRESTFORMAT,
                "idnumber",
                URLEncoder.encode(idnumber, "UTF-8")
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

        JSONArray categories = new JSONArray(response.toString());

        return categories;
    }

    public JSONArray createCategory(String token, String name, String idnumber) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&categories[0][name]=%s&categories[0][idnumber]=%s",
                webservice,
                token,
                "core_course_create_categories",
                MOODLEWSRESTFORMAT,
                URLEncoder.encode(name, "UTF-8"),
                idnumber
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

        JSONArray categories = new JSONArray(response.toString());

        return categories;
    }

    public JSONArray createCategory(String token, int parent, String name, String idnumber) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&categories[0][parent]=%s&categories[0][name]=%s&categories[0][idnumber]=%s",
                webservice,
                token,
                "core_course_create_categories",
                MOODLEWSRESTFORMAT,
                parent,
                URLEncoder.encode(name, "UTF-8"),
                idnumber
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

        JSONArray categories = new JSONArray(response.toString());

        return categories;
    }

    public JSONArray getCourse(String token, Grupo grupo) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&field=%s&value=%s",
                webservice,
                token,
                "core_course_get_courses_by_field",
                MOODLEWSRESTFORMAT,
                "idnumber",
                grupo.idnumberMoodle()
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
        JSONArray categories = jsonObject.getJSONArray("courses");

        return categories;
    }

    public JSONArray createCourse(String token, int categoryid, Grupo grupo) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&"
                + "courses[0][fullname]=%s&"
                + "courses[0][shortname]=%s&"
                + "courses[0][categoryid]=%s&"
                + "courses[0][visible]=%s&"
                + "courses[0][startdate]=%s&"
                + "courses[0][enddate]=%s&"
                + "courses[0][enablecompletion]=%s&"
                + "courses[0][idnumber]=%s&"
                + "courses[0][summary]=%s",
                webservice,
                token,
                "core_course_create_courses",
                MOODLEWSRESTFORMAT,
                URLEncoder.encode(grupo.fullNameMoodle(), "UTF-8"),
                URLEncoder.encode(grupo.shortNameMoodle(), "UTF-8"),
                categoryid,
                FALSE,
                grupo.inicioMoodle(),
                grupo.finMoodle(),
                TRUE,
                grupo.idnumberMoodle(),
                URLEncoder.encode(grupo.summaryMoodle(), "UTF-8"));

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

        JSONArray courses = new JSONArray(response.toString());

        return courses;
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

    public JSONArray getUser(String token, String idnumber) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&"
                + "criteria[0][key]=%s&"
                + "criteria[0][value]=%s",
                webservice,
                token,
                "core_user_get_users",
                MOODLEWSRESTFORMAT,
                "idnumber",
                idnumber
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

    public JSONArray createUser(String token, Usuario usuario) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&"
                + "users[0][username]=%s&"
                + "users[0][password]=%s&"
                + "users[0][firstname]=%s&"
                + "users[0][lastname]=%s&"
                + "users[0][email]=%s&"
                + "users[0][city]=%s&"
                + "users[0][country]=%s&"
                + "users[0][phone1]=%s&"
                + "users[0][phone2]=%s&"
                + "users[0][address]=%s&"
                + "users[0][idnumber]=%s",
                webservice,
                token,
                "core_user_create_users",
                MOODLEWSRESTFORMAT,
                URLEncoder.encode(usuario.usuarioMoodle(), "UTF-8"),
                URLEncoder.encode(usuario.contraseñaMoodle(), "UTF-8"),
                URLEncoder.encode(usuario.getNombre(), "UTF-8"),
                URLEncoder.encode(usuario.apellidos(), "UTF-8"),
                URLEncoder.encode(usuario.emailTemporal(), "UTF-8"),
                URLEncoder.encode(CITY, "UTF-8"),
                URLEncoder.encode(COUNTRY, "UTF-8"),
                usuario.getTelefono() != null ? usuario.getTelefono() : "",
                usuario.getCelular() != null ? usuario.getCelular() : "",
                URLEncoder.encode(usuario.getDireccion(), "UTF-8"),
                usuario.getId_persona());

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

        JSONArray courses = new JSONArray(response.toString());

        return courses;
    }

    public JSONArray createUser(String token, String usuario, String contraseña, String nombre, String apellidos, String email, String pais, Integer telefono,
            Integer celular, String direccion, String id) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&"
                + "users[0][username]=%s&"
                + "users[0][password]=%s&"
                + "users[0][firstname]=%s&"
                + "users[0][lastname]=%s&"
                + "users[0][email]=%s&"
                + "users[0][city]=%s&"
                + "users[0][country]=%s&"
                + "users[0][phone1]=%s&"
                + "users[0][phone2]=%s&"
                + "users[0][address]=%s&"
                + "users[0][idnumber]=%s",
                webservice,
                token,
                "core_user_create_users",
                MOODLEWSRESTFORMAT,
                URLEncoder.encode(usuario, "UTF-8"),
                URLEncoder.encode(contraseña, "UTF-8"),
                URLEncoder.encode(nombre, "UTF-8"),
                URLEncoder.encode(apellidos, "UTF-8"),
                URLEncoder.encode(email, "UTF-8"),
                URLEncoder.encode(CITY, "UTF-8"),
                URLEncoder.encode(pais, "UTF-8"),
                telefono != null ? telefono : "",
                celular != null ? celular : "",
                URLEncoder.encode(direccion, "UTF-8"),
                id);

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

        JSONArray courses = new JSONArray(response.toString());

        return courses;
    }

    public void enrolUser(String token, int roleid, int userid, int courseid) throws MalformedURLException, IOException {
        String stringurl = String.format("%s?wstoken=%s&wsfunction=%s&moodlewsrestformat=%s&"
                + "enrolments[0][roleid]=%s&"
                + "enrolments[0][userid]=%s&"
                + "enrolments[0][courseid]=%s",
                webservice,
                token,
                "enrol_manual_enrol_users",
                MOODLEWSRESTFORMAT,
                roleid,
                userid,
                courseid);

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

    public void copiarGrupo() {
        try {
            //token
            String token = token();

            //categorias
            JSONArray categories = getCategory(token, PARENT_IDNUMBER);
            System.out.println("get categories -> " + categories.length());
            if (categories.isEmpty()) {
                categories = createCategory(token, PARENT_NAME, PARENT_IDNUMBER);
                System.out.println("create category -> ");
            }
            JSONObject category = categories.getJSONObject(0);
            System.out.println("category -> " + category.getString("name"));

            //gestion academica
            JSONArray ga_categories = getCategory(token, grupo.getGestionAcademica().idnumberMoodle());
            System.out.println("get categories -> " + ga_categories.length());
            if (ga_categories.isEmpty()) {
                ga_categories = createCategory(token, category.getInt("id"), grupo.getGestionAcademica().nameMoodle(), grupo.getGestionAcademica().idnumberMoodle());
                System.out.println("create category -> ");
            }
            JSONObject ga_category = ga_categories.getJSONObject(0);
            System.out.println("category -> " + ga_category.getString("name"));

            //carrera
            JSONArray c_categories = getCategory(token, grupo.getGestionAcademica().idnumberMoodle() + grupo.getMateria().getCarrera().idnumberMoodle());
            System.out.println("get categories -> " + c_categories.length());
            if (c_categories.isEmpty()) {
                c_categories = createCategory(token, ga_category.getInt("id"), grupo.getMateria().getCarrera().nameMoodle(), grupo.getGestionAcademica().idnumberMoodle() + grupo.getMateria().getCarrera().idnumberMoodle());
                System.out.println("create category -> ");
            }
            JSONObject c_category = c_categories.getJSONObject(0);
            System.out.println("category -> " + c_category.getString("name"));

            //nivel
            JSONArray p_categories = getCategory(token, grupo.getGestionAcademica().idnumberMoodle() + grupo.getMateria().getCarrera().idnumberMoodle() + grupo.getMateria().getNivel().getAbreviatura());
            System.out.println("get categories -> " + p_categories.length());
            if (p_categories.isEmpty()) {
                p_categories = createCategory(token, c_category.getInt("id"), grupo.getMateria().getNivel().getNombre(), grupo.getGestionAcademica().idnumberMoodle() + grupo.getMateria().getCarrera().idnumberMoodle() + grupo.getMateria().getNivel().getAbreviatura());
                System.out.println("create category -> ");
            }
            JSONObject p_category = p_categories.getJSONObject(0);
            System.out.println("category -> " + p_category.getString("name"));

            //curso
            JSONArray courses = getCourse(token, grupo);
            System.out.println("get courses -> " + courses.length());
            if (courses.isEmpty()) {
                courses = createCourse(token, p_category.getInt("id"), grupo);
                System.out.println("create course -> ");
            }
            JSONObject course = courses.getJSONObject(0);
            System.out.println("course -> " + course.getString("shortname"));

            //supervisor externo
            JSONArray externalSupervisors = getUser(token, ID_NUMBER);
            System.out.println("get supervisors -> " + externalSupervisors.length());
            if (externalSupervisors.isEmpty()) {
                externalSupervisors = createUser(token, USUARIO, CONTRASEÑA, NOMBRE, APELLIDOS, EMAIL, PAIS, TELEFONO, CELULAR, DIRECCION, ID_NUMBER);
                System.out.println("create supervisor -> ");
            }

            JSONObject externalSupervisor = externalSupervisors.getJSONObject(0);
            System.out.println("supervisor -> " + externalSupervisor.getString("username"));

            enrolUser(token, TEACHER_ROLEID, externalSupervisor.getInt("id"), course.getInt("id"));
            System.out.println("editing supervisor enrol -> ");

            //director academico
            if (grupo.getMateria().getCarrera().getCampus().getInstituto().getDirectorAcademico() != null) {
                JSONArray supervisors = getUser(token, grupo.getMateria().getCarrera().getCampus().getInstituto().getDirectorAcademico());
                System.out.println("get supervisors -> " + supervisors.length());
                if (supervisors.isEmpty()) {
                    supervisors = createUser(token, grupo.getMateria().getCarrera().getCampus().getInstituto().getDirectorAcademico());
                    System.out.println("create supervisor -> ");
                }

                JSONObject supervisor = supervisors.getJSONObject(0);
                System.out.println("supervisor -> " + supervisor.getString("username"));

                enrolUser(token, TEACHER_ROLEID, supervisor.getInt("id"), course.getInt("id"));
                System.out.println("editing supervisor enrol -> ");
            }

            //jefe de carrera
            if (grupo.getMateria().getCarrera().getJefeCarrera() != null) {
                JSONArray supervisors = getUser(token, grupo.getMateria().getCarrera().getJefeCarrera());
                System.out.println("get supervisors -> " + supervisors.length());
                if (supervisors.isEmpty()) {
                    supervisors = createUser(token, grupo.getMateria().getCarrera().getJefeCarrera());
                    System.out.println("create supervisor -> ");
                }

                JSONObject supervisor = supervisors.getJSONObject(0);
                System.out.println("supervisor -> " + supervisor.getString("username"));

                enrolUser(token, TEACHER_ROLEID, supervisor.getInt("id"), course.getInt("id"));
                System.out.println("editing supervisor enrol -> ");
            }

            //docente
            if (grupo.getEmpleado() != null) {
                JSONArray teachers = getUser(token, grupo.getEmpleado());
                System.out.println("get editing teachers -> " + teachers.length());
                if (teachers.isEmpty()) {
                    teachers = createUser(token, grupo.getEmpleado());
                    System.out.println("create editing teacher -> ");
                }

                JSONObject teacher = teachers.getJSONObject(0);
                System.out.println("teacher -> " + teacher.getString("username"));

                enrolUser(token, EDITINGTEACHER_ROLEID, teacher.getInt("id"), course.getInt("id"));
                System.out.println("editing teacher enrol -> ");
            }

            //estudiantes
            for (Nota nota : notas) {
                JSONArray students = getUser(token, nota.getEstudiante());
                System.out.println("get students -> " + students.length());
                if (students.isEmpty()) {
                    students = createUser(token, nota.getEstudiante());
                    System.out.println("create student -> ");
                }

                JSONObject student = students.getJSONObject(0);
                System.out.println("student -> " + student.getString("username"));

                enrolUser(token, STUDENT_ROLEID, student.getInt("id"), course.getInt("id"));
                System.out.println("student enrol -> ");
            }
        } catch (IOException | JSONException e) {
            System.out.println("Exception in NetClientGet:- " + e);
        }
    }

    @Override
    public void run() {
        copiarGrupo();
    }

}
