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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ru.kradin.social_media.exceptions.NoAccessException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Chat;
import ru.kradin.social_media.responses.ChatPageResponse;
import ru.kradin.social_media.services.interfaces.ChatService;

@RestController
@RequestMapping("/api/v1/shats")
@Api(value = "Chats API", tags = {"Chats"})
public class ChatController {

    @Autowired
    ChatService chatService;

    @GetMapping
    @ApiOperation(value = "Return page of user chats")
    @ApiResponses(value = @ApiResponse(code = 200, message = "Chats retrieved successfully", response = ChatPageResponse.class))
    public ResponseEntity<Page<Chat>> getChats(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                               @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Chat> chatPage = chatService.getAll(pageable);
        return ResponseEntity.ok(chatPage);
    }

    @GetMapping("/users/{userId}")
    @ApiOperation(value = "Return chat with user by userId")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Chat retrieved successfully", response = Chat.class),
        @ApiResponse(code = 403, message = "Access forbidden"),
        @ApiResponse(code = 404, message = "Chat not found", response = String.class)
    })
    public ResponseEntity<?> getChatWithUser(@PathVariable("userId") @ApiParam(value = "User ID", required = true) long userId) {
        try {
            Chat chat = chatService.getWithUser(userId);
            return ResponseEntity.ok(chat);
        } catch (NoAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{chatId}")
    @ApiOperation(value = "Delete chat")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Chat deleted successfully"),
        @ApiResponse(code = 403, message = "Access forbidden"),
        @ApiResponse(code = 404, message = "Chat not found", response = String.class)
    })
    public ResponseEntity<?> deleteById(@PathVariable("chatId") @ApiParam(value = "Chat ID", required = true) long chatId) {
        try {
            chatService.delete(chatId);
            return ResponseEntity.ok().build();
        } catch (NoAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
