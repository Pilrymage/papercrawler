package org.example.model;

import java.util.List;

public class PaperAction {
    public void add(Paper paper) throws Exception {
        PaperDAO paperDAO=new PaperDAO();
        paperDAO.addPaper(paper);
    }
    public void delete(Integer id) throws Exception {
        PaperDAO paperDAO=new PaperDAO();
        paperDAO.deletePaper(id);
    }
    public void update(Paper paper) throws Exception {
        PaperDAO paperDAO=new PaperDAO();
        paperDAO.updatePaper(paper);
    }
    public Paper query(Integer id) throws Exception {
        PaperDAO paperDAO=new PaperDAO();
        return paperDAO.queryPaper(id);
    }
    public List<Paper> queryAll() throws Exception {
        PaperDAO paperDAO=new PaperDAO();
        return paperDAO.queryAllPaper();
    }
    public List<Paper> queryByTitle(String title) throws Exception {
        PaperDAO paperDAO=new PaperDAO();
        return paperDAO.queryPaperByTitle(title);
    }


    // 测试
    public static void main(String[] args) {
        PaperDAO paperDAO=new PaperDAO();
        try {
            System.out.println(paperDAO.queryPaperByTitle("test").get(0).getAuthor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
