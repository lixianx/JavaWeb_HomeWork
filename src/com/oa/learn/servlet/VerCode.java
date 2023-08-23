package com.oa.learn.servlet;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.oa.learn.utils.CodeUtil;
import com.oa.learn.utils.DBUtils;
import com.oa.learn.utils.Sms;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/sendCode")
public class VerCode extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phone = request.getParameter("phone");
        String code = CodeUtil.generateRandomNumber();
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = Sms.sendSms(phone, code);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }
        if(sendSmsResponse != null){
            if(sendSmsResponse.getCode().equals("OK")){
                long l = System.currentTimeMillis();
                String date = String.valueOf(l);

                Connection conn = null;
                PreparedStatement ps = null;
                int count = 0;
                try {
                    conn = DBUtils.getConnection();
                    String sql = "insert into code(phone, code, date) values(?, ?, ?)";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, phone);
                    ps.setString(2, code);
                    ps.setString(3, date);
                    count = ps.executeUpdate();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    DBUtils.close(conn, ps, null);
                }
                if(count == 0){
                    response.sendRedirect(request.getContextPath() + "/error.jsp");
                }
            }
            response.getWriter().print("{\"status\":\""+sendSmsResponse.getCode()+"\"}");
        }
    }
}
