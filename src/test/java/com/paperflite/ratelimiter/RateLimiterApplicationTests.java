package com.paperflite.ratelimiter;

import com.paperflite.ratelimiter.Models.AccessInfoDTO;
import com.paperflite.ratelimiter.services.UserBucketService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

@SpringBootTest
class RateLimiterApplicationTests {

	@Mock
	UserBucketService userBucketService;

	@Before
	public void init() {
		Refill refill = Refill.of(2, Duration.ofMinutes(1));
		Bandwidth limit = Bandwidth.classic(1,refill);
		Bucket bucket = Bucket4j.builder().addLimit(limit).build();

		UserBucketService.userBucketMap.put("1",bucket);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void grantAccess(){

		String userId = "1";

		AccessInfoDTO accessInfoDTO = new AccessInfoDTO();
		accessInfoDTO.setMessage("The user with userId1has requested too many hits in the decided time");
		accessInfoDTO.setAccessAvailable(false);

		for (int i=1;i<=2;i++) {
			userBucketService.accessApplication(userId);
		}
		assertEquals(accessInfoDTO,userBucketService.accessApplication(userId));
	}

}
