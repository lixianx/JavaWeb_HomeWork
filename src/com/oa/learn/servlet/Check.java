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

@WebServlet("/Check")
public class Check extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phone = request.getParameter("phone");
        String code = request.getParameter("code");
        long ed = System.currentTimeMillis();
        String start = "";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String responseStatus = "";
        int count = 0;

        try {
            conn = DBUtils.getConnection();
            String sql1 = "select * from code where phone=? and code=?";
            ps = conn.prepareStatement(sql1);
            ps.setString(1, phone);
            ps.setString(2, code);
            rs = ps.executeQuery();
            if(rs.next()){
                start = rs.getString("date");
                long st = Long.parseLong(start);
                if(ed - st > 5 * 60 * 1000){
                    responseStatus = "1";
                }
                else {
                    responseStatus = "2";
                }
                String sql2 = "delete from code where phone=? and code=?";
                ps = conn.prepareStatement(sql2);
                ps.setString(1, phone);
                ps.setString(2, code);
                count = ps.executeUpdate();
                if(count == 0){
                    response.sendRedirect(request.getContextPath() + "/error.jsp");
                }
            }
            else {
                responseStatus = "3";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        response.getWriter().print("{\"status\":\""+responseStatus+"\"}");
    }
}
