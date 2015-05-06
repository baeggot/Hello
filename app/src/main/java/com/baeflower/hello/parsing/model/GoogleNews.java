
package com.baeflower.hello.parsing.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sol on 2015-03-30.
 */
public class GoogleNews {

    private Map<String, String> dataMap;
    private String title;
    private String link;

    public GoogleNews() {
        dataMap = new HashMap<>();
        title = "";
        link = "";
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }
    public String getDataMap(String key) { // key에 대한 data 반환
        return dataMap.get(key);
    }
    public void setDataMap(String key, String data) { // key, data로 map에 put
        dataMap.put(key, data);
    }
    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }


    public String getTitle() {return title;}
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return this.title;
    }
}
