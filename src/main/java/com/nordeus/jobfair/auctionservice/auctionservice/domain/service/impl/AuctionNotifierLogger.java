package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.impl;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionNotifer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Slf4j
@Service
public class AuctionNotifierLogger implements AuctionNotifer {

    @Override
    public void auctionEndedNotification(User user, Auction auction) {

    }

    @Override
    public void auctionFinished(Auction auction) {
        log.info("Auction finished: {}", auction);
    }

    @Override
    public void bidPlaced(Bid bid) {
        log.info("Bid placed: {}", bid);
    }

    @Override
    public void activeAuctionsRefreshed(Collection<Auction> activeAuctions) {
        log.info("Active auctions are refreshed: {}", activeAuctions);
    }

}
