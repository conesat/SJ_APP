package com.hg.sj_app.tools;

import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.entity.HGNewsMediaEntity;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    static String[] videoUrls = {"http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
            "http://v3-ppx.ixigua.com/fb0e629ea5f26a3373dc1c9ae60c6fda/5c961f84/video/m/220ce60cb8d9e494e36a324291e6453eb3d11612b0a8000077ed685d876f/?rc=M2Z4b3drZG5sajMzaGYzM0ApQHRlaHhAbzs0ODYzMzQzMzQ4NDUzNDVvQGg2dSlAZjN1KWZzbGR2ZWY8NkAwLWZsXi5zLmdfLS0zMS9zcy9vI29tcG8jMjIvLzA2LS4uMjUwMDYtOiNvIzphLXEjOmBoXittK2E6Iy4uXg%3D%3D"
            , "http://v6-ppx.ixigua.com/b3071fa82481a5208eacab23a8225e6f/5c9620cf/video/m/22083393aaa71734934b576112aaa7aa0611161aae6f000082287b3d115d/?rc=MzM8Zm93NnV0bDMzZWYzM0ApQHRlaHhAbzU7NDYzMzQzMzYzNDUzNDVvQGg2dSlAZjN1KWZzbGR2ZWY8NkBkXmJtNmNvZl5fLS0uMS9zcy9vI29tcG8jMC4vLzA2LS4wMDYwMDYtOiNvIzphLXEjOmBoXittK2E6Iy4uXg%3D%3D"};

    static String[] videoThumbs = {"http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
            "https://p3-ppx.bytecdn.cn/img/mosaic-legacy/15223000cf219ea04d6a2~1125x632_q80.jpeg",
            "https://p3-ppx.bytecdn.cn/img/mosaic-legacy/1d31f0003e78f8760a0b9~632x1125_q80.jpeg"};

    static String[] title = {"随机标题", "漫威的演员走红毯，红女巫-伊丽莎白简直太漂亮了！", "收拾后院了一下，手机说:天气有点冷，现在游泳有点早"};
    static String[] username = {"随机用户", "漫威大家庭", "东北酱在洛杉矶"};
    static String[] usericon = {"https://avatar.csdn.net/7/9/0/3_zhaihaohao1.jpg", "https://p3-ppx.bytecdn.cn/img/tos-cn-i-0000/811242f9f65e4f729cd5a62f3b1ed182~200x200.jpeg", "https://p3-ppx.bytecdn.cn/img/tos-cn-i-0000/0bcfa1defc604be49233d9d1fad610fd~200x200.jpeg"};

    public static List<HGNewsItemEntity> hgNewsItemEntities = new ArrayList<>();

    static {
        suiji(10);
    }

    public static void suiji(int num) {
        for (int i = 0; i < num; i++) {
            int x = (int) (Math.random() * 2);
            if (x == 0) {
                suijiV();
            } else {
                suijiImg();
            }
        }

    }

    public static void suijiV() {
        HGNewsItemEntity hgNewsItemEntity = new HGNewsItemEntity();
        int x = (int) (Math.random() * 3);
        List<HGNewsMediaEntity> medias = new ArrayList<>();
        HGNewsMediaEntity hgNewsMediaEntity = new HGNewsMediaEntity(videoThumbs[x], videoUrls[x]);
        medias.add(hgNewsMediaEntity);

        hgNewsItemEntity.setSenderIconUrl(usericon[x]);
        hgNewsItemEntity.setNewsDetail(title[x]);
        hgNewsItemEntity.setSenderName(username[x]);
        hgNewsItemEntity.setCommentNum((long) 123456);
        hgNewsItemEntity.setMediaType("vedio");
        hgNewsItemEntity.setMedias(medias);
        hgNewsItemEntities.add(hgNewsItemEntity);
    }

    public static void suijiImg() {
        HGNewsItemEntity hgNewsItemEntity = new HGNewsItemEntity();
        List<HGNewsMediaEntity> medias = new ArrayList<>();
        int x = (int) (Math.random() * 3);
        int xs = (int) (Math.random() * 6);
        for (int j = 0; j < xs; j++) {
            int tt = (int) (Math.random() * 3);
            HGNewsMediaEntity hgNewsMediaEntity = new HGNewsMediaEntity(videoThumbs[tt], videoThumbs[tt]);
            medias.add(hgNewsMediaEntity);
        }
        hgNewsItemEntity.setSenderIconUrl(usericon[x]);
        hgNewsItemEntity.setNewsDetail(title[x]);
        hgNewsItemEntity.setSenderName(username[x]);
        hgNewsItemEntity.setCommentNum((long) 123456);
        hgNewsItemEntity.setMediaType("img");
        hgNewsItemEntity.setMedias(medias);
        hgNewsItemEntities.add(hgNewsItemEntity);
    }

    public static List<HGNewsItemEntity> loadMore() {
        suiji(10);
        return hgNewsItemEntities;
    }
    public static List<HGNewsItemEntity> reload() {
        hgNewsItemEntities.clear();
        suiji(10);
        return hgNewsItemEntities;
    }
}
