package cn.xcom.helper.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 尉鑫鑫 on 2016/9/13.
 */
public class Front implements Serializable {

    /**
     * id : 192
     * type :
     * goodsname : 在线
     * username : 黄金丽
     * phone : 13515067861
     * price : 1
     * oprice : 10
     * picture : null
     * description : 一切众生下一代
     * sellnumber : 0
     * address : 泽友家电商场(金山店)
     * longitude : 119.654707765037
     * latitude : 27.0908283086104
     * delivery : 同城自取
     * picturelist : [{"id":"116","goodsid":"192","file":"avatar2016:09:11:21:31:45.9900.png","time":null},{"id":"117","goodsid":"192","file":"avatar2016:09:11:21:31:46.1600.png","time":null},{"id":"118","goodsid":"192","file":"avatar2016:09:11:21:31:46.1190.png","time":null}]
     * commentlist : [{"id":"69","content":"0.00.0.0.","add_time":"1473675745","userid":"607","name":"你好冴","photo":null}]
     */

    private String id;
    private String type;
    private String goodsname;
    private String username;
    private String phone;
    private String price;
    private String oprice;
    private Object picture;
    private String description;
    private String sellnumber;
    private String address;
    private String longitude;
    private String latitude;
    private String delivery;
    private List<PicturelistBean> picturelist;
    private List<CommentlistBean> commentlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOprice() {
        return oprice;
    }

    public void setOprice(String oprice) {
        this.oprice = oprice;
    }



    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellnumber() {
        return sellnumber;
    }

    public void setSellnumber(String sellnumber) {
        this.sellnumber = sellnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public List<PicturelistBean> getPicturelist() {
       return picturelist;
    }
//    public List<PicturelistBean>   getPicture(){
//        List<PicturelistBean> list=new ArrayList<>();
//          for (PicturelistBean p:picturelist){
//              list.add(p);
//          }
//        return list;
//    }
    public void setPicturelist(List<PicturelistBean> picturelist) {
        this.picturelist = picturelist;
    }

    public List<CommentlistBean> getCommentlist() {

        return commentlist;
    }
    public void setCommentlist(List<CommentlistBean> commentlist) {
        this.commentlist = commentlist;
    }



    public static class PicturelistBean implements Serializable {
        private String id;
        private String goodsid;
        private String file;
        private Object time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(String goodsid) {
            this.goodsid = goodsid;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }
    }

    public static class CommentlistBean implements Serializable{
        private String id;
        private String content;
        private String add_time;
        private String userid;
        private String name;
        private Object photo;

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

        public Object getPhoto() {
            return photo;
        }

        public void setPhoto(Object photo) {
            this.photo = photo;
        }
    }



}
