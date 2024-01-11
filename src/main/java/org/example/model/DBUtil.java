package org.example.model;
import java.sql.*;

public class DBUtil
{
    private static final String URL="jdbc:sqlite:papers.db";
    private static Connection connection=null;

    // 测试生成一个数据库
    static {
        try {
//             获得连接
            connection = DriverManager.getConnection(URL);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

        } catch(SQLException e) {

            System.err.println(e.getMessage());
        }
    }
    // 连接数据库
    public static Connection getConnection(){
        return connection;
    }
    // 测试连接数据库
}