package com.dingli.diandians.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2016/4/27.
 */
public class TableData {


    /**
     * dayOfWeek : 1
     * courseList : [{"id":34291,"dayOfWeek":"1","classRoom":"2506","courseName":"现代传感技术","classBeginTime":"08:00","classEndTime":"10:00","teach_time":"2016-05-16","weekName":"1","whichLesson":"第1～2节课","lessonOrderNum":1,"teacher":"","rollcallType":"","periodType":"2"},{"id":34323,"dayOfWeek":"1","classRoom":"2506","courseName":"现代传感技术","classBeginTime":"10:00","classEndTime":"12:00","teach_time":"2016-05-16","weekName":"1","whichLesson":"第3～4节课","lessonOrderNum":3,"teacher":"","rollcallType":"","periodType":"2"}]
     */

    private String dayOfWeek;
    private List<CourseListEntity> courseList;

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setCourseList(List<CourseListEntity> courseList) {
        this.courseList = courseList;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public List<CourseListEntity> getCourseList() {
        return courseList;
    }

    public static class CourseListEntity implements Serializable {
        /**
         "id": 0,
         "dayOfWeek": "2",
         "classRoom": "",
         "courseName": "计算机应用基础",
         "classBeginTime": "12:00",
         "classEndTime": "12:00",
         "teach_time": "2017-07-04",
         "weekName": "709",
         "whichLesson": "第3~4节课",
         "lessonOrderNum": 3,
         "teacher": "",
         "teacherId": 0,
         "studentId": 0,
         "studentScheduleId": 0,
         "classId": 0,
         "semesterId": 0,
         "courseId": 0,
         "rollcallType": "",
         "periodType": "2",
         "isAutomatic": "",
         "canReport": true,
         "localtion": "",
         "rollCallEndTime": "",
         "type": "",
         "haveReport": false,
         "isOpen": "",
         "lateTime": 0,
         "reuser": 0,
         "rollCallTypeOrigin": "",
         "calCount": 0,
         "deviation": 0,
         "confiLevel": 0,
         "signTime": "",
         "course_later_time": -1,
         "address": "",
         "rollCall": false
         */

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

        public void setId(int id) {
            this.id = id;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
        public void setScheduleId(int ScheduleId){
            this.scheduleId=scheduleId;
        }
        public void setClassRoom(String classRoom) {
            this.classRoom = classRoom;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public void setClassBeginTime(String classBeginTime) {
            this.classBeginTime = classBeginTime;
        }

        public void setClassEndTime(String classEndTime) {
            this.classEndTime = classEndTime;
        }

        public void setTeach_time(String teach_time) {
            this.teach_time = teach_time;
        }

        public void setWeekName(String weekName) {
            this.weekName = weekName;
        }

        public void setWhichLesson(String whichLesson) {
            this.whichLesson = whichLesson;
        }

        public void setLessonOrderNum(int lessonOrderNum) {
            this.lessonOrderNum = lessonOrderNum;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public void setRollcallType(String rollcallType) {
            this.rollcallType = rollcallType;
        }

        public void setPeriodType(String periodType) {
            this.periodType = periodType;
        }

        public int getId() {
            return id;
        }
        public int getscheduleId() {
            return scheduleId;
        }
        public String getDayOfWeek() {
            return dayOfWeek;
        }

        public String getClassRoom() {
            return classRoom;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getClassBeginTime() {
            return classBeginTime;
        }

        public String getClassEndTime() {
            return classEndTime;
        }

        public String getTeach_time() {
            return teach_time;
        }

        public String getWeekName() {
            return weekName;
        }

        public String getWhichLesson() {
            return whichLesson;
        }

        public int getLessonOrderNum() {
            return lessonOrderNum;
        }

        public String getTeacher() {
            return teacher;
        }

        public String getRollcallType() {
            return rollcallType;
        }

        public String getPeriodType() {
            return periodType;
        }
    }
}