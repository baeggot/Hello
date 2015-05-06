package com.baeflower.hello.parsing;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baeflower.hello.R;
import com.baeflower.hello.parsing.adapter.GoogleNewsAdapter;
import com.baeflower.hello.parsing.model.GoogleNews;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ParsingXmlActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private List<GoogleNews> googleNewsList;
    // private ArrayAdapter arrayAdapter;
    private GoogleNewsAdapter googleNewsAdapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parsing_xml);

        // init Data
        googleNewsList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_parsing_xml);

        listView.setOnItemClickListener(this);


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        // XML 데이터를 읽어옴 (구글 뉴스 RSS)
        String urlStr = "https://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&output=rss";
        try {
            URL url = new URL(urlStr);
            //InputStream in = url.openStream();
            InputStream in = url.openConnection().getInputStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            // XmlPullParser에 XML 데이터와 인코딩 방식을 입력
            parser.setInput(in, "utf-8");

            int eventType = parser.getEventType(); // Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)
            boolean isItemTag = false;
            boolean isTitleTag = false;
            boolean isLinkTag = false;

            String tagName = "";
            String data = "";
            String link = "";
            GoogleNews googleNews = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // 0 : START_DOCUMENT
                if (eventType == XmlPullParser.START_TAG) { // 2
                    tagName = parser.getName();

                    if ("item".equals(tagName)) {
                        googleNews = new GoogleNews();
                        isItemTag = true;
                    } else if ("title".equals(tagName)) {
                        isTitleTag = true;
                    } if ("link".equals(tagName)) {
                        isLinkTag = true;
                    }

                } else if (eventType == XmlPullParser.TEXT && isItemTag) { // 4

                    data = parser.getText();

                    if (isTitleTag) {
                        googleNews.setTitle(data);
                    } else if (isLinkTag) {
                        googleNews.setLink(data);
                    }

                } else if (eventType == XmlPullParser.END_TAG) { // 3

                    tagName = parser.getName();

                    if (tagName.equals("item")) {
                        googleNewsList.add(googleNews);
                        isItemTag = false;
                    } else if (tagName.equals("title")) {
                        isTitleTag = false;
                    } else if (tagName.equals("link")) {
                        isLinkTag = false;
                    }
                }


                eventType = parser.next();
            } // while

        } catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "IOException", Toast.LENGTH_SHORT).show();
        } catch (XmlPullParserException e) {
            Toast.makeText(getApplicationContext(), "XmlPullParserException", Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "NullPointerException", Toast.LENGTH_SHORT).show();
        }

        // adapter
        // arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, googleNewsList);
        googleNewsAdapter = new GoogleNewsAdapter(getApplicationContext(), googleNewsList);

        // view
        listView.setAdapter(googleNewsAdapter);

    }   // onCreate


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String link = googleNewsList.get(position).getLink();

        Intent intent = new Intent(getApplicationContext(), ParsingWebviewActivity.class);
        intent.putExtra("link", link); // url 을 intent 실어서 activity를 호출
        startActivity(intent);
    }
}
