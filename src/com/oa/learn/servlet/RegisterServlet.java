package com.oa.learn.servlet;

import com.oa.learn.utils.DBUtils;
import com.oa.learn.utils.Encryption;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/user/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String encodedPassword = Encryption.encodePassword(password);
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtils.getConnection();
            String sql = "insert into user(username, password, phone, email, startdate) values(?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, encodedPassword);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, formattedTime);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, null);
        }
        if(count == 1){
            response.sendRedirect(request.getContextPath() + "/goods/list");
        }
        else {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
