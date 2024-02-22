package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Auction {

    private final AuctionId auctionId;
    private String playerName;
    private String winningUser;
    private Bid currentBid;
    private LocalDateTime experationTime;
    private List<String> usersInAuction;
    private boolean expired;
}
