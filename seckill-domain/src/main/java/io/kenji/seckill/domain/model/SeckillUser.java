package io.kenji.seckill.domain.model;


import java.io.Serial;
import java.io.Serializable;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/17
 **/
public class SeckillUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 8975856674362682714L;
    /**
     * User id
     */
    private Long id;
    /**
     * User name
     */
    private String userName;
    /**
     * Password
     */
    private String password;
    /**
     * 1: normal 2: frozen
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
