/**
  * Copyright 2023 bejson.com 
  */
package com.mc.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2023-08-18 14:12:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Replies implements Serializable {

    private int count;
    private int rcount;
    private long ctime;
    private int like;
    private Member member;
    private Content content;
    private List<Replies> replies;

}