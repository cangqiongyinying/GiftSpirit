package com.jiangxin.giftspirit.JSONresource;

import java.util.List;

/**
 * Created by my on 2016/8/17.
 */
public class HotGameMessage {
    /**
     * flag : true
     * info : {"push1":[{"id":8,"appid":"1441623161","type":1,"clicks":12112,"flag":1,"platform":0,"name":"航海归来","typename":"策略塔防","logo":"/allimgs/img_iapp/201608/_1470623898433.jpg","addtime":"2016-08-17 11:19:47.0"},{"id":10,"appid":"1443491252","type":1,"clicks":14283,"flag":1,"platform":0,"name":"王者荣耀","typename":"动作格斗","logo":"/allimgs/img_iapp/201509/_1443491274999.png","size":"360M","addtime":"2016-08-04 21:33:32.0"},{"id":37,"appid":"1428908867","type":1,"clicks":13262,"flag":0,"platform":0,"name":"火影忍者","typename":"动作格斗","logo":"/allimgs/img_iapp/201605/_1463474275867.png","size":"342M","addtime":"2016-07-23 14:04:44.0"}],"push2":[{"id":28,"appid":"1451971043","type":0,"clicks":2598,"flag":1,"platform":0,"name":"部落冲突:皇室战争","typename":"卡牌游戏","logo":"/allimgs/img_iapp/201601/_1451970639500.jpg","size":"98.1M","addtime":"2016-04-03 17:40:23.0"},{"id":27,"appid":"1451550790","type":0,"clicks":100,"flag":1,"platform":0,"name":"天天酷跑3D","typename":"休闲益智","logo":"/allimgs/img_iapp/201601/_1454054325445.png","size":"244M","addtime":"2016-04-03 17:39:10.0"},{"id":11,"appid":"1438084072","type":0,"clicks":4522,"flag":1,"platform":0,"name":"全民超神","typename":"角色扮演","logo":"/allimgs/img_iapp/201507/_1438084023090.jpg","size":"401 MB","addtime":"2016-03-24 13:41:41.0"},{"id":23,"appid":"1421918699","type":0,"clicks":100,"flag":1,"platform":0,"name":"熹妃传","typename":"角色扮演","logo":"/allimgs/img_iapp/201601/_1452236952103.png","size":"158 MB","addtime":"2016-03-23 20:02:16.0"},{"id":9,"appid":"1420564361","type":0,"clicks":3533,"flag":1,"platform":0,"name":"全民突击","typename":"射击空战","logo":"/userfiles/applogo/_1420610936640.jpg","size":"223M","addtime":"2016-01-09 18:12:19.0"},{"id":12,"appid":"1421467036","type":0,"clicks":2532,"flag":1,"platform":0,"name":"热血传奇手机版","typename":"角色扮演","logo":"/allimgs/img_iapp/201603/_1459327042485.png","size":" 516 MB","addtime":"2016-01-09 18:08:27.0"}]}
     */

    private boolean flag;
    private InfoBean info;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 8
         * appid : 1441623161
         * type : 1
         * clicks : 12112
         * flag : 1
         * platform : 0
         * name : 航海归来
         * typename : 策略塔防
         * logo : /allimgs/img_iapp/201608/_1470623898433.jpg
         * addtime : 2016-08-17 11:19:47.0
         */

        private List<Push1Bean> push1;
        /**
         * id : 28
         * appid : 1451971043
         * type : 0
         * clicks : 2598
         * flag : 1
         * platform : 0
         * name : 部落冲突:皇室战争
         * typename : 卡牌游戏
         * logo : /allimgs/img_iapp/201601/_1451970639500.jpg
         * size : 98.1M
         * addtime : 2016-04-03 17:40:23.0
         */

        private List<Push2Bean> push2;

        public List<Push1Bean> getPush1() {
            return push1;
        }

        public void setPush1(List<Push1Bean> push1) {
            this.push1 = push1;
        }

        public List<Push2Bean> getPush2() {
            return push2;
        }

        public void setPush2(List<Push2Bean> push2) {
            this.push2 = push2;
        }

        public static class Push1Bean {
            private int id;
            private String appid;
            private int type;
            private int clicks;
            private int flag;
            private int platform;
            private String name;
            private String typename;
            private String logo;
            private String size;
            private String addtime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getClicks() {
                return clicks;
            }

            public void setClicks(int clicks) {
                this.clicks = clicks;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }

        public static class Push2Bean {
            private int id;
            private String appid;
            private int type;
            private int clicks;
            private int flag;
            private int platform;
            private String name;
            private String typename;
            private String logo;
            private String size;
            private String addtime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getClicks() {
                return clicks;
            }

            public void setClicks(int clicks) {
                this.clicks = clicks;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public int getPlatform() {
                return platform;
            }

            public void setPlatform(int platform) {
                this.platform = platform;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTypename() {
                return typename;
            }

            public void setTypename(String typename) {
                this.typename = typename;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
        }
    }
}
