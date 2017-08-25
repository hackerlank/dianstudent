package com.dingli.diandians.newProject.moudle.eye.protocol;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.dingli.diandians.common.Course;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lwq on 2017/6/6.
 */

public class HrdProtocol implements Serializable {
    public int id;
    public String createdBy;
    public String createdDate;
    public String lastModifiedBy;
    public String lastModifiedDate;
    public int deleteFlag;
    public String title;
    public String name;
    public String coverPic;
    public String childPic;
    public String data;
    public String status;
    public String publishTime;
    public String userId;
    public int typeId;
    public String liveStatus;
    public String subscriptionStatus;
    public int onlineNumber;

    public ViedoProtocol getViedoProtocol() {
//        String data="{\n" +
//                "\"data\":[{\"images_b\":[\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_0_b.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_1_b.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_2_b.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_3_b.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_4_b.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_5_b.jpg\"],\n" +
//                "\"md5checksum\":\"db66d05091bf4d4676269f0064b07f7e\",\n" +
//                "\"tag\":\"标签\",\n" +
//                "\"mp4\":\"http://mpv.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_1.mp4 \n" +
//                "\n" +
//                "\",\n" +
//                "\"title\":\"标题\",\n" +
//                "\"df\":3,\n" +
//                "\"times\":\"0\",\n" +
//                "\"mp4_1\":\"http://mpv.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_1.mp4 \n" +
//                "\n" +
//                " \n" +
//                "\n" +
//                "\",\n" +
//                "\"mp4_3\":\"http://mpv.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_3.mp4 \n" +
//                "\n" +
//                " \n" +
//                "\n" +
//                "\",\n" +
//                "\"vid\":\"e1510bdd3a2e695e8ca9168b9bba3ab4_e\",\n" +
//                "\"mp4_2\":\"http://mpv.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_2.mp4 \n" +
//                "\n" +
//                " \n" +
//                "\n" +
//                "\",\n" +
//                "\"cataid\":\"1\",\n" +
//                "\"swf_link\":\"http://player.polyv.net/videos/e1510bdd3a2e695e8ca9168b9bba3ab4_e.swf\",\n" +
//                "\"source_filesize\":25589985,\n" +
//                "\"status\":\"10\",\n" +
//                "\"seed\":0,\n" +
//                "\"flv2\":\"http://plvod01.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_2.flv \n" +
//                "\n" +
//                " \n" +
//                "\n" +
//                "\",\n" +
//                "\"flv3\":\"http://plvod01.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_3.flv \n" +
//                "\n" +
//                " \n" +
//                "\n" +
//                "\",\n" +
//                "\"flv1\":\"http://plvod01.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_1.flv \n" +
//                "\n" +
//                " \n" +
//                "\n" +
//                "\",\n" +
//                "\"sourcefile\":\"\",\n" +
//                "\"playerwidth\":\"600\",\n" +
//                "\"hls\":[\"http://hls.videocc.net/e1510bdd3a/e/e1510bdd3a2e695e8ca9168b9bba3ab4_1.m3u8\",\n" +
//                "\"http://hls.videocc.net/e1510bdd3a/e/e1510bdd3a2e695e8ca9168b9bba3ab4_2.m3u8\",\n" +
//                "\"http://hls.videocc.net/e1510bdd3a/e/e1510bdd3a2e695e8ca9168b9bba3ab4_3.m3u8\"],\n" +
//                "\"default_video\":\"http://plvod01.videocc.net/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_2.flv \n" +
//                "\n" +
//                " \n" +
//                "\n" +
//                "\",\n" +
//                "\"duration\":\"00:03:10\",\n" +
//                "\"filesize\":[0,0,0],\n" +
//                "\"first_image\":\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_0.jpg\",\n" +
//                "\"original_definition\":\"640x480\",\n" +
//                "\"context\":\"描述\",\n" +
//                "\"images\":[\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_0.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_1.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_2.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_3.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_4.jpg\",\n" +
//                "\"http://img.videocc.net/uimage/e/e1510bdd3a/4/e1510bdd3a2e695e8ca9168b9bba3ab4_5.jpg\"],\n" +
//                "\"playerheight\":\"450\",\n" +
//                "\"ptime\":\"2017-05-31 16:19:57\"}\n" +
//                "]}";
        ViedoProtocol viedoProtocol =null;
        try{
            if (!new JsonParser().parse(data).equals("{}")) {
            Gson gson=new Gson();
                 viedoProtocol=gson.fromJson(new JsonParser().parse(data).toString(),ViedoProtocol.class);
//               if(null!=viedoProtocol){
//                   viedo=viedoProtocol.data.get(0);
//               }
                Log.i("呵呵呵呵",viedoProtocol.vid) ;
             }
        }catch (Exception e){
            e.printStackTrace();
        }
        return viedoProtocol;
    }
}
