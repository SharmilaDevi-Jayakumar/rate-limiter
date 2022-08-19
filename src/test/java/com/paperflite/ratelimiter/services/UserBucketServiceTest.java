package com.paperflite.ratelimiter.services;

import com.paperflite.ratelimiter.Models.UserBucketInfoDTO;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserBucketServiceTest {

    private UserBucketService userBucketService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        userBucketService = new UserBucketService();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createUserBucket() {

        //Mock userBucketInfoDTO - input
        UserBucketInfoDTO userBucketInfoDTO = new UserBucketInfoDTO();
        userBucketInfoDTO.setUserID("1");
        userBucketInfoDTO.setRequestLimit("2");
        userBucketInfoDTO.setRequestTimeInMinutes("1");


        //Mock bucket object - output
        //Refill
        Refill refill = Refill.of(Long.parseLong(userBucketInfoDTO.getRequestLimit()), Duration.ofMinutes(Long.parseLong(userBucketInfoDTO.getRequestTimeInMinutes())));
        //Bandwidth specifications
        Bandwidth limit = Bandwidth.classic(Long.parseLong(userBucketInfoDTO.getRequestLimit()), refill);
        //Bucket specifications
        Bucket bucket = Bucket4j.builder().addLimit(limit).build();

        //Test Call
        userBucketService.createUserBucket(userBucketInfoDTO);

        //Assertion
        assertEquals(true,UserBucketService.userBucketMap.containsKey("1"));
        assertNotNull(UserBucketService.userBucketMap.get(userBucketInfoDTO.getUserID()));
    }


    @Test
    public void accessApplication(){

        //Mock Input Data
        String userId_1 = "1";
        String userIs_2 = "2";

        //Mock userBucketInfoDTO - input
        UserBucketInfoDTO userBucketInfoDTO = new UserBucketInfoDTO();
        userBucketInfoDTO.setUserID("1");
        userBucketInfoDTO.setRequestLimit("2");
        userBucketInfoDTO.setRequestTimeInMinutes("1");

        userBucketService.createUserBucket(userBucketInfoDTO);
        for (int i=1;i<=2;i++) {
            userBucketService.accessApplication(userId_1);
        }
        assertEquals(false,userBucketService.accessApplication(userId_1).isAccessAvailable);
        assertEquals("Too many request, Try again later",userBucketService.accessApplication(userId_1).message);

        assertEquals(false,userBucketService.accessApplication(userIs_2).isAccessAvailable);
        assertEquals("The user bucket is not created, So register the user",userBucketService.accessApplication(userIs_2).message);
    }
}
