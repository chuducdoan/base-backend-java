package com.example.myshop.dto.view;

import java.time.LocalDateTime;

public interface CommentView {
    Long getId();
    String getContent();
    Long getMoment(); // vì moment trong entity là Long
    Integer getUserId(); // nên đổi sang Long cho khớp
    String getUserName();
    LocalDateTime getCreatedAt();
    String getType();
}
