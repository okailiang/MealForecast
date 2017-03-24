package me.ele.hackathon.data.okl;

import java.math.BigDecimal;

/**
 * 餐馆信息
 *
 * @author oukailiang
 * @create 2016-10-11 下午6:24
 */

public class HisRstInfo {
    private String restaurant_id;            //餐厅ID
    private String primary_category;         //餐厅所属分类
    private String food_name_list;          //该餐厅里top10品种最多的食物种类
    private String category_list;            //该餐厅top5品种最多的食物大类
    private String x;                       // 餐厅所在位置的横坐标
    private String y;                       // 餐厅所在位置的纵坐标
    private String agent_fee;               // 配送费
    private String is_premium;              // 是否品牌馆，1是，0否
    private String address_type;             //餐厅地址类型，同上，
    private String good_rating_rate;         //4星以上好评占比
    private String open_month_num;
    private String has_image;                //餐厅是否有图片，1是，0否
    private String has_food_img;             //食物是否有图片
    private String min_deliver_amount;       //最低起送价
    private String time_ensure_spent;        //保证多久可以送达，is_time_ensure=1时可用
    private String is_time_ensure;           //是否有时间保证
    private String is_ka;                    //是否是连锁店大大客户
    private String is_time_ensure_discount;  //是否有超时折扣
    private String is_eleme_deliver;         //是否是饿了么配送
    private String radius;
    private String bu_flag;                  //餐厅所属bu
    private String brand_name;               //餐厅所属品牌，只有部分餐厅有，如肯德基，麦当劳等
    private String service_rating;           //服务综合评价，5分制
    private String invoice;                  //是否支持开发票
    private String online_payment;           //是否在线支付
    private String public_degree;           //信息公开等级，餐厅厨房，大堂，门面等的公开级别0-3，公开的信息越大，数字越大
    private String food_num;                 //餐厅内食物品种数量
    private String food_image_num;           //有图片的食物品种数量
    private String is_promotion_info;        //是否有促销信息
    private String is_bookable;              //是否支持预定单

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getPrimary_category() {
        return primary_category;
    }

    public void setPrimary_category(String primary_category) {
        this.primary_category = primary_category;
    }

    public String getFood_name_list() {
        return food_name_list;
    }

    public void setFood_name_list(String food_name_list) {
        this.food_name_list = food_name_list;
    }

    public String getCategory_list() {
        return category_list;
    }

    public void setCategory_list(String category_list) {
        this.category_list = category_list;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getAgent_fee() {
        return agent_fee;
    }

    public void setAgent_fee(String agent_fee) {
        this.agent_fee = agent_fee;
    }

    public String getIs_premium() {
        return is_premium;
    }

    public void setIs_premium(String is_premium) {
        this.is_premium = is_premium;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getGood_rating_rate() {
        return good_rating_rate;
    }

    public void setGood_rating_rate(String good_rating_rate) {
        this.good_rating_rate = good_rating_rate;
    }

    public String getOpen_month_num() {
        return open_month_num;
    }

    public void setOpen_month_num(String open_month_num) {
        this.open_month_num = open_month_num;
    }

    public String getHas_image() {
        return has_image;
    }

    public void setHas_image(String has_image) {
        this.has_image = has_image;
    }

    public String getHas_food_img() {
        return has_food_img;
    }

    public void setHas_food_img(String has_food_img) {
        this.has_food_img = has_food_img;
    }

    public String getMin_deliver_amount() {
        return min_deliver_amount;
    }

    public void setMin_deliver_amount(String min_deliver_amount) {
        this.min_deliver_amount = min_deliver_amount;
    }

    public String getTime_ensure_spent() {
        return time_ensure_spent;
    }

    public void setTime_ensure_spent(String time_ensure_spent) {
        this.time_ensure_spent = time_ensure_spent;
    }

    public String getIs_time_ensure() {
        return is_time_ensure;
    }

    public void setIs_time_ensure(String is_time_ensure) {
        this.is_time_ensure = is_time_ensure;
    }

    public String getIs_ka() {
        return is_ka;
    }

    public void setIs_ka(String is_ka) {
        this.is_ka = is_ka;
    }

    public String getIs_time_ensure_discount() {
        return is_time_ensure_discount;
    }

    public void setIs_time_ensure_discount(String is_time_ensure_discount) {
        this.is_time_ensure_discount = is_time_ensure_discount;
    }

    public String getIs_eleme_deliver() {
        return is_eleme_deliver;
    }

    public void setIs_eleme_deliver(String is_eleme_deliver) {
        this.is_eleme_deliver = is_eleme_deliver;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getBu_flag() {
        return bu_flag;
    }

    public void setBu_flag(String bu_flag) {
        this.bu_flag = bu_flag;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getService_rating() {
        return service_rating;
    }

    public void setService_rating(String service_rating) {
        this.service_rating = service_rating;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getOnline_payment() {
        return online_payment;
    }

    public void setOnline_payment(String online_payment) {
        this.online_payment = online_payment;
    }

    public String getPublic_degree() {
        return public_degree;
    }

    public void setPublic_degree(String public_degree) {
        this.public_degree = public_degree;
    }

    public String getFood_num() {
        return food_num;
    }

    public void setFood_num(String food_num) {
        this.food_num = food_num;
    }

    public String getFood_image_num() {
        return food_image_num;
    }

    public void setFood_image_num(String food_image_num) {
        this.food_image_num = food_image_num;
    }

    public String getIs_promotion_info() {
        return is_promotion_info;
    }

    public void setIs_promotion_info(String is_promotion_info) {
        this.is_promotion_info = is_promotion_info;
    }

    public String getIs_bookable() {
        return is_bookable;
    }

    public void setIs_bookable(String is_bookable) {
        this.is_bookable = is_bookable;
    }
}
