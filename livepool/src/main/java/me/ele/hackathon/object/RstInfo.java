package me.ele.hackathon.object;

/**
 * Created by solomon on 16/10/17.
 */
public class RstInfo {
    String restaurant_id  ;          //餐厅ID
    int primary_category  ;     //餐厅所属分类
    int food_name_list ;        //该餐厅里top10品种最多的食物种类
    int category_list  ;        //该餐厅top5品种最多的食物大类
    int agent_fee      ;    //   配送费
    int is_premium;    //  是否品牌馆，1是，0否
    int address_type ;    // 餐厅地址类型，同上，
    int good_rating_rate;    //4星以上好评占比
    int has_image;  // 餐厅是否有图片，1是，0否
    int has_food_img; // 食物是否有图片
    int min_deliver_amount ; //最低起送价
    int time_ensure_spent;//保证多久可以送达，is_time_ensure=1时可用
    int is_time_ensure;  //是否有时间保证
    int is_ka ;                  //是否是连锁店大大客户
    int is_time_ensure_discount ;  //是否有超时折扣
    int is_eleme_deliver ;         //是否是饿了么配送
    int bu_flag ;           //       餐厅所属bu
    int brand_name   ;        //    餐厅所属品牌，只有部分餐厅有，如肯德基，麦当劳等
    int service_rating  ;      //    服务综合评价，5分制
    int invoice   ;            //    是否支持开发票
    int online_payment   ;      //   是否在线支付
    int public_degree  ;        //   信息公开等级，餐厅厨房，大堂，门面等的公开级别0-3，公开的信息越大，数字越大
    int food_num       ;         //  餐厅内食物品种数量
    int food_image_num   ;       //  有图片的食物品种数量
    int is_promotion_info  ;     //  是否有促销信息
    int is_bookable    ;         //  是否支持预定单


    public String getRestaurant_id() {
        return restaurant_id;
    }

    public int getPrimary_category() {
        return primary_category;
    }

    public int getFood_name_list() {
        return food_name_list;
    }

    public int getCategory_list() {
        return category_list;
    }

    public int getAgent_fee() {
        return agent_fee;
    }

    public int getIs_premium() {
        return is_premium;
    }

    public int getAddress_type() {
        return address_type;
    }

    public int getGood_rating_rate() {
        return good_rating_rate;
    }

    public int getHas_image() {
        return has_image;
    }

    public int getHas_food_img() {
        return has_food_img;
    }

    public int getMin_deliver_amount() {
        return min_deliver_amount;
    }

    public int getTime_ensure_spent() {
        return time_ensure_spent;
    }

    public int getIs_time_ensure() {
        return is_time_ensure;
    }

    public int getIs_ka() {
        return is_ka;
    }

    public int getIs_time_ensure_discount() {
        return is_time_ensure_discount;
    }

    public int getIs_eleme_deliver() {
        return is_eleme_deliver;
    }

    public int getBu_flag() {
        return bu_flag;
    }

    public int getBrand_name() {
        return brand_name;
    }

    public int getService_rating() {
        return service_rating;
    }

    public int getInvoice() {
        return invoice;
    }

    public int getOnline_payment() {
        return online_payment;
    }

    public int getPublic_degree() {
        return public_degree;
    }

    public int getFood_num() {
        return food_num;
    }

    public int getFood_image_num() {
        return food_image_num;
    }

    public int getIs_promotion_info() {
        return is_promotion_info;
    }

    public int getIs_bookable() {
        return is_bookable;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public void setPrimary_category(int primary_category) {
        this.primary_category = primary_category;
    }

    public void setFood_name_list(int food_name_list) {
        this.food_name_list = food_name_list;
    }

    public void setCategory_list(int category_list) {
        this.category_list = category_list;
    }

    public void setAgent_fee(int agent_fee) {
        this.agent_fee = agent_fee;
    }

    public void setIs_premium(int is_premium) {
        this.is_premium = is_premium;
    }

    public void setAddress_type(int address_type) {
        this.address_type = address_type;
    }

    public void setGood_rating_rate(int good_rating_rate) {
        this.good_rating_rate = good_rating_rate;
    }

    public void setHas_image(int has_image) {
        this.has_image = has_image;
    }

    public void setHas_food_img(int has_food_img) {
        this.has_food_img = has_food_img;
    }

    public void setMin_deliver_amount(int min_deliver_amount) {
        this.min_deliver_amount = min_deliver_amount;
    }

    public void setTime_ensure_spent(int time_ensure_spent) {
        this.time_ensure_spent = time_ensure_spent;
    }

    public void setIs_time_ensure(int is_time_ensure) {
        this.is_time_ensure = is_time_ensure;
    }

    public void setIs_ka(int is_ka) {
        this.is_ka = is_ka;
    }

    public void setIs_time_ensure_discount(int is_time_ensure_discount) {
        this.is_time_ensure_discount = is_time_ensure_discount;
    }

    public void setIs_eleme_deliver(int is_eleme_deliver) {
        this.is_eleme_deliver = is_eleme_deliver;
    }

    public void setBu_flag(int bu_flag) {
        this.bu_flag = bu_flag;
    }

    public void setBrand_name(int brand_name) {
        this.brand_name = brand_name;
    }

    public void setService_rating(int service_rating) {
        this.service_rating = service_rating;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public void setOnline_payment(int online_payment) {
        this.online_payment = online_payment;
    }

    public void setPublic_degree(int public_degree) {
        this.public_degree = public_degree;
    }

    public void setFood_num(int food_num) {
        this.food_num = food_num;
    }

    public void setFood_image_num(int food_image_num) {
        this.food_image_num = food_image_num;
    }

    public void setIs_promotion_info(int is_promotion_info) {
        this.is_promotion_info = is_promotion_info;
    }

    public void setIs_bookable(int is_bookable) {
        this.is_bookable = is_bookable;
    }
}
