package goorm.board.service;


import goorm.board.model.dto.request.PostRequest;
import goorm.board.model.dto.response.CommentResponse;
import goorm.board.model.dto.response.PostDto;
import goorm.board.model.dto.response.PostListResponse;
import goorm.board.model.dto.response.PostResponse;
import goorm.board.model.entity.Comment;
import goorm.board.model.entity.Post;
import goorm.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostListResponse getAllPosts() {
        // 본문 내용 불포함
        // 페이징 기능
        // 최신 글이 운선순위가 높음
        Pageable pageable = PageRequest.of(0, 10, Sort.by("postId").descending());
        Page<Post> postList = postRepository.findAllByOrderByPostIdDesc(pageable);

        List<PostDto> posts = postList.stream()
                .map(post -> new PostDto(post.getPostId(), post.getPostTitle()))
                .toList();

        return new PostListResponse(posts);
    }

    public PostResponse getPostById(Long postId) {
        // 제목, 본문, 댓글들 모두 응답에 포함
        // 삭제된 댓글은 포함하지 않음
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found [Post Id : " + postId + "]"));

        if (!findPost.getStatus()) {
            throw new IllegalStateException("This post has been deleted [Post Id : " + postId + "]");
        }

        List<CommentResponse> comments = findPost.getComments().stream()
                .filter(Comment::getStatus)
                .map(comment -> new CommentResponse(comment.getCommentContent()))
                .toList();

        return new PostResponse(findPost.getPostTitle(), findPost.getPostContent(), comments);
    }

    public PostResponse addPost(PostRequest postRequest) {

        Post newPost = new Post(postRequest.getTitle(), postRequest.getContent());
        postRepository.save(newPost);

        return new PostResponse(newPost.getPostTitle(), newPost.getPostContent(), new ArrayList<>());
    }

    public PostResponse editPost(Long postId, PostRequest postRequest) {
        // 삭제 된 게시글은 불가
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found [Post Id : " + postId + "]"));

        if (!findPost.getStatus()) {
            throw new IllegalStateException("This post has been deleted [Post Id : " + postId + "]");
        }

        Post editedPost = findPost.changePost(postRequest.getTitle(), postRequest.getContent());
        postRepository.save(editedPost);

        List<CommentResponse> comments = findPost.getComments().stream()
                .map(comment -> new CommentResponse(comment.getCommentContent()))
                .toList();

        return new PostResponse(
                editedPost.getPostTitle(),
                editedPost.getPostTitle(),
                comments
                );
    }

    public void deletePost(Long postId) {
        // soft delete 진행
        // post 삭제 시 comments 도 삭제
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found [Post Id : " + postId + "]"));

        if (!findPost.getStatus()) {
            throw new IllegalStateException("This post has already been deleted [Post Id : " + postId + "]");
        }

        findPost.deletePost();
        postRepository.save(findPost);
    }

}
