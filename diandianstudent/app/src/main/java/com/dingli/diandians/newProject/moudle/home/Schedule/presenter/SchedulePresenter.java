package com.dingli.diandians.newProject.moudle.home.Schedule.presenter;
import com.dingli.diandians.newProject.constants.HttpRequestURL;
import com.dingli.diandians.newProject.http.base.presenter.BasePresenter;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.http.subscriber.BKSubscriber;
import com.dingli.diandians.newProject.moudle.home.Schedule.presenter.view.IScheduleView;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CourseListProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CurrentWeekProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.WeekListProtocol;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lwq on 2017/7/6.
 */

public class SchedulePresenter extends BasePresenter {

    private IScheduleView iScheduleView;
    public SchedulePresenter(IScheduleView iScheduleView){
        this.iScheduleView=iScheduleView;
    }

    /*获取当前时间的周数*/
    public void getCurrentWeek() {
        subscribe(utouuHttp(api().getWeek(), HttpRequestURL.GETWEEK)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<CurrentWeekProtocol>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<CurrentWeekProtocol> result) {
                        iScheduleView.getCurrentWeekSuccess(result.data);
                        if(null!=result&&null!=result.data){
                            getCourseweek(result.data.id);
                        }
                    }

                    @Override
                    protected void onFailure(String message) {
                        iScheduleView.getCurrentWeekFailure(message);
                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                        iScheduleView.onNetworkFailure(message);
                    }

                    @Override
                    protected void onLoginInvalid(String message) {
                        iScheduleView.onLoginInvalid(message);
                    }
                }));
    }

    /*获取选中周所有课程*/
    public void getCourseweek(int id) {
        subscribe(utouuHttp(api().getCourseweek(id), HttpRequestURL.GETCOURSEWEEK)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<List<CourseListProtocol>>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<List<CourseListProtocol>> result) {
                        iScheduleView.getCourseListSuccess(result.data);
                    }

                    @Override
                    protected void onFailure(String message) {
                        iScheduleView.getCourseListFailure(message);
                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                        iScheduleView.onNetworkFailure(message);
                    }

                    @Override
                    protected void onLoginInvalid(String message) {
                        iScheduleView.onLoginInvalid(message);
                    }
                }));
    }

    /*获取本学期所有周*/
    public void getWeekList() {
        subscribe(utouuHttp(api().getWeekList(), HttpRequestURL.GETWEEKLIST)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BKSubscriber<BaseProtocol<WeekListProtocol>>() {
                    @Override
                    protected void onSuccess(BaseProtocol<WeekListProtocol> result) {
                        iScheduleView.getWeekListSuccess(result.data);
                    }

                    @Override
                    protected void onFailure(String message) {
                        iScheduleView.getWeekListFailure(message);
                    }
                    @Override
                    protected void onNetworkFailure(String message) {
                        iScheduleView.onNetworkFailure(message);
                    }

                    @Override
                    protected void onLoginInvalid(String message) {
                        iScheduleView.onLoginInvalid(message);
                    }
                }));
    }
}
