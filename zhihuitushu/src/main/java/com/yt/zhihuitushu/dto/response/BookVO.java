package com.yt.zhihuitushu.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 返回给前端的书籍详细信息
 */
@Getter
@Setter
public class BookVO {

    /**
     * 书籍ID
     */
    private Long id;

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
     * 分类名称
     */
    private String categoryName;

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
     * 评分
     */
    private Double rating;

    /**
     * 评论数
     */
    private Integer reviewCount;

    /**
     * 阅读数
     */
    private Integer readCount;

    /**
     * 下载数
     */
    private Integer downloadCount;

    /**
     * 状态：0-下架 1-上架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}