package com.oa.learn.servlet;

import com.alibaba.fastjson.JSON;
import com.oa.learn.bean.Level;
import com.oa.learn.bean.User;
import com.oa.learn.utils.DBUtils;
import com.oa.learn.utils.Encryption;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@WebServlet({"/user/login", "/user/logout", "/admin/change", "/users/edit",
        "/user/delete", "/user/reset", "/verify/password"})
public class UserServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if("/user/login".equals(servletPath)){
            doLogin(request, response);
        }
        else if("/user/logout".equals(servletPath)){
            doLogout(request, response);
        }
        else if("/admin/change".equals(servletPath)){
            doAdminChange(request, response);
        }
        else if("/users/edit".equals(servletPath)){
            doUserEdit(request, response);
        }
        else if("/user/delete".equals(servletPath)){
            doUserDel(request, response);
        }
        else if("/user/reset".equals(servletPath)){
            doReset(request, response);
        }
        else if("/verify/password".equals(servletPath)){
            doVerify(request, response);
        }
    }

    /**
     * 验证用户密码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doVerify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        String oldPassword = request.getParameter("oldPassword");
        String hashedPassword = user.getPassword();
        boolean flag = false;
        if(BCrypt.checkpw(oldPassword, hashedPassword)){
            flag = true;
        }
        response.getWriter().print("{\"status\":"+flag+"}");
    }

    /**
     * 重置用户密码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doReset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String username = null;
        String phone = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        HttpSession session = request.getSession(false);
        ServletContext servletContext = request.getServletContext();

        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "select phone, username from user where id=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                username = rs.getString("username");
                phone = rs.getString("phone");
            }
            String resetPassword = username + phone;
            String hashedResetPassword = Encryption.encodePassword(resetPassword);
            String sql2 = "update user set password=? where id=?";
            ps = conn.prepareStatement(sql2);
            ps.setString(1, hashedResetPassword);
            ps.setString(2, id);
            count = ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, null);
        }
        response.getWriter().print("{\"status\":"+count+"}");
    }

    /**
     * 删除用户
     * 我也不知道是什么原因，直接传用户名老是报错，所以才传的id
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doUserDel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String username = "";
        int count = 0;

        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);
            String sql1 = "select username from user where id=?";
            ps = conn.prepareStatement(sql1);
            ps.setString(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                username = rs.getString("username");
                String sql2 = "delete from history where username=?";
                ps = conn.prepareStatement(sql2);
                ps.setString(1, username);
                count = ps.executeUpdate();
                String sql3 = "delete from user where id=?";
                ps = conn.prepareStatement(sql3);
                ps.setString(1, id);
                count = ps.executeUpdate();
                if(count == 1){
                    conn.commit();
                }
            }
            else {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        response.getWriter().print("{\"status\":\""+count+"\"}");

    }

    /**
     * 展示用户列表
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doUserEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<>();
        String keywords = request.getParameter("keywords");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            conn = DBUtils.getConnection();
            String sql = "select * from user where username like ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keywords + "%");
            rs = ps.executeQuery();
            while(rs.next()){
                //System.out.println(rs.getString("username"));
                String id = rs.getString("id");
                String role = rs.getString("role");
                String username = rs.getString("username");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                String startDate = rs.getString("startdate");
                String totalCost = rs.getString("totalcost");
                Double cost = Double.valueOf(totalCost);
                Date utilDate = dateFormat.parse(startDate);


                User user = new User(id, username, null, null, phone, email, utilDate, role, cost);
                if(cost >= 10000){
                    user.setLevel(Level.GOLD);
                }
                else if(cost >= 3000){
                    user.setLevel(Level.SILVER);
                }
                else {
                    user.setLevel(Level.BRONZE);
                }
                if ("0".equals(role)) {
                    userList.add(user);
                }
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        String json = JSON.toJSONString(userList);
        response.getWriter().print(json);
        //request.setAttribute("userList", userList);
        //System.out.println(userList.size());
        //request.getRequestDispatcher("/userlist.jsp").forward(request, response);
    }

    /**
     * 管理员修改密码，获取前端表单数据后，与session中的密码验证，若验证失败，响应失败信息
     * 否则，连接数据库提交密码更改
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doAdminChange(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String newPassword = request.getParameter("newPassword");
        boolean status = Boolean.parseBoolean(request.getParameter("status"));
        String hashedPassword = Encryption.encodePassword(newPassword);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");

        //配对成功，连接数据库修改

        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        if(status){

        }
        try {
            conn = DBUtils.getConnection();
            String sql = "update user set password=? where username=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, hashedPassword);
            ps.setString(2, user.getUsername());
            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, null);
        }
        if(count == 0){
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
        else {
            response.getWriter().print("{\"st\":"+true+"}");
        }
    }

    /**
     * 退出登录，销毁Session和Cookie
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        if(session != null){
            //销毁session
            session.removeAttribute("user");
            //销毁cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    //删除cookie
                    cookie.setMaxAge(0);
                    //设置路径
                    cookie.setPath(request.getContextPath());
                    //覆盖前端cookie
                    response.addCookie(cookie);
                }
            }
            response.sendRedirect(request.getContextPath());
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //验证用户名
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean flag = false;
        String role = null;
        String totalCost = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String passwordDB = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "select role, totalcost, password from user where username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()){
                role = rs.getString("role");
                totalCost = rs.getString("totalcost");
                passwordDB = rs.getString("password");
                if(BCrypt.checkpw(password, passwordDB)){
                    flag = true;
                }
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
            user.setPassword(passwordDB);
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
            session.setAttribute("user", user);
            String f = request.getParameter("f");
            if("1".equals(f)){
                Cookie cookie1 = new Cookie("username", username);
                Cookie cookie2 = new Cookie("password", passwordDB);
                cookie1.setMaxAge(60 * 60 * 24 * 10);
                cookie2.setMaxAge(60 * 60 * 24 * 10);
                cookie1.setPath(request.getContextPath());
                cookie2.setPath(request.getContextPath());
                response.addCookie(cookie1);
                response.addCookie(cookie2);
            }
            if("1".equals(role)){
                response.sendRedirect(request.getContextPath() + "/select.jsp");
            }
            else {
                response.sendRedirect(request.getContextPath() + "/userSelect.jsp");
            }
        }
        else {
            //验证失败
            out.print("用户名或密码错误！！");
        }
    }
}
