package com.example.myshop.controller;

import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@Log4j2
public class FileUploadController {

    @PostMapping("/upload")
    public BaseResponse uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        if (file == null || file.isEmpty()) {
            return BaseResponse.of(ResponseCode.FILE_EMPTY);
        }
        try {
            // Lưu file vào thư mục tạm (hoặc cấu hình thư mục riêng)
            String uploadDir = System.getProperty("user.dir") + "/uploads";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Lấy tên file gốc
            String originalFilename = file.getOriginalFilename();
            // Tách tên và đuôi mở rộng
            String name = originalFilename.substring(0, originalFilename.lastIndexOf("."));
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String newNameFile = name + "_" + System.currentTimeMillis() + "." + ext;


            Path filepath = Paths.get(uploadDir, newNameFile);
            file.transferTo(filepath.toFile());

            // Tạo link truy cập file (giả định ứng dụng đang chạy tại port 8080)
            String fileUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/uploads/" + newNameFile;

            Map<String, String> response = new HashMap<>();
            response.put("fileName", newNameFile);
            response.put("fileUrl", fileUrl);

            log.info("(upload file) upload file success");
            return BaseResponse.of(ResponseCode.SUCCESS, response);
        } catch (IOException e) {
            log.error("(upload file) upload file error, {}", e.getMessage());
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }
}
