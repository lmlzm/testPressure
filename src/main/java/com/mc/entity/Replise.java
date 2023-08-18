package com.mc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @className Replise
 * @description TODO
 * @author: LM理智梦
 * @date: 2023/8/18
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Replise implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String uname;

    private String message;

    @TableField("likeNum")
    private Integer like;

    private Integer rCount;

    private Date cTime;

}
