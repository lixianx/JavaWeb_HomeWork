package com.oa.learn.filter;


import com.oa.learn.bean.Level;
import com.oa.learn.bean.User;
import com.oa.learn.servlet.RegisterServlet;
import com.oa.learn.utils.DBUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFilter implements Filter {
    /**
     * 白名单:welcomeServlet, index.jsp,
     * @param req
     * @param res
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        HttpSession session = request.getSession(false);
        //获取请求路径
        String servletPath = request.getServletPath();
        if("/index.jsp".equals(servletPath) || "/welcome".equals(servletPath) ||
        "/user/login".equals(servletPath) || "/user/logout".equals(servletPath) ||
        "/register.jsp".equals(servletPath) || "/query".equals(servletPath) ||
        "/user/register".equals(servletPath) || "/sendCode".equals(servletPath) ||
                "/images/zq3.png".equals(servletPath) || "/Check".equals(servletPath) ||
                (session != null && session.getAttribute("user") != null)){
            if(session != null && session.getAttribute("user") != null){
                User user = (User)session.getAttribute("user");
                String username = user.getUsername();
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                boolean flag = false;
                String role = null;
                String totalCost = null;

                try {
                    conn = DBUtils.getConnection();
                    String sql = "select role, totalcost, password from user where username=?";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    rs = ps.executeQuery();
                    if(rs.next()){
                        role = rs.getString("role");
                        totalCost = rs.getString("totalcost");
                        user.setPassword(rs.getString("password"));
                        flag = true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    DBUtils.close(conn, ps, null);
                }
                if(flag){
                    user.setRole(role);
                    Double cost = Double.valueOf(totalCost);
                    user.setTotalConsume(cost);
                    if(cost >= 10000){
                        user.setLevel(Level.GOLD);
                    }
                    else if(cost >= 3000){
                        user.setLevel(Level.SILVER);
                    }
                    else {
                        user.setLevel(Level.BRONZE);
                    }
                }
                session.setAttribute("user", user);
            }
            //放行
            chain.doFilter(request, response);
        }
        else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
}
