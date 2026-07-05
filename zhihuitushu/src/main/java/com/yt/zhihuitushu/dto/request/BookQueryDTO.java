package com.yt.zhihuitushu.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询书籍的条件参数
 */
@Getter
@Setter
public class BookQueryDTO {

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页数量
     */
    private Integer size = 10;

    /**
     * 关键词（标题或作者）
     */
    private String keyword;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 状态：0-下架 1-上架
     */
    private Integer status;

    /**
     * 语言
     */
    private String language;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 排序字段
     */
    private String sortBy;

    /**
     * 排序方向：asc-升序 desc-降序
     */
    private String sortOrder = "desc";
}