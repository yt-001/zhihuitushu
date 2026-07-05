package com.yt.zhihuitushu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yt.zhihuitushu.dto.request.BookCreateDTO;
import com.yt.zhihuitushu.dto.request.BookQueryDTO;
import com.yt.zhihuitushu.dto.request.BookUpdateDTO;
import com.yt.zhihuitushu.dto.response.ApiResponse;
import com.yt.zhihuitushu.dto.response.BookVO;
import com.yt.zhihuitushu.service.BookService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 书籍管理控制器（管理端）
 */
@RestController
@RequestMapping("/api/admin/books")
public class BookManageController {

    @Resource
    private BookService bookService;

    /**
     * 创建书籍
     * POST /api/admin/books
     */
    @PostMapping
    public ApiResponse<BookVO> createBook(@Valid @RequestBody BookCreateDTO dto) {
        BookVO bookVO = bookService.createBook(dto);
        return ApiResponse.success("创建成功", bookVO);
    }

    /**
     * 更新书籍
     * PUT /api/admin/books/{id}
     */
    @PutMapping("/{id}")
    public ApiResponse<BookVO> updateBook(@PathVariable Long id, @RequestBody BookUpdateDTO dto) {
        BookVO bookVO = bookService.updateBook(id, dto);
        return ApiResponse.success("更新成功", bookVO);
    }

    /**
     * 删除书籍
     * DELETE /api/admin/books/{id}
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ApiResponse.success("删除成功", null);
    }

    /**
     * 查询单个书籍
     * GET /api/admin/books/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<BookVO> getBookById(@PathVariable Long id) {
        BookVO bookVO = bookService.getBookById(id);
        if (bookVO == null) {
            return ApiResponse.error(404, "书籍不存在");
        }
        return ApiResponse.success(bookVO);
    }

    /**
     * 分页查询书籍列表
     * GET /api/admin/books
     */
    @GetMapping
    public ApiResponse<IPage<BookVO>> listBooks(BookQueryDTO queryDTO) {
        IPage<BookVO> page = bookService.listBooks(queryDTO);
        return ApiResponse.success(page);
    }

    /**
     * 批量删除书籍
     * DELETE /api/admin/books/batch
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteBooks(@RequestBody Map<String, List<Long>> request) {
        List<Long> ids = request.get("ids");
        if (ids == null || ids.isEmpty()) {
            return ApiResponse.error(400, "请选择要删除的书籍");
        }
        bookService.batchDeleteBooks(ids);
        return ApiResponse.success("批量删除成功", null);
    }

    /**
     * 更新书籍状态
     * PUT /api/admin/books/{id}/status
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateBookStatus(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        Integer status = request.get("status");
        if (status == null) {
            return ApiResponse.error(400, "状态不能为空");
        }
        bookService.updateBookStatus(id, status);
        return ApiResponse.success("状态更新成功", null);
    }
}