package com.oa.learn.utils;

import java.sql.*;
import java.util.ResourceBundle;

public class DBUtils {
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources.jdbc");
    private static String driver = bundle.getString("driver");
    private static String url = bundle.getString("url");
    private static String user = bundle.getString("user");
    private static String password = bundle.getString("password");

    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //获取连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     *
     * @param conn 连接对象
     * @param ps 数据库操作对象
     * @param rs 结果集对象
     */
    //释放资源
    public static void close(Connection conn, Statement ps, ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
