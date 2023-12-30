package goorm.board.service;

import goorm.board.model.dto.request.CommentRequest;
import goorm.board.model.dto.response.CommentResponse;
import goorm.board.model.entity.Comment;
import goorm.board.model.entity.Post;
import goorm.board.repository.CommentRepository;
import goorm.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponse addComment(CommentRequest commentRequest, Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found [Post Id : " + postId + "]"));

        Comment newComment = new Comment(commentRequest.getContent(), findPost);
        commentRepository.save(newComment);

        return new CommentResponse(newComment.getCommentContent());
    }

    public CommentResponse editComment(Long commentId, CommentRequest commentRequest) {

        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found [Comment Id : " + commentId + "]"));

        if (!findComment.getStatus()) {
            throw new IllegalStateException("This Comment has been deleted [Comment Id : " + commentId + "]");
        }

        Comment editedComment = findComment.changeComment(commentRequest.getContent());
        commentRepository.save(editedComment);

        return new CommentResponse(
                editedComment.getCommentContent()
        );

    }

    public void deleteComment(Long commentId) {
        // soft delete 진행
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found [Comment Id : " + commentId + "]"));

        if (!findComment.getStatus()) {
            throw new IllegalStateException("This Comment has already been deleted [Comment Id : " + commentId + "]");
        }

        findComment.deleteComment();
        commentRepository.save(findComment);
    }
}
