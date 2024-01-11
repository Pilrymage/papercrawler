package org.example.model;

public class Paper {
    private Integer id;
    private String title;
    private String author;
    private String source;
    private String time;
    private String email;
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id=id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getAuthor(){
        return author;
    }
    public void setAuthor(String author){
        this.author=author;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        if (email == null) {
            email = "";
        }
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void  setTime(String time) {
        if (time == null) {
            time = "";
        }
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (source == null) {
            source = "";
        }
        this.source = source;
    }
}
