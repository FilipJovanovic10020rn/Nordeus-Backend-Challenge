package com.nordeus.jobfair.auctionservice.auctionservice.domain.service.impl;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionNotifer;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.sockets.MyWebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionNotifer auctionNotifer;

    public static List<Auction> auctions = new ArrayList<>();
    public static List<String> usersCurrentlyConnected = new ArrayList<>();



    @Scheduled(fixedRate = 600000)
    public void addNewAuctions() {
        for(int i =0; i<10;i++){
            // todo promeniti
            auctions.add(
                    new Auction(new AuctionId(System.currentTimeMillis()+i),
                        "Messi"+i,
                        null,
                        new Bid(1),
                        LocalDateTime.now().plusMinutes(1),
                        new ArrayList<>(),
                        false
                    ));
        }
        try {
            sendNewAuctionsNotification();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Scheduled(fixedRate = 5000)
    public void checkAuctionTimers() {
        for(Auction auction: auctions){
            if(auction.getExperationTime().isBefore(LocalDateTime.now()) && !auction.isExpired()){
                auction.setExpired(true);
                for(String userId : auction.getUsersInAuction()){
                    try {
                        sendFinishedAuctionNotification(auction,userId);
                    }catch (Exception e){

                    }
                }
            }
        }
    }

    @Override
    public List<Auction> getAllActive() {
        return auctions;
    }

    public List<String> getUsers(){
        return usersCurrentlyConnected;
    }

    @Override
    public Auction getAuction(AuctionId auctionId) {
        for(Auction auction: auctions){
            if(auction.getAuctionId().equals(auctionId)){
                return auction;
            }
        }
        throw new RuntimeException("No such auction");
    }

    @Override
    public void joinAuction(long auctionId, String userId) {
        for(Auction auction : auctions) {
            if(auction.getAuctionId().getValue()==auctionId){
                if(auction.getUsersInAuction().contains(userId)){
                    throw new RuntimeException("User already in auction");
                }
                auction.getUsersInAuction().add(userId);
                return;
            }
        }
        throw new RuntimeException("No such auction");
    }

    @Override
    public void bid(long auctionId, String userId) {

        if(!usersCurrentlyConnected.contains(userId)){
            throw new RuntimeException("User doesnt exist");
        }

        for(Auction auction: auctions){
            if(auction.getAuctionId().getValue() == auctionId
                    && !auction.isExpired() && auction.getUsersInAuction().contains(userId)){

                if(auction.getExperationTime().isBefore(LocalDateTime.now().plusSeconds(5))){
                    auction.setExperationTime(LocalDateTime.now().plusSeconds(5));
                }
                auction.setWinningUser(userId);
                auctionNotifer.bidPlaced(new Bid(auction.getCurrentBid().getValue()+1));
                auction.setCurrentBid(new Bid(auction.getCurrentBid().getValue()+1));

                // notify all players // user - id
                for(String user : auction.getUsersInAuction()) {
                    if(user.equals(userId))
                        continue;
                    try {
                        sendBidNotification(auction.getPlayerName(),user, userId, auction.getCurrentBid().getValue());
                    }
                    catch (Exception e){
                        System.out.println("greska sa sendNotify");
                    }
                }
            }
        }
    }

    private void sendBidNotification(String player, String userId, String biddingUser, int value) throws Exception{
        for(UserSession session : MyWebSocketHandler.sessionList){
            if(session.getUserId().equals(userId)){
                session.getSession().sendMessage(new TextMessage(biddingUser+ " je bidovao i trenutno vodi sa "+value + "na aukciji za igraca " + player));
            }
        }
    }
    private void sendFinishedAuctionNotification(Auction auction,String userId) throws Exception{
        for(UserSession session : MyWebSocketHandler.sessionList){
            if(session.getUserId().equals(userId)){
                if(userId.equals(auction.getWinningUser()))
                    session.getSession().sendMessage(new TextMessage("Pobedili ste aukciju za igrica"+ auction.getPlayerName()));
                else{
                    session.getSession().sendMessage(new TextMessage("Korisnik " + auction.getWinningUser() + " je pobedio aukciju za igraca "+ auction.getPlayerName()));
                }
            }
        }
    }

    private void sendNewAuctionsNotification() throws Exception{
        for(UserSession session : MyWebSocketHandler.sessionList){
            session.getSession().sendMessage(new TextMessage("There are new auctions added"));
        }
    }

}
