package ru.kradin.social_media.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ru.kradin.social_media.exceptions.CannotUnsubscribeFromFriendException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Followership;
import ru.kradin.social_media.responses.ChatPageResponse;
import ru.kradin.social_media.services.interfaces.FollowershipService;

@RestController
@RequestMapping("/api/v1/followerships")
@Api(value = "Followership API", tags = {"Followerships"})
public class FollowershipController {

    @Autowired
    FollowershipService followershipService;

    @GetMapping
    @ApiOperation(value = "Return page of user friendships")
    @ApiResponses(value = @ApiResponse(code = 200, message = "Followers retrieved successfully", response = ChatPageResponse.class))
    public ResponseEntity<?> getFriendship(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                           @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Followership> followershipPage = followershipService.getAll(pageable);
        return ResponseEntity.ok(followershipPage);                                                                  
    }

    @PostMapping("/users/{userId}")
    @ApiOperation(value = "Toggle user subscription")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Subscription status toggled successfully"),
        @ApiResponse(code = 400, message = "Invalid action type", response = String.class),
        @ApiResponse(code = 404, message = "User not found", response = String.class),
        @ApiResponse(code = 409, message = "Cannot unsubscribe from friend", response = String.class)
    })
    public ResponseEntity<?> toggleSubscription(
            @PathVariable("userId") @ApiParam(value = "User ID", required = true) long userId,
            @RequestParam(name = "action", required = true) @ApiParam(value = "Action type", allowableValues = "subscribe, unsubscribe", required = true) String action) {
        try {
            if (action.equals("subscribe")) {
                followershipService.subscribe(userId);
            } else if (action.equals("unsubscribe")) {
                followershipService.unsubscribe(userId);
            } else {
                return ResponseEntity.badRequest().body("Invalid action type");
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (CannotUnsubscribeFromFriendException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot unsubscribe from friend");
        }

        return ResponseEntity.ok().build();
    }
}
