package ru.kradin.social_media.controllers.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.FriendRequest;
import ru.kradin.social_media.models.Friendship;
import ru.kradin.social_media.responses.FriendRequestPageResponse;
import ru.kradin.social_media.responses.FriendshipPageResponse;
import ru.kradin.social_media.services.interfaces.FriendRequestService;
import ru.kradin.social_media.services.interfaces.FriendshipService;

@RestController
@RequestMapping("/api/v1/friendships")
@Api(value = "Frienship API", tags = {"Friendship"})
public class FriendController {

    @Autowired
    FriendshipService friendshipService;

    @Autowired
    FriendRequestService friendRequestService;

    @GetMapping
    @ApiOperation(value = "Return page of user friendships")
    @ApiResponses(value = @ApiResponse(code = 200, message = "Friendships retrieved successfully", response = FriendshipPageResponse.class))
    public ResponseEntity<?> getAllFriendships(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                               @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Friendship> frienshipPage = friendshipService.getAll(pageable);
        return ResponseEntity.ok(frienshipPage);
    }

    @DeleteMapping("/users/{userId}")
    @ApiOperation(value = "Delete frienship")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Friendship deleted successfully"),
        @ApiResponse(code = 404, message = "Friendship not found", response = String.class)
    })
    public ResponseEntity<?> deleteFromFriendships(@PathVariable("userId") @ApiParam(value = "User ID", required = true) long userId) {
        try {
            friendshipService.delete(userId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/requests")
    @ApiOperation(value = "Return page of user friend requests")
    @ApiResponses(value = @ApiResponse(code = 200, message = "Friend requests retrieved successfully", response = FriendRequestPageResponse.class))
    public ResponseEntity<?> getAllRequests(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                            @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FriendRequest> friendRequestPage = friendRequestService.getAll(pageable);
        return ResponseEntity.ok(friendRequestPage);
    }

    @PostMapping("/requests/users/{requesterId}")
    @ApiOperation(value = "Toggle friend request status")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Friend request status toggled successfully"),
        @ApiResponse(code = 400, message = "Invalid action type", response = String.class),
        @ApiResponse(code = 404, message = "Requester not found", response = String.class)
    })
    public ResponseEntity<?> toggleFriendsRequestStatus(
            @PathVariable("requesterId") @ApiParam(value = "Requester ID", required = true) long requesterId,
            @RequestParam(name = "action", required = true) @ApiParam(value = "Action type", allowableValues = "accept, cancel", required = true) String action) {
        try {
            if (action.equals("accept")) {
                friendRequestService.accept(requesterId);
            } else if (action.equals("cancel")) {
                friendRequestService.cancel(requesterId);
            } else {
                return ResponseEntity.badRequest().body("Invalid action type");
            }
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/users/{userId}")
    @ApiOperation(value = "Send friend request")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Friend request sent successfully"),
        @ApiResponse(code = 404, message = "User not found", response = String.class)
    })
    public ResponseEntity<?> sendFriendRequest (
            @PathVariable("userId") @ApiParam(value = "User ID", required = true) long userId) {
        try {
            friendRequestService.sendRequest(userId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/requests/users/{userId}")
    @ApiOperation(value = "Cancel friend request")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Friend request cancelled successfully"),
        @ApiResponse(code = 404, message = "Friend request not found", response = String.class)
    })
    public ResponseEntity<?> cancelFriendRequest (
            @PathVariable("userId") @ApiParam(value = "User ID", required = true) long userId) {
        try {
            friendRequestService.cancelRequest(userId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
