package com.nordeus.jobfair.auctionservice.auctionservice.api;


import com.nordeus.jobfair.auctionservice.auctionservice.domain.dtos.UserDto;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
public class UsersController {

    UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User register(@RequestBody UserDto userDto){
        return userService.register(userDto.getUsername(),userDto.getPassword());
    }

    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User login(@RequestBody UserDto userDto){
        return userService.login(userDto.getUsername(),userDto.getPassword());
    }

}
