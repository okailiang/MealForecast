package me.ele.hackathon.object;

/**
 * Created by solomon on 16/10/17.
 */
public class OrderInfo {

    int day_no;                 //同上
    int minutes;              	 //同上
    int order_id;             	 //订单ID
    int restaurant_id;        	 //餐厅ID
    int deliver_fee;          	 //总配送费
    int is_online_paid;       	 //是否在线支付
    int order_process_minutes; 	 //订单处理时间（分钟)
    int restaurant_num;       	 //该订单在本餐厅内的第几单
    int address_type;         	 //收货地址类型，community/office/school/netbar/other
    int is_valid;             	 //是否有效订单
    int is_book;              	 //是否预定单
    int is_coupon;            	 //是否使用抵用券
    int is_invoice;           	 //是否开了发票
    int pindan_flag;          	 //拼单标志, 1拼单，0非拼单
    int bu_flag_name ;        	 //订单所属BU，分高校，白领，早餐，代理商四种
    int eleme_order_total;    	 //用户实付总价（满减及各类优惠之后的价格）
    int total;                	 //订单总价(满减及各类优惠之前的总价）
    int cut_money;            	 //满减优惠金额
    int is_activity;          	 //订单是否参与活动，1是，0否
    int has_new_user_subsidy;  //是否享用新客补贴，1是，0否
    int hongbao_amount    ;        //使用红包金额
    int receiver_deliver_fee; 	 //收货人实际缴纳的配送费（会员卡减免后的配送费）
    int user_id;             	 //用户ID
    int food_name ;           	 //该订单所涉及主要食物名称（已规范化）
    int food_category ;       	 //该订单所涉及主要食物分类

    public int getDay_no() {
        return day_no;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public int getDeliver_fee() {
        return deliver_fee;
    }

    public int getIs_online_paid() {
        return is_online_paid;
    }

    public int getOrder_process_minutes() {
        return order_process_minutes;
    }

    public int getRestaurant_num() {
        return restaurant_num;
    }

    public int getAddress_type() {
        return address_type;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public int getIs_book() {
        return is_book;
    }

    public int getIs_coupon() {
        return is_coupon;
    }

    public int getIs_invoice() {
        return is_invoice;
    }

    public int getPindan_flag() {
        return pindan_flag;
    }

    public int getBu_flag_name() {
        return bu_flag_name;
    }

    public int getEleme_order_total() {
        return eleme_order_total;
    }

    public int getTotal() {
        return total;
    }

    public int getCut_money() {
        return cut_money;
    }

    public int getIs_activity() {
        return is_activity;
    }

    public int getHas_new_user_subsidy() {
        return has_new_user_subsidy;
    }

    public int getHongbao_amount() {
        return hongbao_amount;
    }

    public int getReceiver_deliver_fee() {
        return receiver_deliver_fee;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getFood_name() {
        return food_name;
    }

    public int getFood_category() {
        return food_category;
    }
}
