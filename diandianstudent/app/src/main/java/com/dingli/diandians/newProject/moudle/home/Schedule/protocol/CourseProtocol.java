package com.dingli.diandians.newProject.moudle.home.Schedule.protocol;
import java.io.Serializable;

/**
 * Created by lwq on 2017/7/6.
 *
 */

public class CourseProtocol implements Serializable{
    public int id;
    public String dayOfWeek;
    public String classRoom;
    public String courseName;
    public String classBeginTime;
    public String classEndTime;
    public String teach_time;
    public String weekName;
    public String whichLesson;
    public int lessonOrderNum;
    public String teacher;
    public String rollcallType;
    public String periodType;
    public int scheduleId;
    public int teacherId;
    public int studentId;
    public int studentScheduleId;
    public int classId;
    public int semesterId;
    public int courseId;
    public String isAutomatic;
    public  boolean canReport;
    public String  localtion;
    public String  rollCallEndTime;
    public String type;
    public  boolean haveReport;
    public String isOpen;
    public int lateTime;
    public int reuser;
    public String rollCallTypeOrigin;
    public int calCount;
    public int deviation;
    public int confiLevel;
    public String signTime;
    public int course_later_time;
    public String address;
    public  boolean rollCall;
}
