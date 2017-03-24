package me.ele.hackathon.data.okl;

/**
 * 用户信息
 *
 * @author oukailiang
 * @create 2016-10-30 上午9:34
 */

public class User {
    private String user_id;
    private String is_old;

    public String getIs_old() {
        return is_old;
    }

    public void setIs_old(String is_old) {
        this.is_old = is_old;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
