package ru.donex.rssreader;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.donex.rssreader.API.APILiga;
import ru.donex.rssreader.Adapters.RSSListAdapter;
import ru.donex.rssreader.Models.RSSItem;
import ru.donex.rssreader.Support.InternetTools;


public class MainActivity extends AppCompatActivity {

    private Context thisContext = this;
    private String CLASS_NAME = "MainActivity";

    private int ITEMS_IN_LIST = 3;
    private int ITEMS_COUNT;
    private int PAGES_COUNT;

    private APILiga api;
    private InternetTools internetTools = new InternetTools(thisContext);

    private Button[] paginationButtons;
    private ArrayList<RSSItem> items;
    private ArrayList<RSSItem> sortedItems;

    ListView FeedList;
    LinearLayout paginationLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStart();
    }

    // действия при загрузке фрагмента
    public void fragmentStart(){

        elementsInit();
        setListeners();
        reload();
    }


    public void reload(){

        if(internetTools.isInternetAvailable())
            showElements();
        else
            showAlertDialog();
    }

    // установка обработчиков событий
    public void setListeners(){

        FeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openNewsItem(
                        sortedItems.get(position).getLink(),
                        sortedItems.get(position).getTitle()
                );
            }
        });
    }

    // инициализация view элементов
    public void elementsInit(){
        FeedList = findViewById(R.id.lvRSSList);
    }

    // установка адаптера для списка
    public void showElements(){

        api = new APILiga();
        items = new ArrayList<>(api.getItems());
        ITEMS_COUNT = items.size();
        if( ITEMS_COUNT > 0 ) { // проверка на существование новостей

            /*
                вывод без панинации

                RSSListAdapter listAdapter = new RSSListAdapter(this, items);
                FeedList.setAdapter(listAdapter);
            */

            /*
                 вывод с пагинацией
            */

            pagination();
            loadNewsList(0);
            setActivePage(0);

        }else {

            Log.v(CLASS_NAME + " showElements ", "elements not found");
            Toast.makeText(this, getString(R.string.rss_empty), Toast.LENGTH_SHORT).show();
        }
    }

    // вывод сообщения об отсутсвии интернета
    protected void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(thisContext);
        builder.setMessage(R.string.dialog_no_internet_connection)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.dialog_retry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reload();
                    }
                })
                .setNegativeButton(R.string.dialog_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onBackPressed();
                    }
                });

        builder.create().show();
    }

    // вызов нового Activity с полной новостью
    public void openNewsItem(String newsUrl, String newsTitle){

        Intent intent = new Intent(this, NewsReadActivity.class);
        intent.putExtra("URL", newsUrl);
        intent.putExtra("TITLE", newsTitle);
        startActivity(intent);
    }

    // расчет пагинации
    private void pagination(){
        int val = ITEMS_COUNT % ITEMS_IN_LIST;
        val = val == 0 ? 0 : 1;
        PAGES_COUNT = ITEMS_COUNT / ITEMS_IN_LIST + val;

        paginationLayout = findViewById(R.id.pagination);

        paginationButtons = new Button[PAGES_COUNT];

        for(int i = 0; i < PAGES_COUNT; i++){
            paginationButtons[i] = new Button(this);
            paginationButtons[i]
                    .setBackgroundColor(getResources()
                            .getColor(android.R.color.transparent));
            paginationButtons[i].setText("" + (i + 1));

            LinearLayout.LayoutParams paginationLayoutParameters = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            paginationLayout.addView(paginationButtons[i], paginationLayoutParameters);

            final int j = i;
            paginationButtons[j].setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){

                    loadNewsList(j);
                    setActivePage(j);
                }
            });
        }

    }

    // выделение активной страницы
    private void setActivePage(int index){

        this.setTitle(
                getString(R.string.pagination_page)
                + " " + (index + 1) + " "
                + getString(R.string.pagination_of)
                + " " + PAGES_COUNT
        );

        for(int i = 0; i < PAGES_COUNT; i++){
            if(i==index){
                paginationButtons[index]
                        .setBackgroundDrawable(getResources()
                                .getDrawable(R.drawable.ic_launcher_background));
                paginationButtons[i]
                        .setTextColor(getResources()
                                .getColor(android.R.color.white));
            }
            else{
                paginationButtons[i]
                        .setBackgroundColor(getResources()
                                .getColor(android.R.color.transparent));
                paginationButtons[i]
                        .setTextColor(getResources()
                                .getColor(android.R.color.black));
            }
        }

    }

    // загрузка списка по страницам
    private void loadNewsList(int number){
        sortedItems = new ArrayList<>();

        int start = number * ITEMS_IN_LIST;

        for(int i = start; i < start + ITEMS_IN_LIST; i++){
            if(i<items.size())
                sortedItems.add(items.get(i));
            else
                break;
        }

        RSSListAdapter sd = new RSSListAdapter(this, sortedItems);
        FeedList.setAdapter(sd);
    }

}
