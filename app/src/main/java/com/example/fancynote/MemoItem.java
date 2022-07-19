package com.example.fancynote;

public class MemoItem {
    String title;
    String content;
    String imagePath;
    String createDate;

    public MemoItem(){}

    public MemoItem(String title, String content, String imagePath, String createDate) {
        this.title = title;
        this.content = content;
        this.imagePath = imagePath;
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreateDate() {return createDate;}

    public void setCreateDate(String createDate){
        this.createDate = createDate;
    }

}

