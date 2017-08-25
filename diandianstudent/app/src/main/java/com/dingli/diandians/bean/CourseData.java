package com.dingli.diandians.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by admin on 2016/4/2.
 */
public class CourseData {

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

    public static class CourseListEntity {
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

        private int id;
        private String dayOfWeek;
        private String classRoom;
        private String courseName;
        private String classBeginTime;
        private String classEndTime;
        private String teach_time;
        private String weekName;
        private String whichLesson;
        private int lessonOrderNum;
        private String teacher;
        private String rollcallType;
        private String periodType;
        private int scheduleId;
        private int teacherId;
        private int studentId;
        private int studentScheduleId;
        private int classId;
        private int semesterId;
        private int courseId;
        private String isAutomatic;
        private  boolean canReport;
        private String  localtion;
        private String  rollCallEndTime;
        private String type;
        private  boolean haveReport;
        private String isOpen;
        private int lateTime;
        private int reuser;
        private String rollCallTypeOrigin;
        private int calCount;
        private int deviation;
        private int confiLevel;
        private String signTime;
        private int course_later_time;
        private String address;
        private  boolean rollCall;

        public void setId(int id) {
            this.id = id;
        }

        public void setDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
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

        public int getId() {
            return id;
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


        /**
         *
         * @return
         */
        public boolean isEmpty() {
            if (TextUtils.isEmpty(courseName)) return true;
            return false;
        }

        @Override
        public boolean equals(Object o) {
            try {
                return courseName.equals(((CourseListEntity) o).courseName);
            } catch (ClassCastException e) {
                return false;
            }
        }
    }
}
