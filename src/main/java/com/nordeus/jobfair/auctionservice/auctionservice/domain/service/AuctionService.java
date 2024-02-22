package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;

import java.util.Collection;
import java.util.List;

public interface AuctionService {

    List<Auction> getAllActive();

    Auction getAuction(AuctionId auctionId);

    public List<String> getUsers();

    void joinAuction(long auctionId, String userId);

    void bid(long auctionId, String userId);

}
