package com.oa.learn.servlet;

import com.google.gson.Gson;
import com.oa.learn.bean.Goods;
import com.oa.learn.bean.User;
import com.oa.learn.utils.DBUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@WebServlet("/goods/search")
public class SearchServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Goods> goodList = new ArrayList<>();
        //连接数据库
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //查验用户身份为管理员还是普通用户

        try {
            conn = DBUtils.getConnection();
            String sql = "select * from goods";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            //遍历结果集
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
                goodList.add(goods);
            }

        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtils.close(conn, ps, rs);
        }

        request.setAttribute("goodList", goodList);

        String keyword = request.getParameter("keyword").trim();

        List<Goods> searchResults = new ArrayList<>();
        for (Goods good : goodList) {
            if (good.getName().contains(keyword)) {
                searchResults.add(good);
            }
        }

        // 将符合搜索条件的商品数据转换为JSON并发送给客户端
        Gson gson = new Gson();
        String jsonSearchResults = gson.toJson(searchResults);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonSearchResults);
    }
}
