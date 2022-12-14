package com.example.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Book implements Serializable {

    /**
     * 编号
     */
    private Long id;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String writer;

    /**
     * 简介
     */
    private String introduction;

}
