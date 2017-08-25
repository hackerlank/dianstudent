package com.dingli.diandians.newProject.http;
import com.dingli.diandians.newProject.constants.HttpRequestURL;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.ArticleProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollandJoinMBTIProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HollantHollandReportsProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.HrdListProtocol;
import com.dingli.diandians.newProject.moudle.eye.protocol.MessageCommentListProtocl;
import com.dingli.diandians.newProject.moudle.eye.protocol.NoliveProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CourseListProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CurrentWeekProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.WeekListProtocol;
import com.dingli.diandians.newProject.moudle.user.protocol.LoginResultProtocol;
import com.dingli.diandians.newProject.moudle.user.protocol.UserProtocol;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by lwq on 2017/5/31.
 */

public interface ApiService {

    @POST(HttpRequestURL.ACCOUNT_VALIDATE_URL)/*获取用户getToken*/
    Call<BaseProtocol<LoginResultProtocol>> getToken(@QueryMap Map<String, String> params);

    @POST(HttpRequestURL.ACCOUNT_VALIDATE_URL)/*获取用户信息*/
    Observable<BaseProtocol<LoginResultProtocol>> getLoginToken(@QueryMap Map<String, String> params);

    @GET(HttpRequestURL.ACCOUNT_INFO_URL)/*获取用户信息*/
    Observable<BaseProtocol<UserProtocol>> getUserInfo();

    @GET(HttpRequestURL.QUERYLIVELIST_URL)/*获取已发布的直播列表*/
    Observable<BaseProtocol<HrdListProtocol>> queryLiveList(@QueryMap Map<String, String> params);
    @GET(HttpRequestURL.ARTICLE_LIST_URL)/*就业心理3条文章*/
    Observable<BaseProtocol<List<ArticleProtocol>>> queryArticleList();
    @GET(HttpRequestURL.RECENTLYNOLIVE_URL)/*获取预告信息*/
    Observable<BaseProtocol<NoliveProtocol>> recentlyNoLive();

    @GET(HttpRequestURL.ONLINENUMBER)/*计算当前直播在线人数*/
    Observable<BaseProtocol<Object>> onlineNumber(@Query("liveId") String data);

    @GET(HttpRequestURL.QUERYMESSAGECOMMENTLIST)/*查看评论列表*/
    Observable<BaseProtocol<MessageCommentListProtocl>> queryMessageCommentList(@Query("pageNumber") String pageNumber,@Query("pageSize") String pageSize,@Query("liveId") String data);

    @POST(HttpRequestURL.SAVEMESSAGECOMMENT)/*保存评论信息*/
    Observable<BaseProtocol<Object>> saveMessageComment(@Body RequestBody body);

    @POST(HttpRequestURL.SAVESUBSCRIPTION) /*订阅*/
    Observable<BaseProtocol<Object>> saveLiveSubscription(@Body RequestBody body);

    @POST(HttpRequestURL.CANCELSUBSCRIPTION) /*取消订阅内容*/
    Observable<BaseProtocol<Object>> cancelLiveSubscription(@Body RequestBody body);

    @GET(HttpRequestURL.GETHOLLANDJOINMBTI) /*Holland和MBTI推荐共同推荐岗位*/
    Observable<BaseProtocol<HollandJoinMBTIProtocol>> getHollandJoinMBTI();
    @GET(HttpRequestURL.GETHOLLANDREPORTS) /*判断是否有测试报告*/
    Observable<BaseProtocol<HollantHollandReportsProtocol>> getHollandReports(@Query("evaluationId") String data);


    @GET(HttpRequestURL.GETWEEK) /*获取当前时间的周数*/
    Observable<BaseProtocol<CurrentWeekProtocol>> getWeek();
    @GET(HttpRequestURL.GETCOURSEWEEK) /*获取选中周所有课程*/
    Observable<BaseProtocol<List<CourseListProtocol>>> getCourseweek(@Query("weekId") int data);
    @GET(HttpRequestURL.GETWEEKLIST) /*获取本学期所有周*/
    Observable<BaseProtocol<WeekListProtocol>> getWeekList();
}
