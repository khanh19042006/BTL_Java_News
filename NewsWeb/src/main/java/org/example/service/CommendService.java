package org.example.service;

import org.example.dto.CommentDTO;

import java.util.List;

public interface CommendService {
    public boolean createComment(String content, String authorId,
                                 String newsId, String parentId);   // Tạo comment khi có người bấm bình luận
    public List<CommentDTO> getParentComments(String newsId);       // Lấy tất cả bình luận trong bài viết (chỉ bình luận cha)
    public List<CommentDTO> getReplies(String parentId);            // Lấy tất cả bình luận reply bình luận cha
    public boolean hasReplies(String parentId);                     // Kiểm tra xem bình luận này có bình luận reply hay không
    public boolean deleteComment(String commentId, String userId);  // Xóa comment (nếu là admin thì xóa thẳng, user thì chỉ xóa bình luận do user tạo ra)
    public boolean isOwner(String commentId, String userId);        // Kiểm tra xem bình luận này có do user này viết hay không
}
