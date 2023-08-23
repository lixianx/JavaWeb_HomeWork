package com.oa.learn.servlet;


import com.alibaba.fastjson.JSON;
import com.oa.learn.bean.GoodInCar;
import com.oa.learn.bean.Goods;
import com.oa.learn.bean.User;
import com.oa.learn.utils.DBUtils;
import com.oa.learn.utils.TimesCompare;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@WebServlet({"/goods/list", "/goods/detail", "/goods/delete", "/goods/save",
"/goods/modify", "/goods/customer", "/goods/car", "/goods/lookCar", "/goods/delCar"})
@MultipartConfig
public class GoodsServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if("/goods/list".equals(servletPath)){
            doList(request, response);
        }
        else if("/goods/detail".equals(servletPath)){
            doDetail(request, response);
        }
        else if("/goods/delete".equals(servletPath)){
            doDel(request, response);
        }
        else if("/goods/save".equals(servletPath)){
            doSave(request, response);
        }
        else if("/goods/modify".equals(servletPath)){
            doModify(request, response);
        }
        else if("/goods/customer".equals(servletPath)){
            doCustomer(request, response);
        }
        else if("/goods/car".equals(servletPath)){
            doCar(request, response);
        }
        else if("/goods/lookCar".equals(servletPath)){
            doLookCar(request, response);
        }
        else if("/goods/delCar".equals(servletPath)){
            doDelCar(request, response);
        }
    }

    /**
     * 删除购物车中的字段
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDelCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String no = request.getParameter("no");
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        boolean flag = false;
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtils.getConnection();
            String sql = "delete from usercar where goodid=? and username=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no);
            ps.setString(2, username);
            count = ps.executeUpdate();
            if(count == 1){
                flag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, null);
        }
        if(flag){
            response.sendRedirect(request.getContextPath() + "/goods/lookCar");
        }
        else {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    /**
     * 查看购物车
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doLookCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        List<GoodInCar> carList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = """
                    select
                    u.goodid, g.name, g.retailPrice, u.num, u.maxnum, g.images
                    from
                    usercar u
                    join
                    goods g
                    on
                    g.no = u.goodid
                    where
                    u.username = ?;
                    """;
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while(rs.next()){
                String id = rs.getString("goodid");
                String name = rs.getString("name");
                String price = rs.getString("retailPrice");
                String num = rs.getString("num");
                String maxNum = rs.getString("maxnum");
                byte[] image = rs.getBytes("images");
                //封装对象响应到前端
                GoodInCar car = new GoodInCar(id, name, Integer.parseInt(num), Integer.parseInt(maxNum), image, Double.parseDouble(price));
                carList.add(car);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }

        request.setAttribute("carList", carList);

        request.getRequestDispatcher("/mycar.jsp").forward(request, response);
    }

    /**
     * 加入购物车
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String no = request.getParameter("no");
        String maxNum = request.getParameter("maxNum");
        int count = 0;
        boolean flag = false;
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        String username = user.getUsername();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "select * from usercar where goodid=? and username=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no);
            ps.setString(2, username);
            rs = ps.executeQuery();
            if(!rs.next()){
                String add = "insert into usercar(username, goodid, num, maxnum) values(?, ?, ?, ?)";
                ps = conn.prepareStatement(add);
                ps.setString(1, username);
                ps.setString(2, no);
                ps.setString(3, "1");
                ps.setString(4, maxNum);
                count = ps.executeUpdate();
                if(count == 1){
                    flag = true;
                }
            }
            else {
                flag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        response.getWriter().print("{\"status\":"+flag+"}");
    }

    /**
     * 用户页面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keywords = request.getParameter("keywords");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        List<Goods> list = new ArrayList<>();
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from goods where name like ? and amount != 0";
            ps = conn.prepareStatement(sql);
            ps.setString(1, keywords + "%");
            rs = ps.executeQuery();
            while(rs.next()){
                String no = rs.getString("no");
                String name = rs.getString("name");
                String retailPrice = rs.getString("retailPrice");
                String amount = rs.getString("amount");
                byte[] image = rs.getBytes("images");
                String imageDate = Base64.getEncoder().encodeToString(image);
                Goods goods = new Goods();
                goods.setNo(no);
                goods.setName(name);
                goods.setRetailPrice(retailPrice);
                goods.setAmount(amount);
                goods.setImage(imageDate);

                String sql2 = "select times from saled where goodid=?";
                ps = conn.prepareStatement(sql2);
                ps.setString(1, no);
                rs2 = ps.executeQuery();
                if(rs2.next()){
                    int times = rs2.getInt("times");
                    goods.setTimes(times);
                }
                else {
                    goods.setTimes(0);
                }
                list.add(goods);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        Collections.sort(list, new TimesCompare());
        String json = JSON.toJSONString(list);
        response.getWriter().print(json);

    }

    /**
     * 修改商品信息
     * @param request
     * @param response
     */
    private void doModify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String no = request.getParameter("no");
        String name = request.getParameter("name");
        String producer = request.getParameter("producer");
        String date = request.getParameter("date");
        String model = request.getParameter("model");
        String buyPrice = request.getParameter("buyPrice");
        String retailPrice = request.getParameter("retailPrice");
        String amount = request.getParameter("amount");
        Part part = request.getPart("image");
        InputStream inputStream = part.getInputStream();

        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;

        try {
            conn = DBUtils.getConnection();
            String sql = "update goods set name=?, producer=?, date=?, model=?, buyPrice=?, retailPrice=?, amount=?, images=? where no=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, producer);
            ps.setString(3, date);
            ps.setString(4, model);
            ps.setString(5, buyPrice);
            ps.setString(6, retailPrice);
            ps.setString(7, amount);
            ps.setBinaryStream(8, inputStream);
            ps.setString(9, no);

            count = ps.executeUpdate();


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
            DBUtils.close(conn, ps, null);
        }

        if(count == 1){
            //request.getRequestDispatcher("/goods/list").forward(request, response);
            response.sendRedirect(request.getContextPath() + "/goods/list");
        }
        else {
            //request.getRequestDispatcher("/error.html").forward(request, response);
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }

    }

    /**
     * 保存商品信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String producer = request.getParameter("producer");
        String date = request.getParameter("date");
        String model = request.getParameter("model");
        String buyPrice = request.getParameter("buyPrice");
        String retailPrice = request.getParameter("retailPrice");
        String amount = request.getParameter("amount");
        Part part = request.getPart("image");
        InputStream inputStream = part.getInputStream();

        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBUtils.getConnection();
            String sql = "insert into goods(name, producer, date, model, buyPrice, retailPrice, amount, images) values(?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, producer);
            ps.setString(3, date);
            ps.setString(4, model);
            ps.setString(5, buyPrice);
            ps.setString(6, retailPrice);
            ps.setString(7, amount);
            ps.setBinaryStream(8, inputStream);
            count = ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            DBUtils.close(conn, ps, null);
        }

        if(count == 1){
            //进行图片上传操作
            //request.getRequestDispatcher("/goods/list").forward(request, response);
            response.sendRedirect(request.getContextPath() + "/goods/list");
        }
        else {
            //request.getRequestDispatcher("/error.html").forward(request, response);
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    /**
     * 删除商品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDel(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String no = request.getParameter("no");
        Connection conn = null;
        PreparedStatement ps = null;
        int count = 0;
        try {
            conn = DBUtils.getConnection();
            String sql = "delete from goods where no=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no);
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

    /**
     * 根据部门编号获取部门详细信息。
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String no = request.getParameter("no");
        Goods good;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtils.getConnection();
            String sql = "select * from goods where no=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, no);
            rs = ps.executeQuery();

            if(rs.next()){
                String name = rs.getString("name");
                String producer = rs.getString("producer");
                String date = rs.getString("date");
                String buyPrice = rs.getString("buyPrice");
                String retailPrice = rs.getString("retailPrice");
                String model = rs.getString("model");
                String amount = rs.getString("amount");
                byte[] image = rs.getBytes("images");
                String imageData = Base64.getEncoder().encodeToString(image);
                //封装对象
                good = new Goods(no, name, producer, date, model, buyPrice, retailPrice, amount, imageData, 0);
                request.setAttribute("good", good);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }

        String forward = "/" + request.getParameter("f") + ".jsp";
        //转发
        request.getRequestDispatcher(forward).forward(request, response);
    }

    /**
     * 连接数据库，查询所有部门信息，跳转到JSP展示(管理员初始页面)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Goods> goodList = new ArrayList<>();
        //连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        //查验用户身份为管理员还是普通用户
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");

        try {
            conn = DBUtils.getConnection();
            String sql = "select * from goods where amount > 0";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            //遍历结果集
            while(rs.next()){
                String no = rs.getString("no");
                String name = rs.getString("name");
                String retailPrice = rs.getString("retailPrice");
                String amount = rs.getString("amount");
                byte[] image = rs.getBytes("images");
                String imageData = Base64.getEncoder().encodeToString(image);
                Goods goods = new Goods();
                goods.setNo(no);
                goods.setName(name);
                goods.setRetailPrice(retailPrice);
                goods.setAmount(amount);
                goods.setImage(imageData);
                String sql2 = "select times from saled where goodid=?";
                ps = conn.prepareStatement(sql2);
                ps.setString(1, no);
                rs2 = ps.executeQuery();
                if(rs2.next()){
                    int times = rs2.getInt("times");
                    goods.setTimes(times);
                }
                else {
                    goods.setTimes(0);
                }
                goodList.add(goods);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }

        //排序
        Collections.sort(goodList, new TimesCompare());

        request.setAttribute("goodList", goodList);

        //管理员
        if("1".equals(user.getRole())){
            request.getRequestDispatcher("/list.jsp").forward(request, response);
        }
        else {
            request.getRequestDispatcher("/clist.jsp").forward(request, response);
        }
    }
}
