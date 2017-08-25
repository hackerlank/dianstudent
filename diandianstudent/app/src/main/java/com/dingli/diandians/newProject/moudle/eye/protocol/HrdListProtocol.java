package com.dingli.diandians.newProject.moudle.eye.protocol;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lwq on 2017/6/6.
 */

public class HrdListProtocol implements Serializable {
      public List<HrdProtocol>   data;
      public int totalCount;
      public int pageCount;
      public int offset;
      public  int  limit;
}
