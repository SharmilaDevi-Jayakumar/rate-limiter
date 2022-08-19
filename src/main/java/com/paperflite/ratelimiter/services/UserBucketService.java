package com.paperflite.ratelimiter.services;

import com.paperflite.ratelimiter.Models.AccessInfoDTO;
import com.paperflite.ratelimiter.Models.UserBucketInfoDTO;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserBucketService {

    private final Logger logger = LoggerFactory.getLogger(UserBucketService.class);

    public static Map<String, Bucket> userBucketMap = new ConcurrentHashMap<>();

    public UserBucketService(){}


    public void createUserBucket(UserBucketInfoDTO userBucketInfoDTO){
        //Refill
        Refill refill = Refill.of(Long.parseLong(userBucketInfoDTO.getRequestLimit()), Duration.ofMinutes(Long.parseLong(userBucketInfoDTO.getRequestTimeInMinutes())));

        //Bandwidth specifications
        Bandwidth limit = Bandwidth.classic(Long.parseLong(userBucketInfoDTO.getRequestLimit()), refill);
        //Bucket specifications
        Bucket bucket = Bucket4j.builder().addLimit(limit).build();

        //Local cache to store user specific buckets
        userBucketMap.put(userBucketInfoDTO.getUserID(), bucket);
    }

    public AccessInfoDTO accessApplication(String id){
        AccessInfoDTO accessInfoDTO = new AccessInfoDTO();
        if(userBucketMap.containsKey(id)){
           //try to consume the bucket
            if(userBucketMap.get(id).tryConsume(1)) {
                logger.info("The user with userID " + id + " request is accepted");
                accessInfoDTO.setAccessAvailable(true);
                accessInfoDTO.setMessage("The request is processed successfully");
            }
            else {
                logger.info("The user with userId "+ id + " has requested too many hits in the decided time");
                accessInfoDTO.setAccessAvailable(false);
                accessInfoDTO.setMessage("Too many request, Try again later");
            }

        } else {
            logger.error("The user bucket is not present, so kindly register the user bucket");
            accessInfoDTO.setAccessAvailable(false);
            accessInfoDTO.setMessage("The user bucket is not created, So register the user");
            return accessInfoDTO;
        }
        return accessInfoDTO;
    }
}
