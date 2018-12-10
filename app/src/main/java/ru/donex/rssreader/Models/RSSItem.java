package ru.donex.rssreader.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RSSItem {

    private String CLASS_NAME = "RSSItem";

    private String title;
    private Date date;
    private String image;
    private String description;
    private String link;

    /* Инициализация с параметрами */
    public RSSItem(
            String newTitle,
            Date newDate,
            String newImage,
            String newDescription,
            String newLink){

        title = newTitle;
        date = newDate;
        description = newDescription;
        image = newImage;
        link = newLink;
    }

    /* Get'еры */
    public String getTitle(){
        return title;
    }

    public String getDate(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public String getDescription(){
        return description;
    }

    public String getImage(){
            return image;
    }

    public String getLink(){
            return link;
    }
}
