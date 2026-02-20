package org.example.service.Impl;

import org.example.dao.CommentDAO;
import org.example.dao.UserDAO;
import org.example.dto.CommentDTO;
import org.example.dto.UserDTO;
import org.example.service.CommendService;

import java.util.List;

public class CommentServiceImpl implements CommendService {
    private final CommentDAO commentDAO = new CommentDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    public boolean createComment(String content, String authorId, String newsId, String parentId){
        return commentDAO.createComment(content, authorId, newsId, parentId);
    }

    @Override
    public List<CommentDTO> getParentComments(String newsId){
        return commentDAO.getParentComments(newsId);
    }

    @Override
    public List<CommentDTO> getReplies(String parentId){
        return commentDAO.getReplies(parentId);
    }

    @Override
    public boolean hasReplies(String parentId){
        return commentDAO.hasReplies(parentId);
    }

    @Override
    public boolean deleteComment(String commentId, String userId){
        UserDTO user = userDAO.getUserById(userId);
        if (user.getRole().equalsIgnoreCase("admin"))
            return commentDAO.deleteCommentByAdmin(commentId);
        return commentDAO.deleteCommentByUser(commentId, userId);
    }

    @Override
    public boolean isOwner(String commentId, String userId){
        return commentDAO.isOwner(commentId, userId);
    }
}
