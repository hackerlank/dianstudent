package com.dingli.diandians.newProject.moudle.eye.protocol;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lwq on 2017/6/6.
 */

public class NoliveProtocol implements Serializable {
      public List<NoHrdProtocol> data;
      public int count;
      public class NoHrdProtocol{
            public  int id;
            public String title;
            public String name;
            public String coverPic;
            public String childPic;
            public  String status;
            public String publishTime;
            public  int userId;
            public  int  typeId;
            public  String liveStatus;

      }

}
