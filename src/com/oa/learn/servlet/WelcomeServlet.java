package com.oa.learn.servlet;

import com.oa.learn.bean.User;
import com.oa.learn.utils.DBUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String username = null;
        String password = null;
        if(cookies != null){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if("username".equals(name)){
                    username = cookie.getValue();
                }
                else if("password".equals(name)){
                    password = cookie.getValue();
                }
            }

        }

        if (username != null && password != null) {
            //验证用户名、密码
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String role = null;
            boolean flag = false;

            try {
                conn = DBUtils.getConnection();
                String sql = "select username, password, role from user where username = ? and password = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                rs = ps.executeQuery();
                if(rs.next()){
                    flag = true;
                    role = rs.getString("role");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBUtils.close(conn, ps, rs);
            }

            if(flag){
                //获取session
                HttpSession session = request.getSession();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setRole(role);
                session.setAttribute("user", user);
                if("1".equals(role)){
                    response.sendRedirect(request.getContextPath() + "/select.jsp");
                }
                else {
                    response.sendRedirect(request.getContextPath() + "/userSelect.jsp");
                }

            }
            else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        }
        else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
