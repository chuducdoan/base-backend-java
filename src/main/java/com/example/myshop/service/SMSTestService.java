package com.example.myshop.service;

import com.example.myshop.constant.SmsLength;
import com.example.myshop.dto.request.smstest.SMSSendRequestDTO;
import com.example.myshop.dto.request.smstest.SMSTestRequestDTO;
import com.example.myshop.dto.response.smstest.SMSTestResponseDTO;
import com.example.myshop.entity.SMSTestEntity;
import com.example.myshop.repository.SMSTestRepository;
import com.example.myshop.response.BaseResponse;
import com.example.myshop.response.ResponseCode;
import com.example.myshop.response.SmsTest.SmsTestResponseCode;
import com.example.myshop.utils.MapperUtils;
import com.example.myshop.utils.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class SMSTestService {
    private static final String SMS_URL = "https://smsgw.vnpaytest.vn/smsgw/sendSms";

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    @Autowired
    private SMSTestRepository smsTestRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final Gson gson;

    private final RedisTemplate<String, Object> redisTemplate;

    public SMSTestService(Gson gson, RedisTemplate<String, Object> redisTemplate) {
        this.gson = gson;
        this.redisTemplate = redisTemplate;
    }

    public BaseResponse create(SMSTestRequestDTO smsTestDTO) {
        log.info("Begin create smsTest with request: ", gson.toJson(smsTestDTO));
        try {
            // Check require field messageId, keyword, sender, destination, shortMessage
            if(null == smsTestDTO
                    || Util.isEmpty(smsTestDTO.getMessageId())
                    || Util.isEmpty(smsTestDTO.getKeyword())
                    || Util.isEmpty(smsTestDTO.getSender())
                    || Util.isEmpty(smsTestDTO.getDestination())
                    || Util.isEmpty(smsTestDTO.getShortMessage())
            ) {
                log.info("Request null or missing field");
                return BaseResponse.of(ResponseCode.INVALID_REQUEST);
            }

            // Check max length field
            if(smsTestDTO.getMessageId().length() > SmsLength.MESSAGE_ID.getMaxLength()) {
                log.info("Message id exceeds max length");
                return BaseResponse.of((SmsTestResponseCode.MESSAGE_ID_OVER_MAX_LENGTH));
            }
            if(smsTestDTO.getKeyword().length() > SmsLength.KEYWORD.getMaxLength()) {
                log.info("Keyword exceeds max length");
                return BaseResponse.of((SmsTestResponseCode.KEYWORD_OVER_MAX_LENGTH));
            }
            if(smsTestDTO.getSender().length() > SmsLength.SENDER.getMaxLength()) {
                log.info("Sender exceeds max length");
                return BaseResponse.of((SmsTestResponseCode.SENDER_OVER_MAX_LENGTH));
            }
            if(smsTestDTO.getDestination().length() > SmsLength.DESTINATION.getMaxLength()) {
                log.info("Destination exceeds max length");
                return BaseResponse.of((SmsTestResponseCode.DESTINATION_OVER_MAX_LENGTH));
            }
            if(smsTestDTO.getShortMessage().length() > SmsLength.SHORT_MESSAGE.getMaxLength()) {
                log.info("Short message exceeds max length");
                return BaseResponse.of((SmsTestResponseCode.SHORT_MESSAGE_OVER_MAX_LENGTH));
            }

            // check valid phone number
            if(!Util.validatePhoneNumber(smsTestDTO.getDestination())) {
                log.info("Invalid phone number");
                return BaseResponse.of(SmsTestResponseCode.INVALID_PHONE_NUMBER);
            }

            // check messageId is exist
            SMSTestEntity smsTestEntityByMessageId = smsTestRepository.findByMessageId(smsTestDTO.getMessageId());
            if (smsTestEntityByMessageId != null) {
                log.info("MessageId already exists");
                return BaseResponse.of(SmsTestResponseCode.EXIST_MESSAGE_ID);
            }

            // convert data from dto to entity
            SMSTestEntity smsTestEntity = MapperUtils.toEntity(smsTestDTO, SMSTestEntity.class);
            SMSTestEntity smsTestCreate = smsTestRepository.save(smsTestEntity);
            log.info("Create smsTest success");
            return BaseResponse.of(smsTestCreate);
        } catch (Exception e) {
            log.error("Create smsTest fail: ", e);
            return BaseResponse.of(ResponseCode.SYSTEM_ERROR);
        }
    }

    public void sendSmsDataToQueue() {
        List<SMSTestEntity> listSMSEntity = smsTestRepository.findAll();
        log.info("Sending data from list request through queue message");
        try {
            for (SMSTestEntity data: listSMSEntity) {
                rabbitTemplate.convertAndSend(exchange, routingKey, data);
                log.info("Sent SMS: {}", data);
            }
        } catch (Exception e) {
            log.error("Error sending data to queue: {}", e.getMessage());
        }
    }

    private String generateUniqueMessageId() {
        // Tạo messageId duy nhất, có thể sử dụng UUID hoặc một phương pháp khác
        return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }

    @RabbitListener(queues = "sms.mt.dev")
    public void consumerSMSTest(byte[] message) {
        try {
            SMSTestResponseDTO smsTestDTO = Util.convertMessageFromByteToDTO(message);
            String key = smsTestDTO.getShortMessage() + ":" + smsTestDTO.getDestination();
            // Kiểm tra trùng lặp trong redis
            if (redisTemplate.opsForValue().get(key) == null) {
                SMSSendRequestDTO smsSendRequestDTO = MapperUtils.toDto(smsTestDTO, SMSSendRequestDTO.class);
                // Gửi yêu cầu tới api
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(SMS_URL, smsSendRequestDTO, String.class);
                log.info("Sent sms ------");
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    SMSTestEntity smsTestEntity = smsTestRepository.findById(smsTestDTO.getId())
                            .orElseThrow(() -> new RuntimeException("SMSTestEntity not found with ID: " + smsTestDTO.getId()));
                    String status = extractValue(responseEntity.getBody(), "status");
                    String description = extractValue(responseEntity.getBody(), "description");
                    smsTestEntity.setStatus(Integer.parseInt(status));
                    smsTestEntity.setDescription(description);
                    smsTestEntity.setSentTime(System.currentTimeMillis());
                    smsTestRepository.save(smsTestEntity);
                    // Lưu trữ key vào Redis để đánh dấu rằng đã kiểm tra trùng
                    redisTemplate.opsForValue().set(key, "sent");
                    log.info("Sent sms success, key: {}", key);
                } else {
                    log.error("Sent sms fail: {}", responseEntity.getBody());
                }
            } else {
                log.info("Messages sent before with key: {}", key);
            }
        } catch (Exception e) {
            log.error("Error processing received message: {}", e.getMessage());
        }
    }

    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return null; // Trả về null nếu không tìm thấy
        }
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
}
