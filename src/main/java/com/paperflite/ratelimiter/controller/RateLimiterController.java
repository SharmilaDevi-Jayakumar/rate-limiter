package com.paperflite.ratelimiter.controller;

import com.paperflite.ratelimiter.Models.AccessInfoDTO;
import com.paperflite.ratelimiter.Models.UserBucketCreationDTO;
import com.paperflite.ratelimiter.Models.UserBucketInfoDTO;
import com.paperflite.ratelimiter.services.UserBucketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rateLimiter/")
public class RateLimiterController {

    private final UserBucketService userBucketService;
    private final Logger logger = LoggerFactory.getLogger(RateLimiterController.class);

    @Autowired
    public RateLimiterController(UserBucketService userBucketService) {
        this.userBucketService = userBucketService;
    }

    @PostMapping(value = "/userBucketRegistration")
    //Assuming the default request Rate to be 10 per minute
    public UserBucketCreationDTO createUserBucket(@RequestParam("userId") String userId, @RequestParam(value= "requestLimit", defaultValue = "10") String requestLimit, @RequestParam(value = "requestTimeMinutes", defaultValue = "1") String requestTimeMinutes) {
        UserBucketCreationDTO userBucketCreationDTO = new UserBucketCreationDTO();
        try {
            UserBucketInfoDTO userBucketInfoDTO = new UserBucketInfoDTO();
            userBucketInfoDTO.setUserID(userId);
            userBucketInfoDTO.setRequestLimit(requestLimit);
            userBucketInfoDTO.setRequestTimeInMinutes(requestTimeMinutes);

            this.userBucketService.createUserBucket(userBucketInfoDTO);
            userBucketCreationDTO.setUserBucketCreated(true);
        } catch (Exception e) {
            logger.error("Error occurred while creating user bucket" + e.getMessage());
            userBucketCreationDTO.setUserBucketCreated(false);
        }
        return userBucketCreationDTO;
    }

    @GetMapping("/grant-access")
    public AccessInfoDTO grantAccessToService(@RequestParam("userId") String userId) {
        AccessInfoDTO accessInfoDTO = new AccessInfoDTO();
        try {
             accessInfoDTO = this.userBucketService.accessApplication(userId);
        } catch (Exception e) {
            logger.error("Error occurred while granting access to the client");
            accessInfoDTO.setAccessAvailable(false);
            accessInfoDTO.setMessage("Error occurred while granting access to the client, Kindly try again");
        }
        return accessInfoDTO ;
    }
}
