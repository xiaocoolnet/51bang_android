package cn.xcom.helper.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17 0017.
 */
public class AuthenticationList implements Serializable{

    /**
     * id : 645
     * name : 年年有余
     * photo : avatar20161007154143.png
     * phone : 13905933706
     * address : 宁德市福安市S302
     * status : 2
     * usertype : 1
     * isworking : 1
     * serviceCount : 19
     * skilllist : [{"type":"22","typename":"送外卖","parent_typeid":"12"},{"type":"262","typename":"超市代购","parent_typeid":"12"},{"type":"314","typename":"排队挂号","parent_typeid":"12"},{"type":"243","typename":"其他...","parent_typeid":"12"},{"type":"269","typename":"占位占座","parent_typeid":"12"},{"type":"23","typename":"送货品","parent_typeid":"12"},{"type":"21","typename":"送快递","parent_typeid":"12"},{"type":"167","typename":"美术培训","parent_typeid":"162"},{"type":"327","typename":"武术培训","parent_typeid":"162"},{"type":"74","typename":"宠物美容","parent_typeid":"71"},{"type":"185","typename":"商业摄影","parent_typeid":"179"},{"type":"305","typename":"家教","parent_typeid":"77"},{"type":"49","typename":"保洁清洗","parent_typeid":"48"},{"type":"115","typename":"催乳师","parent_typeid":"48"},{"type":"53","typename":"厨师上门","parent_typeid":"48"},{"type":"93","typename":"家庭服务","parent_typeid":"48"},{"type":"94","typename":"甲醛检测","parent_typeid":"48"},{"type":"92","typename":"钟点工","parent_typeid":"48"},{"type":"51","typename":"保姆月嫂","parent_typeid":"48"},{"type":"50","typename":"干洗改衣","parent_typeid":"48"},{"type":"176","typename":"各种偏方","parent_typeid":"173"},{"type":"224","typename":"名医","parent_typeid":"173"},{"type":"177","typename":"各种药","parent_typeid":"173"},{"type":"174","typename":"疑难杂症","parent_typeid":"173"},{"type":"125","typename":"商标注册","parent_typeid":"54"},{"type":"126","typename":"工商注册","parent_typeid":"54"},{"type":"127","typename":"代理记账","parent_typeid":"54"},{"type":"128","typename":"异地对公","parent_typeid":"54"},{"type":"129","typename":"个人档案","parent_typeid":"54"},{"type":"130","typename":"专利版权申请","parent_typeid":"54"},{"type":"131","typename":"资格认证","parent_typeid":"54"},{"type":"132","typename":"签证代办","parent_typeid":"54"},{"type":"133","typename":"财务代办","parent_typeid":"54"},{"type":"134","typename":"工商代办","parent_typeid":"54"},{"type":"309","typename":"代办信用卡","parent_typeid":"54"},{"type":"310","typename":"代办pos机","parent_typeid":"54"},{"type":"139","typename":"美妆","parent_typeid":"68"},{"type":"141","typename":"纹身","parent_typeid":"68"},{"type":"136","typename":"美甲","parent_typeid":"68"},{"type":"143","typename":"塑身","parent_typeid":"68"},{"type":"144","typename":"理疗按摩","parent_typeid":"68"},{"type":"142","typename":"纹绣","parent_typeid":"68"},{"type":"140","typename":"美发","parent_typeid":"68"},{"type":"138","typename":"美容","parent_typeid":"68"},{"type":"137","typename":"美睫","parent_typeid":"68"},{"type":"193","typename":"法律纠纷","parent_typeid":"192"},{"type":"197","typename":"法律诉状","parent_typeid":"192"},{"type":"196","typename":"法律咨询","parent_typeid":"192"},{"type":"194","typename":"找律师","parent_typeid":"192"}]
     * longitude : 119.692434597453
     * latitude : 26.8177902687607
     * distance : 10000000000
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
    /**
     * type : 22
     * typename : 送外卖
     * parent_typeid : 12
     */

    private List<SkilllistBean> skilllist;

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

    public List<SkilllistBean> getSkilllist() {
        return skilllist;
    }

    public void setSkilllist(List<SkilllistBean> skilllist) {
        this.skilllist = skilllist;
    }

    public static class SkilllistBean implements Serializable {
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
}
