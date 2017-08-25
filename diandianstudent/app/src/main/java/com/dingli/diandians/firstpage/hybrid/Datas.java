package com.dingli.diandians.firstpage.hybrid;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by dingliyuangong on 2016/9/29.
 */
public class Datas {
    public JSONObject array;
    public JSONObject map;
    public String operateType;
    public String url;
    public String title;
    public String  json;
    public String type;
    public boolean isRefresh;
    public boolean isStatusBar;
    public String domainName;
    public String param1;
    public  Datas(){
    }
    public Datas(String json){
        this.json=json;
    }
}
