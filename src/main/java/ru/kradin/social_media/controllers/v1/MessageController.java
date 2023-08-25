package ru.kradin.social_media.controllers.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ru.kradin.social_media.DTOs.MessageDTO;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.models.Message;
import ru.kradin.social_media.responses.MessagePageResponse;
import ru.kradin.social_media.services.interfaces.MessageService;

@RestController
@RequestMapping("/api/v1/messages")
@Api(value = "Message API", tags = {"Message"})
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/users/{secondUserId}")
    @ApiOperation(value = "Return page of messages whit user")
    @ApiResponses(value = @ApiResponse(code = 200, message = "Messages retrieved successfully", response = MessagePageResponse.class))
    public ResponseEntity<?> getMessagesWithUser(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                         @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size,
                                         @PathVariable("secondUserId") @ApiParam(value = "ID of second user", required = true) long secondUserId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messagePage = messageService.getBySecondUserId(secondUserId, pageable);
        return ResponseEntity.ok(messagePage);
    }

    @PostMapping
    @ApiOperation(value = "Write message")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Message written successfully"),
        @ApiResponse(code = 400, message = "Validation error", response = BindingResult.class),
        @ApiResponse(code = 404, message = "Chat not found", response = String.class)
    })
    public ResponseEntity<?> writeMessageToUser(@RequestBody @Valid MessageDTO messageDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult);

        try {
            messageService.write(messageDTO.getText(), messageDTO.getRecipientId());
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
