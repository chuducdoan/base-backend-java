package com.example.myshop.controller;

import com.example.myshop.dto.request.user.ChangePasswordRequest;
import com.example.myshop.dto.request.user.UserRequest;
import com.example.myshop.dto.request.user.UserUpdateRequest;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.service.UserService;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
@RequestMapping(value = "/api/v1/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/create")
    public BaseResponse createUser(@RequestBody @Valid UserRequest request) {
        return userService.create(request);
    }

    @PutMapping(value = "/update")
    public BaseResponse updateUser(@RequestBody @Valid UserUpdateRequest request) {
        return userService.update(request);
    }

    @PostMapping(value = "/change-password")
    public BaseResponse changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return userService.changePassword(request);
    }

//    @GetMapping(value = "/download")
//    public ResponseEntity<byte[]> downloadExcel() {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Example Sheet");
//
//        Row row = sheet.createRow(0);
//        Cell cell = row.createCell(0);
//        cell.setCellValue("Hello, Excel");
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            workbook.write(outputStream);
//            workbook.close();
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//        byte[] excelFile = outputStream.toByteArray();;
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=example.xlsx");
//        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
//    }
}
