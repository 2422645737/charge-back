package com.wanghui.shiyue.comm.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 基础PO
 * @fileName: BasePO
 * @author: wanghui
 * @createAt: 2024/01/13 07:28:53
 * @updateBy:
 * @copyright: 众阳健康
 */
@Data
public class BasePO implements Serializable {

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    public void init(){
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
