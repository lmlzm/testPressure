/**
  * Copyright 2023 bejson.com 
  */
package com.mc.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2023-08-18 14:12:49
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Content implements Serializable {

    private String message;
    private List<Member> members;
    private int max_line;

}