package com.yt.zhihuitushu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("books")
public class Book {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("author")
    private String author;

    @TableField("cover")
    private String cover;

    @TableField("category_id")
    private Long categoryId;

    @TableField("publisher")
    private String publisher;

    @TableField("publish_date")
    private LocalDate publishDate;

    @TableField("language")
    private String language;

    @TableField("pages")
    private Integer pages;

    @TableField("isbn")
    private String isbn;

    @TableField("description")
    private String description;

    @TableField("rating")
    private Double rating;

    @TableField("review_count")
    private Integer reviewCount;

    @TableField("read_count")
    private Integer readCount;

    @TableField("download_count")
    private Integer downloadCount;

    @TableField("status")
    private Integer status;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
