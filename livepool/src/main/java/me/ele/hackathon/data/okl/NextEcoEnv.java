package me.ele.hackathon.data.okl;

import java.math.BigDecimal;

public class NextEcoEnv {
    private String list_id;//                列表ID，此表主键
    private Integer is_select;              //该列表是否是经过用户筛选的列表，1为是，0为否
    private Integer day_no;                 //日期号，本数据集中日期号从1到154
    private Integer minutes;               //该订单产生的的分钟数，hour*60+minute计算得到
    private String eleme_device_id;        //对应的设备ID
    private Integer is_new;                //改设备是否当天首次来访
    private BigDecimal x;                     //浏览用户所在位置的横坐标
    private BigDecimal y;                    //浏览用户所在位置的纵坐标
    private String user_id;              //用户的账号ID，未登陆为NULL
    private String network_type;         // 网络类型
    private String platform;            //操作系统，Android/iOS
    private String brand;              //手机品牌
    private String model;             //手机型号
    private String network_operator;       //网络运营商，yd:移动，lt: 联通，dx: 典型
    private String resolution;               //手机分辨率
    private String channel;                  //APP的下载渠道

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public Integer getIs_select() {
        return is_select;
    }

    public void setIs_select(Integer is_select) {
        this.is_select = is_select;
    }

    public Integer getDay_no() {
        return day_no;
    }

    public void setDay_no(Integer day_no) {
        this.day_no = day_no;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public String getEleme_device_id() {
        return eleme_device_id;
    }

    public void setEleme_device_id(String eleme_device_id) {
        this.eleme_device_id = eleme_device_id;
    }

    public Integer getIs_new() {
        return is_new;
    }

    public void setIs_new(Integer is_new) {
        this.is_new = is_new;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(String network_type) {
        this.network_type = network_type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNetwork_operator() {
        return network_operator;
    }

    public void setNetwork_operator(String network_operator) {
        this.network_operator = network_operator;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}