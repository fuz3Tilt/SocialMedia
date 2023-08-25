package ru.kradin.social_media.controllers.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import ru.kradin.social_media.exceptions.NoAccessException;
import ru.kradin.social_media.exceptions.NotFoundException;
import ru.kradin.social_media.DTOs.PostDTO;
import ru.kradin.social_media.models.Post;
import ru.kradin.social_media.responses.PostPageResponse;
import ru.kradin.social_media.services.interfaces.PostService;

@RestController
@RequestMapping("/api/v1/posts")
@Api(value = "Post API", tags = {"Post"})
public class PostController {
    
    @Autowired
    PostService postService;

    @GetMapping
    @ApiOperation(value = "Get all posts")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Posts retrieved successfully", response =  PostPageResponse.class)
    })
    public ResponseEntity<?> getAll(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                    @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postService.getAll(pageable);
        return ResponseEntity.ok(postPage);
    }

    @GetMapping("/users/{userId}")
    @ApiOperation(value = "Get post by owner")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Posts retrieved successfully", response =  PostPageResponse.class)
    })
    public ResponseEntity<?> getByOwner(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                        @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size,
                                        @PathVariable("userId") @ApiParam(value = "User ID", required = true) long userId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postService.getByOwner(userId, pageable);
        return ResponseEntity.ok(postPage);
    }

    @GetMapping("/feed")
    @ApiOperation(value = "Get feed")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Posts retrieved successfully", response =  PostPageResponse.class)
    })
    public ResponseEntity<?> getFeed(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                        @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postService.getFeed(pageable);
        return ResponseEntity.ok(postPage);
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "Get post by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Post retrieved successfully"),
        @ApiResponse(code = 404, message = "Post not found")
    })
    public ResponseEntity<?> getById(@RequestParam(name = "p", defaultValue = "0") @ApiParam(value = "Page number", defaultValue = "0", required = false) int page,
                                     @RequestParam(name = "s", defaultValue = "10") @ApiParam(value = "Page size", defaultValue = "10", required = false) int size,
                                     @PathVariable("postId") @ApiParam(value = "Post ID", required = true) long postId) {
        try {
            Post post = postService.getById(postId);
            return ResponseEntity.ok(post);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation(value = "Create post")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Post created successfully"),
        @ApiResponse(code = 400, message = "Validation error", response = BindingResult.class)
    })
    public ResponseEntity<?> create(@RequestBody @Valid PostDTO postDTO,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult);

        postService.create(postDTO.getTitle(), postDTO.getText());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PatchMapping("/{postId}")
    @ApiOperation(value = "Update post")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Post updated successfully"),
        @ApiResponse(code = 400, message = "Validation error", response = BindingResult.class),
        @ApiResponse(code = 404, message = "Post not found", response = String.class)
    })
    public ResponseEntity<?> update(@RequestBody @Valid PostDTO postDTO,
                                    BindingResult bindingResult,
                                    @PathVariable("postId") @ApiParam(value = "Post ID", required = true) long postId) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(bindingResult);

        try {
            postService.update(postId, postDTO.getTitle(), postDTO.getText());
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NoAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{postId}")
    @ApiOperation(value = "Delete post")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Post deleted successfully"),
        @ApiResponse(code = 403, message = "Access forbidden"),
        @ApiResponse(code = 404, message = "Post not found", response = String.class)
    })
    public ResponseEntity<?> delete(@PathVariable("postId") @ApiParam(value = "Post ID", required = true) long postId) {
        try {
            postService.delete(postId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NoAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
