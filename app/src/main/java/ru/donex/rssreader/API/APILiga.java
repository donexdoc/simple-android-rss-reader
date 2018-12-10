package ru.donex.rssreader.API;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import ru.donex.rssreader.Models.RSSItem;

public class APILiga{

    private String CLASS_NAME = "APILiga";
    private String RSS_URL = "https://www.liga.net/tech/technology/rss.xml";



    private URL RSSUrl;
    private ArrayList<SyndEntry> RSSElements = new ArrayList<>();
    private ArrayList<RSSItem> items = new ArrayList<>();

    // инициализация и загрузка элементов
    public APILiga(){
        try {
            RSSUrl = new URL(RSS_URL);

            RSSElements.addAll(new FeedRequest().execute().get().getEntries());
            formatElements();

        }catch (java.net.MalformedURLException malformedException){
            Log.e(CLASS_NAME + " APILiga() ","java.net.MalformedURLException: "
                    + malformedException.getMessage());

        }catch (Exception otherException){
            Log.e(CLASS_NAME + " APILiga() ","exception: " + otherException.toString());
        }
    }


    // загрузка RSS
    class FeedRequest extends AsyncTask<Void, Void, SyndFeed>{

        SyndFeed RSSFeed;

        @Override
        protected SyndFeed doInBackground(Void... arg) {
            try {
                SyndFeedInput input = new SyndFeedInput();
                RSSFeed = input.build(new XmlReader(RSSUrl));
            }catch (Exception otherException){
                Log.e("FeedRequest","exception: " + otherException.toString());
            }
            return RSSFeed;
        }

        @Override
        protected void onPostExecute(SyndFeed feedResult) {
            super.onPostExecute(feedResult);
        }
    }

    // выборка необходимой информации
    private void formatElements(){
        for(SyndEntry element : RSSElements){
            items.add(new RSSItem(
                    element.getTitle(),
                    element.getPublishedDate(),
                    element.getEnclosures().get(0).getUrl(),
                    element.getDescription().getValue(),
                    element.getUri())
            );
        }
    }

    // получение списка типа SyndEntry
    public List<SyndEntry> getRSSElements(){
        return RSSElements;
    }

    // получение списка RSSItem
    public List<RSSItem> getItems(){
        return items;
    }

}
