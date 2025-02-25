import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

@WebServlet("/vulnerable")
public class VulnerableApp extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String DB_USER = "root";  // 🔴 Hardcoded Credentials
    private static final String DB_PASS = "password";  // 🔴 Hardcoded Credentials

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userInput = request.getParameter("input");

        // 🔴 SQL Injection
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username = '" + userInput + "'");

            while (rs.next()) {
                response.getWriter().println("User: " + rs.getString("username"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔴 Command Injection
        try {
            String cmd = "ping " + userInput;
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.getWriter().println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔴 Insecure Hashing (MD5)
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // Weak hashing algorithm
            byte[] hash = md.digest(userInput.getBytes());
            response.getWriter().println("Hashed Output: " + new String(hash));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 🔴 XSS Vulnerability
        response.getWriter().println("<html><body>Input: " + userInput + "</body></html>");
    }
}
