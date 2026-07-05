package com.yt.zhihuitushu.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 更新书籍的请求参数
 */
@Getter
@Setter
public class BookUpdateDTO {

    /**
     * 书籍标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 封面图片URL
     */
    private String cover;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 出版日期
     */
    private LocalDate publishDate;

    /**
     * 语言
     */
    private String language;

    /**
     * 页数
     */
    private Integer pages;

    /**
     * ISBN编号
     */
    private String isbn;

    /**
     * 书籍描述
     */
    private String description;

    /**
     * 状态：0-下架 1-上架
     */
    private Integer status;
}