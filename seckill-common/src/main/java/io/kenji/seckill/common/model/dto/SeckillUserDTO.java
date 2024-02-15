package io.kenji.seckill.common.model.dto;


import java.io.Serializable;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/22
 **/
public class SeckillUserDTO implements Serializable {
    private static final long serialVersionUID = -3045506676080226837L;
    /**
     * User name
     */
    private String userName;
    /**
     * password
     */
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
