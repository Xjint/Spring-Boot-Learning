package com.bjt.xint.controller;


import com.bjt.xint.dto.CommentCreateDTO;
import com.bjt.xint.dto.ResultDTO;
import com.bjt.xint.exception.CustomizeErrorCode;
import com.bjt.xint.model.Comment;
import com.bjt.xint.model.User;
import com.bjt.xint.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {


    @Autowired
    CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        //没有登陆
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //评论为空
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setType(commentCreateDTO.getType());
        comment.setContent(commentCreateDTO.getContent());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentatorId(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }
}
