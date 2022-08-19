# Rate Limiter Springboot Project description

A rate limiter is a tool that monitors the number of requests per
a window time a service agrees to allow. If the request count exceeds the number agreed by the
service owner, and the user (in a decided window time), the rate limiter blocks all the excess
calls (say by throwing exceptions).

## Algorithm Used for rate limiting 
The token bucket algorithm is based on an analogy of a fixed capacity bucket into which tokens are added at a fixed rate. 
Before allowing an API to proceed, the bucket is inspected to see if it contains at least 1 token at that time. 
If so, one token is removed from the bucket, the API is allowed to proceed. 
In case there are no tokens available, the API is returned saying that the quota has exceeded during that window time!

## Assumptions
1) Centralized System - local cache using Concurrent Hash Map is used to store user bucket preference and information.
2) User and Service agreement regarding the request limit and window time happens via an API call (createUserBucket), and no 
external DB is used to store/retrieve user-service agreement.
3) Every client (user/microservice) will get registered to the rate limiter with the agreement before requesting the targeted service.   
4) Default request limit is 10 per minute if the client has not configured in the API request.

## Dependencies Used
1) Spring Boot starter dependencies
2) Bucket4j - Java rate-limiting library based on the token-bucket algorithm
3) Junit for test cases

## Steps to run the application in local
Step 1: Fetch the springboot project from https://github.com/SharmilaDevi-Jayakumar/rate-limiter <br>
Step 2: This project uses maven as the build tool, so run maven install to install the required maven dependencies specified in the pom.xml <br>
Step 3: Run the project. <br>

## Scaling 
1) To use distributed cache like redis, hazelcast when this project is used in distributed environment.
2) If required, use categorization of client, like for example FREE user, BASIC user, PREMIUM user with default rate limits.

## END POINTS
1) Register the user with preference - http://localhost:8080/rateLimiter/userBucketRegistration?userId=<<Replace with user id/ip>>&requestLimit=<<Replace with api request limit>>&requestTimeMinutes=<<Replace with time>>
2) Request Access - http://localhost:8080/rateLimiter/grant-access?userId=<<Registered User ID/IP>>
