package com.refreshlayout.bean;

import java.io.Serializable;

/**
 * @Author FangJW
 * @Date 6/30/17
 */
public class OnePictureDetail {

    /**
     * res : 0
     * data : {"hpcontent_id":"1557","hp_title":"VOL.1529","author_id":"-1","hp_img_url":"http://image.wufazhuce.com/FlpT1re2jogI-qVP0_3BbGIXcRF2","hp_img_original_url":"http://image.wufazhuce.com/FlpT1re2jogI-qVP0_3BbGIXcRF2","hp_author":"祝福＆邓杨浩嘉 作品","ipad_url":"http://image.wufazhuce.com/FlpT1re2jogI-qVP0_3BbGIXcRF2","hp_content":"人生的苦难，终会被我们在无数个独自承受的夜里包裹起来，变成珍珠，择日择机，献给最爱的人。 from 熊德启《这一切并没有那么糟》","hp_makettime":"2016-12-13 21:00:00","hide_flag":"0","last_update_date":"2016-12-22 20:52:42","web_url":"http://m.wufazhuce.com/one/1557","wb_img_url":"","image_authors":"邓杨浩嘉","text_authors":"熊德启《这一切并没有那么糟》","image_from":"","text_from":"","content_bgcolor":"#FFFFFF","template_category":"0","maketime":"2016-12-13 21:00:00","praisenum":30747,"sharenum":2232,"commentnum":0}
     */

    private int res;
    private DataEntity data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity implements Serializable {
        /**
         * hpcontent_id : 1557
         * hp_title : VOL.1529
         * author_id : -1
         * hp_img_url : http://image.wufazhuce.com/FlpT1re2jogI-qVP0_3BbGIXcRF2
         * hp_img_original_url : http://image.wufazhuce.com/FlpT1re2jogI-qVP0_3BbGIXcRF2
         * hp_author : 祝福＆邓杨浩嘉 作品
         * ipad_url : http://image.wufazhuce.com/FlpT1re2jogI-qVP0_3BbGIXcRF2
         * hp_content : 人生的苦难，终会被我们在无数个独自承受的夜里包裹起来，变成珍珠，择日择机，献给最爱的人。 from 熊德启《这一切并没有那么糟》
         * hp_makettime : 2016-12-13 21:00:00
         * hide_flag : 0
         * last_update_date : 2016-12-22 20:52:42
         * web_url : http://m.wufazhuce.com/one/1557
         * wb_img_url :
         * image_authors : 邓杨浩嘉
         * text_authors : 熊德启《这一切并没有那么糟》
         * image_from :
         * text_from :
         * content_bgcolor : #FFFFFF
         * template_category : 0
         * maketime : 2016-12-13 21:00:00
         * praisenum : 30747
         * sharenum : 2232
         * commentnum : 0
         */

        private String hpcontent_id;
        private String hp_title;
        private String author_id;
        private String hp_img_url;
        private String hp_img_original_url;
        private String hp_author;
        private String ipad_url;
        private String hp_content;
        private String hp_makettime;
        private String hide_flag;
        private String last_update_date;
        private String web_url;
        private String wb_img_url;
        private String image_authors;
        private String text_authors;
        private String image_from;
        private String text_from;
        private String content_bgcolor;
        private String template_category;
        private String maketime;
        private int praisenum;
        private int sharenum;
        private int commentnum;

        public String getHpcontent_id() {
            return hpcontent_id;
        }

        public void setHpcontent_id(String hpcontent_id) {
            this.hpcontent_id = hpcontent_id;
        }

        public String getHp_title() {
            return hp_title;
        }

        public void setHp_title(String hp_title) {
            this.hp_title = hp_title;
        }

        public String getAuthor_id() {
            return author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getHp_img_url() {
            return hp_img_url;
        }

        public void setHp_img_url(String hp_img_url) {
            this.hp_img_url = hp_img_url;
        }

        public String getHp_img_original_url() {
            return hp_img_original_url;
        }

        public void setHp_img_original_url(String hp_img_original_url) {
            this.hp_img_original_url = hp_img_original_url;
        }

        public String getHp_author() {
            return hp_author;
        }

        public void setHp_author(String hp_author) {
            this.hp_author = hp_author;
        }

        public String getIpad_url() {
            return ipad_url;
        }

        public void setIpad_url(String ipad_url) {
            this.ipad_url = ipad_url;
        }

        public String getHp_content() {
            return hp_content;
        }

        public void setHp_content(String hp_content) {
            this.hp_content = hp_content;
        }

        public String getHp_makettime() {
            return hp_makettime;
        }

        public void setHp_makettime(String hp_makettime) {
            this.hp_makettime = hp_makettime;
        }

        public String getHide_flag() {
            return hide_flag;
        }

        public void setHide_flag(String hide_flag) {
            this.hide_flag = hide_flag;
        }

        public String getLast_update_date() {
            return last_update_date;
        }

        public void setLast_update_date(String last_update_date) {
            this.last_update_date = last_update_date;
        }

        public String getWeb_url() {
            return web_url;
        }

        public void setWeb_url(String web_url) {
            this.web_url = web_url;
        }

        public String getWb_img_url() {
            return wb_img_url;
        }

        public void setWb_img_url(String wb_img_url) {
            this.wb_img_url = wb_img_url;
        }

        public String getImage_authors() {
            return image_authors;
        }

        public void setImage_authors(String image_authors) {
            this.image_authors = image_authors;
        }

        public String getText_authors() {
            return text_authors;
        }

        public void setText_authors(String text_authors) {
            this.text_authors = text_authors;
        }

        public String getImage_from() {
            return image_from;
        }

        public void setImage_from(String image_from) {
            this.image_from = image_from;
        }

        public String getText_from() {
            return text_from;
        }

        public void setText_from(String text_from) {
            this.text_from = text_from;
        }

        public String getContent_bgcolor() {
            return content_bgcolor;
        }

        public void setContent_bgcolor(String content_bgcolor) {
            this.content_bgcolor = content_bgcolor;
        }

        public String getTemplate_category() {
            return template_category;
        }

        public void setTemplate_category(String template_category) {
            this.template_category = template_category;
        }

        public String getMaketime() {
            return maketime;
        }

        public void setMaketime(String maketime) {
            this.maketime = maketime;
        }

        public int getPraisenum() {
            return praisenum;
        }

        public void setPraisenum(int praisenum) {
            this.praisenum = praisenum;
        }

        public int getSharenum() {
            return sharenum;
        }

        public void setSharenum(int sharenum) {
            this.sharenum = sharenum;
        }

        public int getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(int commentnum) {
            this.commentnum = commentnum;
        }
    }
}
