package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29 0029.
 */
public class Collection implements Serializable {

    /**
     * id : 190
     * userid : 578
     * title :
     * description : 4445456
     * type : 3
     * object_id : 12
     * createtime : 1475120389
     * longitude : 119.65475202844
     * latitude : 27.0908246596745
     * price : 0
     * goodspicture : []
     */

    private String id;
    private String userid;
    private String title;
    private String description;
    private String type;
    private String object_id;
    private String createtime;
    private String longitude;
    private String latitude;
    private String price;
    private List<?> goodspicture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<?> getGoodspicture() {
        return goodspicture;
    }

    public void setGoodspicture(List<?> goodspicture) {
        this.goodspicture = goodspicture;
    }
}
