package com.bjt.xint.service;


import com.bjt.xint.dto.PaginationDTO;
import com.bjt.xint.dto.QuestionDTO;
import com.bjt.xint.mapper.QuestionMapper;
import com.bjt.xint.mapper.UserMapper;
import com.bjt.xint.model.Question;
import com.bjt.xint.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {


        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);

        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }

        //page为页数(以1开始)，size为每页显示数量
        //如果用户点击第i页，则应该从索引为size*(i-1)的数据(索引从0开始)开始展示
        Integer offset = size * (page - 1);

        List<Question> questionList = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);

        return paginationDTO;
    }
}
