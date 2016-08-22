package com.jiangxin.giftspirit.JSONresource;

import java.util.List;

/**
 * Created by my on 2016/8/19.
 */
public class XYZKChildMessage {
    /**
     * id : 810
     * fid : 20160534
     * appid : 1467612648
     * appname : 少女咖啡枪
     * typename : 角色扮演
     * appsize : 430M
     * adimg : /allimgs/img_iapp/201608/_1470979549307.jpg
     * appkfs : 西山居
     * iconurl : /allimgs/img_iapp/201607/_1467612570049.png
     * addtime : 2016-08-12
     * descs : 《少女咖啡枪》是由成都西山居自主研发的3D射击作战手游。这是一款以二次元为受众群体、主打枪
     械及萌妹的轻科幻风格大作。游戏中你可以手持枪械击退敌人亦可以操作载具对敌方势力进行碾压，同时还可以经营在末世间隙中努力生存的咖啡馆，跟你喜欢的妹子拉近距离
     * critique : 　　《少女咖啡枪》是一款锻炼反应力和走位的激烈动作枪战型猫咪饲育指南向摸头杀爱好者天堂兼少女养成游戏，优点在于后期关卡堪比《黑暗之魂》一般的难度可以满足抖M们的心理，休闲养肝党每天也可以在喂猫摸头中了此残生(误) ，如果能加入手柄的接口想来肯定能使得操作手感方面更上一层楼，最后，他最大的缺点小编也觉得应该如实的说出来不需要隐瞒，那就是：
     还!T!M!没!开!放!公!测!啊!

     * iszq : 0
     * typeid : 0
     * istop : 0
     */

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int id;
        private int fid;
        private String appid;
        private String appname;
        private String typename;
        private String appsize;
        private String adimg;
        private String appkfs;
        private String iconurl;
        private String addtime;
        private String descs;
        private String critique;
        private int iszq;
        private int typeid;
        private int istop;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFid() {
            return fid;
        }

        public void setFid(int fid) {
            this.fid = fid;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getAppsize() {
            return appsize;
        }

        public void setAppsize(String appsize) {
            this.appsize = appsize;
        }

        public String getAdimg() {
            return adimg;
        }

        public void setAdimg(String adimg) {
            this.adimg = adimg;
        }

        public String getAppkfs() {
            return appkfs;
        }

        public void setAppkfs(String appkfs) {
            this.appkfs = appkfs;
        }

        public String getIconurl() {
            return iconurl;
        }

        public void setIconurl(String iconurl) {
            this.iconurl = iconurl;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getDescs() {
            return descs;
        }

        public void setDescs(String descs) {
            this.descs = descs;
        }

        public String getCritique() {
            return critique;
        }

        public void setCritique(String critique) {
            this.critique = critique;
        }

        public int getIszq() {
            return iszq;
        }

        public void setIszq(int iszq) {
            this.iszq = iszq;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }

        public int getIstop() {
            return istop;
        }

        public void setIstop(int istop) {
            this.istop = istop;
        }
    }
}
