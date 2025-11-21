package com.github.rapid.common.util;

import java.sql.*;

public class SqlServerTest {
    // 数据库连接参数（请根据实际修改）
    private static final String URL = "jdbc:sqlserver://218.245.96.117:1433;databaseName=LZTest;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "newsapSa*4658#_ScLz_1527";

    public static void main(String[] args) throws ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. 注册驱动（新版本JDBC自动注册，可省略）
             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 2. 获取数据库连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 3. 创建Statement对象
            stmt = conn.createStatement();

            // 4. 执行查询
            String sql = "SELECT COUNT(*) AS total FROM WTR1";
            rs = stmt.executeQuery(sql);

            // 5. 处理结果集
            if (rs.next()) {
                int count = rs.getInt("total");
                System.out.println("记录总数: " + count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
