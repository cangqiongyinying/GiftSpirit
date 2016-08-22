package com.jiangxin.giftspirit.JSONresource;

import java.util.List;

/**
 * Created by my on 2016/8/18.
 */
public class BDXQSMessage {
    /**
     * id : 1203
     * cid : 112
     * appid : 1458787671
     * appname : 被错过的天堂
     * appicon : /allimgs/img_iapp/201603/_1458787584145.jpg
     * typename : 冒险解密
     * appdesc :
     * appvtype : 1,2
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
        private int cid;
        private String appid;
        private String appname;
        private String appicon;
        private String typename;
        private String appdesc;
        private String appvtype;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
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

        public String getAppicon() {
            return appicon;
        }

        public void setAppicon(String appicon) {
            this.appicon = appicon;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getAppdesc() {
            return appdesc;
        }

        public void setAppdesc(String appdesc) {
            this.appdesc = appdesc;
        }

        public String getAppvtype() {
            return appvtype;
        }

        public void setAppvtype(String appvtype) {
            this.appvtype = appvtype;
        }
    }
}
