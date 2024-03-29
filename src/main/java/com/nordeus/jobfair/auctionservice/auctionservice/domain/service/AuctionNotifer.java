package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;

import java.util.Collection;

public interface AuctionNotifer {

    void auctionFinished(Auction auction);

    void bidPlaced(Bid bid);

    void activeAuctionsRefreshed(Collection<Auction> activeAuctions);

    void auctionEndedNotification(User user, Auction auction);
}
