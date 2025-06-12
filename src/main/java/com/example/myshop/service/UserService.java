package com.example.myshop.service;

import com.example.myshop.dto.request.user.ChangePasswordRequest;
import com.example.myshop.dto.request.user.UserRequest;
import com.example.myshop.dto.request.user.UserUpdateRequest;
import com.example.myshop.dto.response.user.UserResponse;
import com.example.myshop.entity.UserEntity;
import com.example.myshop.repository.UserRepository;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import com.example.myshop.utils.BcryptUtils;
import com.example.myshop.utils.MapperUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import static com.example.myshop.utils.MapperUtils.MODEL_MAPPER;
import static com.example.myshop.utils.BcryptUtils.getPasswordEncoder;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public BaseResponse create(UserRequest userRequest) {
        log.info("(create) request: {}", userRequest);
        try {
            if (checkExistedUsername(userRequest.getUsername())) {
                return BaseResponse.of(ResponseCode.USERNAME_EXIST);
            }
            if (checkExistedEmail(userRequest.getEmail())) {
                return BaseResponse.of(ResponseCode.EMAIL_EXIST);
            }
            UserEntity userEntity = MapperUtils.toEntity(userRequest, UserEntity.class);
            userEntity.setPassword(BcryptUtils.getPasswordEncoder().encode(userRequest.getPassword()));
            userEntity.setIsActive(0);
            UserResponse userResponse = MapperUtils.toDto(userRepository.save(userEntity), UserResponse.class);
            log.info("(create) user success");
            return BaseResponse.of(ResponseCode.SUCCESS, userResponse);
        } catch (Exception e) {
            log.error("(create) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }

    public BaseResponse update(UserUpdateRequest userUpdateRequest) {
        log.info("(update) request: {}", userUpdateRequest);
        try {
            UserEntity userEntity = find(userUpdateRequest.getId());
            if (userEntity == null) {
                log.error("(Update) user not found, id: {}", userUpdateRequest.getId());
                return BaseResponse.of(ResponseCode.USER_NOT_FOUND);
            }

            if (checkExistedPreUpdate(userEntity, userUpdateRequest.getEmail())) {
                return BaseResponse.of(ResponseCode.EMAIL_EXIST);
            } else {
                MODEL_MAPPER.map(userUpdateRequest, userEntity);
                UserResponse userResponse = MapperUtils.toDto(userRepository.save(userEntity), UserResponse.class);
                log.info("(update) success");
                return BaseResponse.of(ResponseCode.SUCCESS, userResponse);
            }
        } catch(Exception e) {
            log.error("(update) false");
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }

    public Boolean equalPassword(String passwordRaw, String passwordEncrypted) {
        return getPasswordEncoder().matches(passwordRaw, passwordEncrypted);
    }

    public BaseResponse changePassword(ChangePasswordRequest request) {
        log.info("(changePassword)  request: {}", request);
        UserEntity userEntity = find(request.getId());
        if (userEntity == null) {
            log.error("(changePassword) user not found, id: {}", request.getId());
            return BaseResponse.of(ResponseCode.USER_NOT_FOUND);
        }
        if (!equalPassword(request.getPassword(), userEntity.getPassword())) {
            log.error("(changePassword) password no match, password: {}", request.getPassword());
            return BaseResponse.of(ResponseCode.PASSWORD_NO_MATCHES);
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            log.error("(changePassword) password no match, new password: {}, confirm password: {}",
                    request.getNewPassword(), request.getConfirmPassword());
            return BaseResponse.of(ResponseCode.PASSWORD_NO_MATCHES);
        }
        userEntity.setPassword(getPasswordEncoder().encode(request.getNewPassword()));
        userRepository.save(userEntity);
        log.info("(changePassword) success");
        return BaseResponse.of(ResponseCode.SUCCESS);
    }

    private boolean checkExistedPreUpdate(UserEntity userEntity, String email) {
        log.info("(checkExistedPreUpdate) email: {}", email);
        if (!userEntity.getEmail().equals(email)) {
            return checkExistedEmail(email);
        }
        return false;
    }

    private boolean checkExistedEmail(String email) {
        log.info("(checkExistedEmail) email: {}", email);
        if (userRepository.existsByEmail(email)) {
            log.error("(checkExistedEmail) email already exist");
            return true;
        }
        return false;
    }

    private boolean checkExistedUsername(String username) {
        log.info("(checkExistedUsername) username: {}", username);
        if (userRepository.existsByUsername(username)) {
            log.error("(checkExistedUsername) username already exist");
            return true;
        }
        return false;
    }

    private UserEntity find(Integer id) {
        log.info("(find) id: {}", id);
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            return null;
        }
        return optionalUserEntity.get();
    }

    public UserEntity getByUsername(String username) {
        log.info("(getByUsername) username: {}", username);
        return userRepository.getByUsername(username);
    }
}
