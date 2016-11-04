package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class AuthenticationList implements Serializable{


    /**
     * id : 783
     * name : 冰姐
     * photo : 1478084959831.jpg
     * phone : 15392341266
     * address : 宁德市福安市百丽阁
     * status : 2
     * usertype : 1
     * isworking : 1
     * serviceCount : 3
     * skilllist : [{"type":"21","typename":"送快递","parent_typeid":"12"},{"type":"273","typename":"陪练歌","parent_typeid":"272"}]
     * longitude : 119.654799
     * latitude : 27.090945
     * distance : 10000000000
     * evaluatelist : [{"id":"99","content":"态度好","score":"5","add_time":"1478242179","userid":"800","name":"翠花","photo":"1478165506124.jpg"}]
     * score : 5.0000
     */

    private String id;
    private String name;
    private String photo;
    private String phone;
    private String address;
    private String status;
    private String usertype;
    private String isworking;
    private String serviceCount;
    private String longitude;
    private String latitude;
    private long distance;
    private String score;
    /**
     * type : 21
     * typename : 送快递
     * parent_typeid : 12
     */

    private List<SkilllistBean> skilllist;
    /**
     * id : 99
     * content : 态度好
     * score : 5
     * add_time : 1478242179
     * userid : 800
     * name : 翠花
     * photo : 1478165506124.jpg
     */

    private List<EvaluatelistBean> evaluatelist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getIsworking() {
        return isworking;
    }

    public void setIsworking(String isworking) {
        this.isworking = isworking;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public List<SkilllistBean> getSkilllist() {
        return skilllist;
    }

    public void setSkilllist(List<SkilllistBean> skilllist) {
        this.skilllist = skilllist;
    }

    public List<EvaluatelistBean> getEvaluatelist() {
        return evaluatelist;
    }

    public void setEvaluatelist(List<EvaluatelistBean> evaluatelist) {
        this.evaluatelist = evaluatelist;
    }

    public static class SkilllistBean implements Serializable{
        private String type;
        private String typename;
        private String parent_typeid;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }

        public String getParent_typeid() {
            return parent_typeid;
        }

        public void setParent_typeid(String parent_typeid) {
            this.parent_typeid = parent_typeid;
        }
    }

    public static class EvaluatelistBean implements Serializable{
        private String id;
        private String content;
        private String score;
        private String add_time;
        private String userid;
        private String name;
        private String photo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
