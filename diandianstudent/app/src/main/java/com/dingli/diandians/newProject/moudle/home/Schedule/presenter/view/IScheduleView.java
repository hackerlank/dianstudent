package com.dingli.diandians.newProject.moudle.home.Schedule.presenter.view;
import com.dingli.diandians.newProject.http.base.view.IBaseView;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CourseListProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.CurrentWeekProtocol;
import com.dingli.diandians.newProject.moudle.home.Schedule.protocol.WeekListProtocol;

import java.util.List;

/**
 * Created by lwq on 2017/6/2.
 */

public interface IScheduleView extends IBaseView {
    void getCourseListSuccess(List<CourseListProtocol> courseListProtocols);
    void getCourseListFailure(String message);
    void getCurrentWeekSuccess(CurrentWeekProtocol currentWeekProtocol);
    void getCurrentWeekFailure(String message);
    void getWeekListSuccess(WeekListProtocol weekListProtocol);
    void getWeekListFailure(String message);

}
