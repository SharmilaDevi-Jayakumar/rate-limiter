package com.paperflite.ratelimiter.Models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
public class UserBucketInfoDTO {

    public String userID;
    public String requestLimit;
    public String requestTimeInMinutes;
}
