package com.dingli.diandians.newProject.moudle.eye.protocol;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lwq on 2017/6/6.
 */

public class HollandJoinMBTIProtocol implements Serializable {
        public List<Job> jobSet;
        public class Job implements Serializable {
                public int jobsId;
                public String name;
        }
}
