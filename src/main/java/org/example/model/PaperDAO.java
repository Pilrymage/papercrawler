package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaperDAO {
    public void addPaper(Paper paper) throws SQLException {
        Connection connection = DBUtil.getConnection();
        String sql = "insert into paper(title,author,email,source,time) values(?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);  // 预编译
        ps.setString(1, paper.getTitle());
        ps.setString(2, paper.getAuthor());
        ps.setString(3, paper.getEmail());
        ps.setString(4, paper.getSource());
        ps.setString(5, paper.getTime());
        ps.execute();
    }

    public void deletePaper(Integer id) throws SQLException {
        Connection connection = DBUtil.getConnection();
        String sql = "delete from paper where id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        try {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePaper(Paper paper) throws SQLException {
        Connection connection = DBUtil.getConnection();
        String sql = "update paper set title=?,author=?,email=?,source=?,time=? where id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        try {
            ps.setString(1, paper.getTitle());
            ps.setString(2, paper.getAuthor());
            ps.setString(3, paper.getEmail());
            ps.setString(4, paper.getSource());
            ps.setString(5, paper.getTime());
            ps.setInt(6, paper.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Paper queryPaper(Integer id) throws SQLException {
        Connection connection = DBUtil.getConnection();
        String sql = "select * from paper where id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        System.out.println(rs.getInt("id")+" "+rs.getString("title")+" "+rs.getString("author")+" "+rs.getString("email"));
        Paper paper = new Paper();
        paper.setId(rs.getInt("id"));
        paper.setTitle(rs.getString("title"));
        paper.setAuthor(rs.getString("author"));
        paper.setEmail(rs.getString("email"));
        paper.setSource(rs.getString("source"));
        paper.setTime(rs.getString("time"));
        return paper;
    }

    public List<Paper> queryAllPaper() throws SQLException {
        Connection connection = DBUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from paper");
        List<Paper> list = new ArrayList<Paper>();
        while (rs.next()) {
            Paper paper = new Paper();
            paper.setId(rs.getInt("id"));
            paper.setTitle(rs.getString("title"));
            paper.setAuthor(rs.getString("author"));
            paper.setEmail(rs.getString("email"));
            paper.setSource(rs.getString("source"));
            paper.setTime(rs.getString("time"));
            list.add(paper);
        }
        return list;
    }

    public List<Paper> queryPaperByTitle(String title) throws SQLException {
        Connection connection = DBUtil.getConnection();
        String sql = "select * from paper where title like ?";  // 模糊查询
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, "%" + title + "%");
        ResultSet rs = ps.executeQuery();
        List<Paper> list = new ArrayList<Paper>();
        while (rs.next()) {
            Paper paper = new Paper();
            paper.setId(rs.getInt("id"));
            paper.setTitle(rs.getString("title"));
            paper.setAuthor(rs.getString("author"));
            paper.setEmail(rs.getString("email"));
            paper.setSource(rs.getString("source"));
            paper.setTime(rs.getString("time"));
            list.add(paper);
        }
        return list;
    }
}
