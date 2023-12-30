package goorm.board.api;

import goorm.board.model.dto.request.PostRequest;
import goorm.board.model.dto.response.PostResponse;
import goorm.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostService postService;

    @GetMapping("/post")
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getOnePost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @PostMapping("/post")
    public ResponseEntity<PostResponse> addComment(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.addPost(postRequest));
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<PostResponse> editComment(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.editPost(postId, postRequest));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
