package com.example.myshop.service;

import com.example.myshop.dto.request.comment.CommentRequest;
import com.example.myshop.dto.request.track.TrackByUserRequest;
import com.example.myshop.dto.request.track.TrackCommentRequest;
import com.example.myshop.dto.request.track.TrackRequest;
import com.example.myshop.dto.response.track.TrackResponse;
import com.example.myshop.dto.view.CommentView;
import com.example.myshop.entity.CommentEntity;
import com.example.myshop.entity.TrackEntity;
import com.example.myshop.entity.UserEntity;
import com.example.myshop.repository.CommentRepository;
import com.example.myshop.repository.TrackRepository;
import com.example.myshop.repository.UserRepository;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import com.example.myshop.utils.GsonCustom;
import com.example.myshop.utils.MapperUtils;
import com.example.myshop.utils.Util;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private UserRepository userRepository;

    public BaseResponse create(CommentRequest commentRequest) {
        log.info("(create comment) request: {}", commentRequest);
        try {
            if (commentRequest.getMoment() == null || Util.isEmpty(commentRequest.getContent()) ||
                    commentRequest.getTrackId() == null
            ) {
                return BaseResponse.of(ResponseCode.INVALID_REQUEST);
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Integer userId = Integer.parseInt( (String) authentication.getPrincipal());
            // Lấy UserEntity
            UserEntity user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return BaseResponse.of(ResponseCode.USER_NOT_FOUND);
            }

            // Lấy TrackEntity
            TrackEntity track = trackRepository.findById(commentRequest.getTrackId()).orElse(null);
            if (track == null) {
                return BaseResponse.of(ResponseCode.NOT_FOUND_TRACK);
            }

            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setMoment(commentRequest.getMoment());
            commentEntity.setContent(commentRequest.getContent());
            commentEntity.setTrack(track);
            commentEntity.setUser(user);

            // Lưu vào DB
            commentRepository.save(commentEntity);
            log.info("(create comment) success");
            return BaseResponse.of(ResponseCode.SUCCESS);
        } catch (Exception e) {
            log.error("(create comment) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }
}
