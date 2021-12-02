package com.wgcloud.entity;

import java.util.Date;

/**
 * @date: 2021.12.2
 */
public class DingSet extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = -8284741180883299533L;

    /**
     * 发送邮箱的帐号
     */
    private String fromDingName;


    /**
     * 接受告警信息的邮件
     */
    private String toPhone;


    /**
     * 创建时间
     */
    private Date createTime;

    public String getFromDingName() {
        return fromDingName;
    }

    public void setFromDingName(String fromDingName) {
        this.fromDingName = fromDingName;
    }

    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}