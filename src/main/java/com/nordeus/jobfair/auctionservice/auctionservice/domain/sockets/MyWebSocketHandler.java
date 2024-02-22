package com.nordeus.jobfair.auctionservice.auctionservice.domain.sockets;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserSession;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.impl.AuctionServiceImpl;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.UserService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyWebSocketHandler implements WebSocketHandler {

    // lista userId-session kako bismo preko userId mogli da saljemo poruku konkretnom session-u
    public static List<UserSession> sessionList = new ArrayList<>();



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String split[] = session.getUri().toString().split("=");
        String userId = split[1];

        for(User userInList : UserServiceImpl.userList){
            if(userInList.getUserId().getValue().equals(userId)){
                log.info("Connection established on sessionId: {} with userId: {}", session.getId(), userId);
                sessionList.add(new UserSession(session,userId));
                AuctionServiceImpl.usersCurrentlyConnected.add(userId);
                return;
            }
        }
        throw new RuntimeException("No such user");
    }

    // todo ovde poslati token kako bismo povezali korisnika sa sesijom
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String tutorial = (String) message.getPayload();
        log.info("Message: {}", tutorial);

        for (UserSession s: sessionList) {
            if(!s.getSession().equals(session)){
                s.getSession().sendMessage(new TextMessage("Poslao ti je msg: "  ));
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Exception occured: {} on session: {}", exception.getMessage(), session.getId());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.info("Connection closed on session: {} with status: {}", session.getId(), closeStatus.getCode());
        WebSocketSession sessionToRemove = null;
        String userToRemove = null;
        for(UserSession s: sessionList){
            if(s.getSession().equals(session)){
                sessionToRemove = s.getSession();
                userToRemove = s.getUserId();
            }
        }
        if(sessionToRemove!= null && userToRemove != null) {
            sessionList.remove(sessionToRemove);
            AuctionServiceImpl.usersCurrentlyConnected.remove(userToRemove);
        }
        else{
            throw new RuntimeException("Error while removing from list");
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
