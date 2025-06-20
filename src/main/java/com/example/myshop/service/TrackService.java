package com.example.myshop.service;

import com.example.myshop.dto.request.track.TrackByUserRequest;
import com.example.myshop.dto.request.track.TrackCommentRequest;
import com.example.myshop.dto.request.track.TrackRequest;
import com.example.myshop.dto.request.user.ChangePasswordRequest;
import com.example.myshop.dto.request.user.UserRequest;
import com.example.myshop.dto.request.user.UserUpdateRequest;
import com.example.myshop.dto.response.track.TrackCommentResponse;
import com.example.myshop.dto.response.track.TrackResponse;
import com.example.myshop.dto.response.user.UserResponse;
import com.example.myshop.dto.view.CommentView;
import com.example.myshop.entity.CommentEntity;
import com.example.myshop.entity.TrackEntity;
import com.example.myshop.entity.UserEntity;
import com.example.myshop.repository.CommentRepository;
import com.example.myshop.repository.TrackRepository;
import com.example.myshop.repository.UserRepository;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import com.example.myshop.utils.BcryptUtils;
import com.example.myshop.utils.GsonCustom;
import com.example.myshop.utils.MapperUtils;
import com.example.myshop.utils.Util;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.myshop.utils.BcryptUtils.getPasswordEncoder;
import static com.example.myshop.utils.MapperUtils.MODEL_MAPPER;

@Service
@Log4j2
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private CommentRepository commentRepository;

    public BaseResponse create(TrackRequest trackRequest) {
        log.info("(create) request: {}", trackRequest);
        try {
            if (Util.isEmpty(trackRequest.getTitle()) || Util.isEmpty(trackRequest.getDescription()) ||
                    Util.isEmpty(trackRequest.getImgUrl()) || Util.isEmpty(trackRequest.getTrackUrl()) ||
                    Util.isEmpty(trackRequest.getCategory())
            ) {
                return BaseResponse.of(ResponseCode.INVALID_REQUEST);
            }

            TrackEntity trackEntity = MapperUtils.toEntity(trackRequest, TrackEntity.class);
            TrackResponse trackResponse = MapperUtils.toDto(trackRepository.save(trackEntity), TrackResponse.class);
            log.info("(create) track success");
            return BaseResponse.of(ResponseCode.SUCCESS, trackResponse);
        } catch (Exception e) {
            log.error("(create) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }

    public BaseResponse getListByUser(TrackByUserRequest trackRequest) {
        log.info("(getListByUser) request: {}", GsonCustom.getGsonBuilder().toJson(trackRequest));
        try {
            if (trackRequest == null || trackRequest.getId() == null) {
                return BaseResponse.of(ResponseCode.INVALID_REQUEST);
            }
            Pageable pageable = PageRequest.of(trackRequest.getCurrent(), trackRequest.getPageSize());
            Page<TrackEntity> entityList = trackRepository.findByCreatedBy(trackRequest.getId(), pageable);
            log.info("(getListByUser) entityList {} ", GsonCustom.getGsonBuilder().toJson(entityList));
            List<TrackResponse> trackResponseList = entityList.getContent().stream()
                    .map(entity -> MapperUtils.toDto(entity, TrackResponse.class))
                            .collect(Collectors.toList());
            log.info("(getListByUser) success");
            return BaseResponse.of(ResponseCode.SUCCESS, trackResponseList);
        } catch (Exception e) {
            log.error("(getListByUser) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }

    public BaseResponse getAllTrack() {
        log.info("(getAllTrack) start");
        try {
            List<TrackEntity> entityList = trackRepository.findAll();
            List<TrackResponse> trackResponseList = entityList.stream()
                    .map(entity -> MapperUtils.toDto(entity, TrackResponse.class))
                    .collect(Collectors.toList());
            log.info("(getAllTrack) success");
            return BaseResponse.of(ResponseCode.SUCCESS, trackResponseList);
        } catch (Exception e) {
            log.error("(getAllTrack) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }

    public BaseResponse getTrackById(Long id) {
        log.info("(getTrackById) request: {}", id);
        try {
            if (id == null) {
                return BaseResponse.of(ResponseCode.INVALID_REQUEST);
            }
            TrackEntity trackEntity = trackRepository.findById(id).orElse(null);
            if (trackEntity == null) {
                return BaseResponse.of(ResponseCode.NOT_FOUND_DATA);
            }
            TrackResponse trackResponse = MapperUtils.toDto(trackEntity, TrackResponse.class);
            log.info("(getTrackById) success");
            return BaseResponse.of(ResponseCode.SUCCESS, trackResponse);
        } catch (Exception e) {
            log.error("(getTrackById) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }

    public BaseResponse getListCommentByTrack(TrackCommentRequest trackRequest) {
        log.info("(getListCommentByTrack) request: {}", GsonCustom.getGsonBuilder().toJson(trackRequest));
        try {
            if (trackRequest == null) {
                return BaseResponse.of(ResponseCode.INVALID_REQUEST);
            }
            Pageable pageable = PageRequest.of(trackRequest.getCurrent(), trackRequest.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<CommentView> entityList = commentRepository.findByTrackIdProjected(trackRequest.getTrackId(), pageable);
            log.info("(getListCommentByTrack) success");
            return BaseResponse.of(ResponseCode.SUCCESS, entityList.getContent());
        } catch (Exception e) {
            log.error("(getListCommentByTrack) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }
}
