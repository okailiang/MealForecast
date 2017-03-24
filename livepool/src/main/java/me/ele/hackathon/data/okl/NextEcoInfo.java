package me.ele.hackathon.data.okl;

public class NextEcoInfo {
    private String log_id; //  主键，曝光记录的ID, 用户每看到一个餐厅即为一条曝光记录
    private String list_id; // 本次曝光对应的列表ID
    private String restaurant_id; // 餐厅ID
    private Integer index;      // 该餐厅在列表中的排列位置
    private Integer is_click;   // 本次曝光是否产生了点击行为，只有his_eco_info.txt中有该字段
    private Integer is_buy;  //是否产生了有效购买，待预测的购买就是这个字段
    private Integer is_raw_buy; // 是否产生订单，his_eco_info.txt 特有，此处也包含无效订单，供分析用
    private String order_id;  //如果产生了订单，此处是订单ID，否则是NULL

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getList_id() {
        return list_id;
    }

    public void setList_id(String list_id) {
        this.list_id = list_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIs_click() {
        return is_click;
    }

    public void setIs_click(Integer is_click) {
        this.is_click = is_click;
    }

    public Integer getIs_buy() {
        return is_buy;
    }

    public void setIs_buy(Integer is_buy) {
        this.is_buy = is_buy;
    }

    public Integer getIs_raw_buy() {
        return is_raw_buy;
    }

    public void setIs_raw_buy(Integer is_raw_buy) {
        this.is_raw_buy = is_raw_buy;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}