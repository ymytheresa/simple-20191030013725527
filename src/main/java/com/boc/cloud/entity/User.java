package com.boc.cloud.entity;

/**
 * 用戶表, 用於OAuth認證.
 * 
 * @author cyper
 *
 */
public class User extends BaseEntity {
    /**
     * 用戶名: 格式custXXYY, 其中XX是組編號, YY是用戶號, 如cust0101, cust0102.
     */
    private String username;

    /**
     * 密碼
     */
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
