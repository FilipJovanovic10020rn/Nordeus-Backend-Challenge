package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

public interface TokenService {

    // generates a token using the username or userId
    String generateToken(String userName);

    // returns the username decripted from the token
    String validateToken(String token);

}
