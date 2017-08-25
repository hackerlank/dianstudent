package com.dingli.diandians.newProject.constants;

/**
 * 常量类
 * lwq
 * */
public class BKConstant {
    /*分页数据默认加载条数*/
    public static final int PAGE_SIZE = 20;
    /*Holland测试报告数*/
    public static int HLD_TEST_COUNT = 0;
    /*MBTI测试报告数*/
    public static int MBTI_TEST_COUNT = 0;
    /**
     * EventBus常量
     */
    public class EventBus {
        /*登入*/
        public static final String LOGIN_SUCCESS = "cn.dd.login.success";
        /*登出*/
        public static final String LOGIN_OUT = "cn.dd.login.out";

        /*直播页面关闭*/
        public static final String HRDCLOSE= "cn.hrd.close";
        /*预告列表页面关闭trailer*/
        public static final String TRAILERSCLOSE= "cn.trailers.close";
        /*直播列表刷新*/
        public static final String HRDLISTREFSH= "cn.hrdlist.rerfsh";

        public static final String WIFINETWORK= "cn.wifiNetwork";//Wifi网络
//        public static final String DATANETWORK= "cn.dataNetwork";//移动网络
//        public static final String NONETWORK= "cn.noNetwork";//无网络

        public static final String JOBREFSH= "cn.job.fresh";//就业刷新职位
    }
}
