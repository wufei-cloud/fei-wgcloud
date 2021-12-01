package com.wgcloud.entity;

import java.util.Date;

/**
 * @version v2.3
 * @ClassName:DiskIoState.java
 * @author: http://www.wgstart.com
 * @date: 2019年11月16日
 * @Description: 查看磁盘IO使用情况
 * @Copyright: 2017-2021 wgcloud. All rights reserved.
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