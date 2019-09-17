package com.bjt.xint.service;


import com.bjt.xint.enums.CommentTypeEnum;
import com.bjt.xint.exception.CustomizeErrorCode;
import com.bjt.xint.exception.CustomizeException;
import com.bjt.xint.mapper.CommentMapper;
import com.bjt.xint.mapper.QuestionExtMapper;
import com.bjt.xint.mapper.QuestionMapper;
import com.bjt.xint.model.Comment;
import com.bjt.xint.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public void insert(Comment comment) {
        //parentId是针对哪一个问题进行回复，如果为空，说明没有选中问题就进行回复
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //新增一个回复数
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
        }
    }
}
