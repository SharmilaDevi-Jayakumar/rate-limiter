package com.paperflite.ratelimiter.Models;

import lombok.Setter;

@Setter
public class AccessInfoDTO {

    public boolean isAccessAvailable;
    public String message;
}
