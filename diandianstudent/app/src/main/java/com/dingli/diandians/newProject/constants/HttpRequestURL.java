package com.dingli.diandians.newProject.constants;


public class HttpRequestURL {
    /**
     * 用户相关
     */
    /*获取token*/
    public static final String ACCOUNT_VALIDATE_URL = BaseHttpURL.PASSPORT_LOAD_BASEURL + "/oauth/token";
    /*获取用户信息*/
    public static final String ACCOUNT_INFO_URL = BaseHttpURL.LOAD_BASEURL + "/api/phone/v1/user/info";

    /**
     * 慧眼相关
     */
    /*获取已发布的直播列表*/
    public static final String QUERYLIVELIST_URL = BaseHttpURL.HY_LOAD_BASEURL + "/api/phone/v1/Live/queryLiveList";
    /*获取预告信息*/
    public static final String RECENTLYNOLIVE_URL = BaseHttpURL.HY_LOAD_BASEURL + "/api/phone/v1/Live/recentlyNoLive";
    /*就业心理3条文章*/
    public static final String ARTICLE_LIST_URL = BaseHttpURL.HY_LOAD_BASEURL + "/api/web/v1/articleManagement/articleManagementShow/articleList3";
    /*计算当前直播在线人数*/
    public static final String ONLINENUMBER = BaseHttpURL.HY_LOAD_BASEURL +  "/api/phone/v1/Live/onlineNumber";
    /*取消订阅内容*/
    public static final String CANCELSUBSCRIPTION = BaseHttpURL.HY_LOAD_BASEURL +  "/api/phone/v1/Live/cancelLiveSubscription";
    /*订阅*/
    public static final String SAVESUBSCRIPTION = BaseHttpURL.HY_LOAD_BASEURL +  "/api/phone/v1/Live/saveLiveSubscription";
    /*保存评论信息*/
    public static final String SAVEMESSAGECOMMENT = BaseHttpURL.HY_LOAD_BASEURL +  "/api/phone/v1/Live/saveMessageComment";
    /*查看评论列表*/
    public static final String QUERYMESSAGECOMMENTLIST= BaseHttpURL.HY_LOAD_BASEURL +  "/api/phone/v1/Live/queryMessageCommentList";
    /*Holland和MBTI推荐共同推荐岗位*/
    public static final String GETHOLLANDJOINMBTI= BaseHttpURL.HY_LOAD_BASEURL +  "/api/web/v1/praEvaluation/holland/getHollandJoinMBTI";
    /*判断是否有测试报告*/
    public static final String GETHOLLANDREPORTS= BaseHttpURL.HY_LOAD_BASEURL +  "/api/web/v1/praEvaluation/holland/getHollandReports";


    /**
     * 课表相关
     */
    /*获取当前时间的周数*/
    public static final String GETWEEK= BaseHttpURL.LOAD_BASEURL +  "/api/phone/v1/week/get";
    /*获取当前周所有课程*/
    public static final String GETCOURSEWEEK= BaseHttpURL.LOAD_BASEURL +  "/api/phone/v1/student/courseweek/get";
    /*获取本学期所有周*/
    public static final String GETWEEKLIST= BaseHttpURL.LOAD_BASEURL +  "/api/phone/v1/week/getList";
}
