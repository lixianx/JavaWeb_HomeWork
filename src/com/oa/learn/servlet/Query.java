package com.oa.learn.servlet;

import com.oa.learn.utils.DBUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/query")
public class Query extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String username = request.getParameter("username");
        //System.out.println(username);
        boolean flag = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "select * from user where username=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                flag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }

        response.getWriter().print("{\"value\":"+flag+"}");
    }
}
