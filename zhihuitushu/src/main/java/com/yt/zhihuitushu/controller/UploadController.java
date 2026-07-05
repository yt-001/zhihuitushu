package com.yt.zhihuitushu.controller;

import com.yt.zhihuitushu.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api")
public class UploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/covers";
    private static final String COVERS_URL_PREFIX = "/api/files/covers/";

    private static final String[] ALLOWED_TYPES = {
            "image/jpeg", "image/png", "image/gif", "image/webp", "image/bmp"
    };

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    /**
     * 上传图书封面
     * POST /api/upload/cover
     */
    @PostMapping("/upload/cover")
    public ApiResponse<Map<String, String>> uploadCover(@RequestParam("file") MultipartFile file,
                                                         HttpServletRequest request) {
        if (file == null || file.isEmpty()) {
            return ApiResponse.error(400, "请选择要上传的封面图片");
        }
        if (!isAllowedType(file.getContentType())) {
            return ApiResponse.error(400, "仅支持 jpg、png、gif、webp、bmp 格式的图片");
        }
        if (file.getSize() > MAX_SIZE) {
            return ApiResponse.error(400, "图片大小不能超过 5MB");
        }

        String originalFilename = Objects.requireNonNullElse(file.getOriginalFilename(), "cover.jpg");
        String extension = getExtension(originalFilename).toLowerCase();
        String filename = UUID.randomUUID() + "_" + System.currentTimeMillis() + extension;

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(filename);
            file.transferTo(filePath.toFile());

            String url = buildUrl(request, filename);
            return ApiResponse.success("上传成功", Map.of("url", url, "filename", filename));
        } catch (IOException e) {
            return ApiResponse.error(500, "封面上传失败：" + e.getMessage());
        }
    }

    /**
     * 读取封面图片
     * GET /api/files/covers/{filename}
     */
    @GetMapping("/files/covers/{filename}")
    public ResponseEntity<Resource> getCover(@PathVariable String filename) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Path filePath = uploadPath.resolve(filename).normalize();
            if (!filePath.startsWith(uploadPath)) {
                return ResponseEntity.badRequest().build();
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = resolveContentType(filename);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isAllowedType(String contentType) {
        if (contentType == null) {
            return false;
        }
        for (String allowed : ALLOWED_TYPES) {
            if (allowed.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex) : ".jpg";
    }

    private String resolveContentType(String filename) {
        String ext = getExtension(filename);
        return switch (ext) {
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".webp" -> "image/webp";
            case ".bmp" -> "image/bmp";
            default -> "image/jpeg";
        };
    }

    private String buildUrl(HttpServletRequest request, String filename) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String base = scheme + "://" + host;
        if (port != 80 && port != 443) {
            base += ":" + port;
        }
        return base + COVERS_URL_PREFIX + filename;
    }
}
