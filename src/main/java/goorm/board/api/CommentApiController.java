package goorm.board.api;

import goorm.board.model.dto.request.CommentRequest;
import goorm.board.model.dto.response.CommentResponse;
import goorm.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @PostMapping("/comment/{postId}")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId, @RequestBody CommentRequest commentRequest)
    {
        return ResponseEntity.ok(commentService.addComment(commentRequest, postId));
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponse> editComment(
            @PathVariable Long commentId, @RequestBody CommentRequest commentRequest)
    {
        return ResponseEntity.ok(commentService.editComment(commentId, commentRequest));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

}
