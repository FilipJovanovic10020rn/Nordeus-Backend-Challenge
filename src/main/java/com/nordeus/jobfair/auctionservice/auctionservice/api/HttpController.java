package com.nordeus.jobfair.auctionservice.auctionservice.api;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/auctions")
public class HttpController {

    private AuctionService auctionService;

    @GetMapping("/active")
    public List<Auction> getAllActive() {
        return auctionService.getAllActive();
    }


    // vraca aktivne usere tj povezane na soketima
    @GetMapping("/users")
    public List<String> getAllUsers() {
        return auctionService.getUsers();
    }

    @PatchMapping("/bid/{auctionId}/{userId}")
    public void bid(@PathVariable("auctionId") long auctionId, @PathVariable("userId") String userId) {
        auctionService.bid(auctionId,userId);
    }

    // todo return nesto
    @PatchMapping("join/{auctionId}/{userId}")
    public void joinAuction(@PathVariable("auctionId") long auctionId, @PathVariable("userId") String userId) {
        auctionService.joinAuction(auctionId,userId);
    }

    //todo createAuction

    @PostMapping("createAuction/{userId}")
    public void createAuction(@RequestBody Auction auction, @PathVariable("userId") String userId){

    }

//    @MessageMapping("/hello")
//    @SendTo("/tutorial")
//    public String greeting(HelloMessage message) throws Exception {
//        Thread.sleep(1000); // simulated delay
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }



//    https://www.joshmlwood.com/websockets-with-spring-boot/

}
