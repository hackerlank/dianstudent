package com.dingli.diandians.weipingjiao;

import java.util.List;

/**
 */
public class WeiPJ {

    /**
     * totalCount : 80
     * pageCount : 4
     * offset : 1
     * limit : 20
     * data : [{"scheduleId":113616,"periodName":"第7～8节课","beginTime":"16:00","endTime":"18:00","classroom":"七教202","courseName":"形势与政策2","teachTime":"2016-09-21"},{"scheduleId":113600,"periodName":"第5～6节课","beginTime":"14:00","endTime":"16:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-09-21"},{"scheduleId":113584,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-09-20"},{"scheduleId":113648,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教203","courseName":"工程项目融合实践（传感器应用+ Zigbee网络综合）","teachTime":"2016-09-19"},{"scheduleId":113632,"periodName":"第1～2节课","beginTime":"8:00","endTime":"10:00","classroom":"七教202","courseName":"Photoshop图象处理(拓展)","teachTime":"2016-09-19"},{"scheduleId":113615,"periodName":"第7～8节课","beginTime":"16:00","endTime":"18:00","classroom":"七教202","courseName":"形势与政策2","teachTime":"2016-09-14"},{"scheduleId":113599,"periodName":"第5～6节课","beginTime":"14:00","endTime":"16:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-09-14"},{"scheduleId":113647,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教203","courseName":"工程项目融合实践（传感器应用+ Zigbee网络综合）","teachTime":"2016-09-12"},{"scheduleId":113583,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-09-13"},{"scheduleId":113631,"periodName":"第1～2节课","beginTime":"8:00","endTime":"10:00","classroom":"七教202","courseName":"Photoshop图象处理(拓展)","teachTime":"2016-09-12"},{"scheduleId":113614,"periodName":"第7～8节课","beginTime":"16:00","endTime":"18:00","classroom":"七教202","courseName":"形势与政策2","teachTime":"2016-09-07"},{"scheduleId":113598,"periodName":"第5～6节课","beginTime":"14:00","endTime":"16:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-09-07"},{"scheduleId":113582,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-09-06"},{"scheduleId":113646,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教203","courseName":"工程项目融合实践（传感器应用+ Zigbee网络综合）","teachTime":"2016-09-05"},{"scheduleId":113630,"periodName":"第1～2节课","beginTime":"8:00","endTime":"10:00","classroom":"七教202","courseName":"Photoshop图象处理(拓展)","teachTime":"2016-09-05"},{"scheduleId":113613,"periodName":"第7～8节课","beginTime":"16:00","endTime":"18:00","classroom":"七教202","courseName":"形势与政策2","teachTime":"2016-08-31"},{"scheduleId":113597,"periodName":"第5～6节课","beginTime":"14:00","endTime":"16:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-08-31"},{"scheduleId":113581,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教201","courseName":"现代传感技术","teachTime":"2016-08-30"},{"scheduleId":113645,"periodName":"第3～4节课","beginTime":"10:00","endTime":"12:00","classroom":"七教203","courseName":"工程项目融合实践（传感器应用+ Zigbee网络综合）","teachTime":"2016-08-29"},{"scheduleId":113629,"periodName":"第1～2节课","beginTime":"8:00","endTime":"10:00","classroom":"七教202","courseName":"Photoshop图象处理(拓展)","teachTime":"2016-08-29"}]
     */

    private int totalCount;
    private int pageCount;
    private int offset;
    private int limit;
    /**
     * scheduleId : 113616
     * periodName : 第7～8节课
     * beginTime : 16:00
     * endTime : 18:00
     * classroom : 七教202
     * courseName : 形势与政策2
     * teachTime : 2016-09-21
     */

    private List<WeiPJDataBean> data;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<WeiPJDataBean> getData() {
        return data;
    }

    public void setData(List<WeiPJDataBean> data) {
        this.data = data;
    }

    public static class WeiPJDataBean {
        private int scheduleId;
        private String periodName;
        private String beginTime;
        private String endTime;
        private String classroom;
        private String courseName;
        private String teachTime;

        public int getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(int scheduleId) {
            this.scheduleId = scheduleId;
        }

        public String getPeriodName() {
            return periodName;
        }

        public void setPeriodName(String periodName) {
            this.periodName = periodName;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getClassroom() {
            return classroom;
        }

        public void setClassroom(String classroom) {
            this.classroom = classroom;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getTeachTime() {
            return teachTime;
        }

        public void setTeachTime(String teachTime) {
            this.teachTime = teachTime;
        }
    }
}
