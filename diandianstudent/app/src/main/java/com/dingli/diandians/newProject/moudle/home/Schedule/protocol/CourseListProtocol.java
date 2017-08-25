package com.dingli.diandians.newProject.moudle.home.Schedule.protocol;

import com.dingli.diandians.bean.TableData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lwq on 2017/7/6.
 */

public class CourseListProtocol implements Serializable{
    public String dayOfWeek;
    public List<CourseProtocol> courseList;
}
