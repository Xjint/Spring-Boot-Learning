package com.bjt.xint.service;


import com.bjt.xint.dto.CommentDTO;
import com.bjt.xint.enums.CommentTypeEnum;
import com.bjt.xint.exception.CustomizeErrorCode;
import com.bjt.xint.exception.CustomizeException;
import com.bjt.xint.mapper.CommentMapper;
import com.bjt.xint.mapper.QuestionExtMapper;
import com.bjt.xint.mapper.QuestionMapper;
import com.bjt.xint.mapper.UserMapper;
import com.bjt.xint.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;

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

    public List<CommentDTO> listByQuestionId(Long id) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample(example);
        if (comments.size() == 0)
            return new ArrayList<>();
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentatorId()).collect(Collectors.toSet());
        List<Long> users = new ArrayList<>();
        users.addAll(commentators);

        UserExample example1 = new UserExample();
        example1.createCriteria().andIdIn(users);
        List<User> userList = userMapper.selectByExample(example1);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, user -> user));
        List<CommentDTO> commentDTOs = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentatorId()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOs;
    }
}
