package com.yt.zhihuitushu.controller;

import com.yt.zhihuitushu.dto.response.ApiResponse;
import com.yt.zhihuitushu.entity.Book;
import com.yt.zhihuitushu.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Resource
    private BookService bookService;

    @GetMapping
    public ApiResponse<List<Book>> listBooks(
            @RequestParam(value = "type", defaultValue = "recommend") String type,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        List<Book> books = switch (type.toLowerCase()) {
            case "hot" -> bookService.listHot(limit);
            case "new" -> bookService.listNew(limit);
            default -> bookService.listRecommended(limit);
        };
        return ApiResponse.success(books);
    }
}
