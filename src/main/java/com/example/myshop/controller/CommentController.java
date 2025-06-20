package com.example.myshop.controller;

import com.example.myshop.dto.request.comment.CommentRequest;
import com.example.myshop.dto.request.track.TrackByUserRequest;
import com.example.myshop.dto.request.track.TrackCommentRequest;
import com.example.myshop.dto.request.track.TrackRequest;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.service.CommentService;
import com.example.myshop.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public BaseResponse create(@RequestBody CommentRequest request) {
        return commentService.create(request);
    }
}
