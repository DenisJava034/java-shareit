package ru.practicum.shareit.comment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByItemId(Long itemId);

    class CommentMapper {
        public static CommentDto toCommentDto(Comment comment) {
            if (comment == null) return null;
            return CommentDto.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .authorName(comment.getAuthor().getName())
                    .created(comment.getCreated())
                    .build();
        }

        public static Collection<CommentDto> toCommentsDtoCollection(Collection<Comment> comments) {
            return comments.stream()
                    .map(CommentMapper::toCommentDto)
                    .toList();
        }
    }
}
