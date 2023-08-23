package com.oa.learn.servlet;

import com.alibaba.fastjson.JSON;
import com.oa.learn.bean.GoodInCar;
import com.oa.learn.bean.User;
import com.oa.learn.utils.DBUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet({"/goods/buy", "/goods/complete", "/goods/history"})
public class Buy extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if("/goods/buy".equals(path)){
            doBuy(request, response);
        }
        else if("/goods/complete".equals(path)){
            doComplete(request, response);
        }
        else if("/goods/history".equals(path)){
            doHistory(request, response);
        }
    }

    /**
     * 查看购买记录
     * @param request
     * @param response
     */
    private void doHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<GoodInCar> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = """
                    select
                    h.goodid, g.name, h.date, h.num, g.images
                    from
                    history h
                    join
                    goods g
                    on
                    g.no = h.goodid
                    where
                    h.username = ?;
                         """;
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                String id = rs.getString("goodid");
                String name = rs.getString("name");
                int num = rs.getInt("num");
                byte[] image = rs.getBytes("images");
                String date = rs.getString("date");
                GoodInCar good = new GoodInCar(id, name, num, 0, image, 0);
                good.setDate(dateFormat.parse(date));
                list.add(good);
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }

        String json = JSON.toJSONString(list);
        response.getWriter().print(json);
    }

    /**
     * 完成交易
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doComplete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HashMap<String, Integer> map = new HashMap<>();
        String costStr = request.getParameter("cost");
        costStr = costStr.substring(1);
        double cost = Double.parseDouble(costStr);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        double total = cost + user.getTotalConsume();
        LocalDateTime now = LocalDateTime.now();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "select * from usercar where username=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                String goodId = rs.getString("goodid");
                int num = rs.getInt("num");
                int maxNum = rs.getInt("maxnum");
                String sql5 = "INSERT INTO saled (goodid, times) VALUES (?, ?) ON DUPLICATE KEY UPDATE times = times + ?";
                ps = conn.prepareStatement(sql5);
                ps.setString(1, goodId);
                ps.setString(2, String.valueOf(num));
                ps.setString(3, String.valueOf(num));
                count = ps.executeUpdate();
                map.put(goodId, maxNum - num);
            }
            for (Map.Entry<String, Integer> entry: map.entrySet()) {
                String no = entry.getKey();
                String amount = String.valueOf(entry.getValue());
                String sql2 = "update goods set amount=? where no=?";
                ps = conn.prepareStatement(sql2);
                ps.setString(1, amount);
                ps.setString(2, no);
                count = ps.executeUpdate();
            }
            if (count == 1) {
                String sql3 = "update user set totalcost=? where username=?";
                ps = conn.prepareStatement(sql3);
                ps.setString(1, String.valueOf(total));
                ps.setString(2, username);
                count = ps.executeUpdate();

                String sql4 = """
                               INSERT INTO history (username, goodid, num, date)
                               SELECT uc.username, uc.goodid, uc.num, ?
                               FROM usercar uc
                               WHERE uc.username = ?;
                               """;
                ps = conn.prepareStatement(sql4);
                ps.setString(1, String.valueOf(now));
                ps.setString(2, username);
                count = ps.executeUpdate();

                String sql6 = "delete from usercar where username=?";
                ps = conn.prepareStatement(sql6);
                ps.setString(1, username);
                count = ps.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        if(count >= 1){
            response.getWriter().print("{\"cost\":"+total+"}");
        }
        else {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    /**
     * 更新用户数据库
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doBuy(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        String goodId = request.getParameter("id");
        String num = request.getParameter("quantity");

        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtils.getConnection();
            String sql = "update usercar set num=? where username=? and goodid=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, num);
            ps.setString(2, username);
            ps.setString(3, goodId);
            count = ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, null);
        }
    }
}
