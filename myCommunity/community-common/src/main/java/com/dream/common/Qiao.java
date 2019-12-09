package com.dream.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Qiao implements Serializable {
    private static final long serialVersionUID = -2972088766561758830L;
    /**姓名*/
    private String name;
    /**年龄*/
    private Integer age;
}
