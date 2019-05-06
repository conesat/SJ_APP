package com.hg.sj_app.helper;

import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.entity.HGNewsMediaEntity;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    static int G = 0;
    static String[] videoUrls = {"http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
            "http://v3-ppx.ixigua.com/7c8614def4df258d3dbbcadd31cdcbef/5cab430b/video/m/2208346711d483f4041a8d666cbb2909e831161c7f5600001aa019a54ce3/?rc=ajd1ajxsZW9qbDMzOWYzM0ApQHRlaHhAbzM7NjM6MzQzMzY3NDMzNDVvQGg2dSlAZjN1KWZzbGR2ZWY8NkBzXmRoNGluM2lfLS1jMTBzcy9vI29tcG8jLTUxLy4uLS4vMzY1MTYtOiNvIzphLXEjOmBoXittK2E6Iy4uXg%3D%3D"
            , "http://v3-ppx.ixigua.com/1c8d8479f4c782454fdba1ad009f4ebf/5cab433a/video/m/220d66582932a5d498faa21ed397a9c0916116197941000063fc5f6d5551/?rc=amt2cXF4d2o7bDMzNWYzM0ApQHRlaHhAbzY0NDw4MzQzMzQ3NDMzNDVvQGg2dSlAZjN1KWZzbGR2ZWY8NkBvYWRnaS8zYS1fLS0vMTBzcy9vI29tcG8jNDQyLTEwLS42NTY1MTYtOiNvIzphLXEjOmBoXittK2E6Iy4uXg%3D%3D"};

    static String[] videoThumbs = {"http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
            "https://p3-ppx.bytecdn.cn/img/mosaic-legacy/1e75700084a2b29734b51~1125x632_q80.jpeg",
            "https://p3-ppx.bytecdn.cn/img/mosaic-legacy/1bc080007f23ba9010561~632x1125_q80.jpeg"};

    static String[] title = {"随机标题", "外出注意安全", "深圳华强北走出去的“非洲之王”"};
    static String[] username = {"随机用户", "漫威大家庭", "东北酱在洛杉矶"};
    static String[] usericon = {"https://avatar.csdn.net/7/9/0/3_zhaihaohao1.jpg", "https://p3-ppx.bytecdn.cn/img/tos-cn-i-0000/811242f9f65e4f729cd5a62f3b1ed182~200x200.jpeg", "https://p3-ppx.bytecdn.cn/img/tos-cn-i-0000/0bcfa1defc604be49233d9d1fad610fd~200x200.jpeg"};

    //public  List<HGNewsItemEntity> hgNewsItemEntities = new ArrayList<>();

    /* static {
         suiji(10);
     }
 */
    public static List<HGNewsItemEntity> suiji(int num) {
        List<HGNewsItemEntity> hgNewsItemEntities = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int x = (int) (Math.random() * 2);
            if (x == 0) {
                suijiV(hgNewsItemEntities);
            } else {
                suijiImg(hgNewsItemEntities);
            }
            // suijiImg();
        }
        return hgNewsItemEntities;
    }

    public static void suijiV(List<HGNewsItemEntity> hgNewsItemEntities) {
        HGNewsItemEntity hgNewsItemEntity = new HGNewsItemEntity();
        int x = (int) (Math.random() * 3);
        List<HGNewsMediaEntity> medias = new ArrayList<>();
        HGNewsMediaEntity hgNewsMediaEntity = new HGNewsMediaEntity(videoThumbs[x], videoUrls[x]);
        medias.add(hgNewsMediaEntity);

        hgNewsItemEntity.setSenderIconUrl(usericon[x]);
        hgNewsItemEntity.setNewsDetail(title[x]);
        hgNewsItemEntity.setSenderName(username[x] + (G++));
        hgNewsItemEntity.setCommentNum((long) 123456);
        hgNewsItemEntity.setMediaType("vedio");
        hgNewsItemEntity.setMedias(medias);
        hgNewsItemEntities.add(hgNewsItemEntity);
    }

    public static void suijiImg(List<HGNewsItemEntity> hgNewsItemEntities) {
        HGNewsItemEntity hgNewsItemEntity = new HGNewsItemEntity();
        List<HGNewsMediaEntity> medias = new ArrayList<>();
        int x = (int) (Math.random() * 3);
        int xs = (int) (Math.random() * 14);
        for (int j = 0; j < xs; j++) {
            int tt = (int) (Math.random() * 3);
            HGNewsMediaEntity hgNewsMediaEntity = new HGNewsMediaEntity(videoThumbs[tt], videoThumbs[tt]);
            medias.add(hgNewsMediaEntity);
        }
        hgNewsItemEntity.setSenderIconUrl(usericon[x]);
        hgNewsItemEntity.setNewsDetail(title[x]);
        hgNewsItemEntity.setSenderName(username[x] + (G++));
        hgNewsItemEntity.setCommentNum((long) 123456);
        hgNewsItemEntity.setMediaType("img");
        hgNewsItemEntity.setMedias(medias);
        hgNewsItemEntities.add(hgNewsItemEntity);
    }

    public static List<HGNewsItemEntity> loadMore() {

        return suiji(10);
    }

    public static List<HGNewsItemEntity> reload() {

        return suiji(10);
    }
}
