package com.wang.wwj.gateway;


public class TokenCommen {
    
    public static final String TOKEN_HEADER = "Bearer ";
    
    public static final String CLAIM_KEY_USERNAME = "sub";
    
    public static final String CLAIM_KEY_CREATED = "created";
    
    public static final String SECERT = "mySecret";
    
    public static final String AUTHORIZATION = "Authorization";
    
    public static final Long EXPIRATION = 1000*60*30L;
}
