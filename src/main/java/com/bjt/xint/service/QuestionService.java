package com.bjt.xint.service;


import com.bjt.xint.dto.PaginationDTO;
import com.bjt.xint.dto.QuestionDTO;
import com.bjt.xint.exception.CustomizeErrorCode;
import com.bjt.xint.exception.CustomizeException;
import com.bjt.xint.mapper.QuestionExtMapper;
import com.bjt.xint.mapper.QuestionMapper;
import com.bjt.xint.mapper.UserMapper;
import com.bjt.xint.model.Question;
import com.bjt.xint.model.QuestionExample;
import com.bjt.xint.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size) {


        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());//总记录数
        Integer totalPage; //总页数
        if (totalCount != 0) {
            if (totalCount % size == 0) {
                totalPage = totalCount / size;
            } else {
                totalPage = totalCount / size + 1;
            }

            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }
        } else {
            totalPage = 1;
            page = 1;
        }
        paginationDTO.setPagination(totalPage, page);

        //page为页数(以1开始)，size为每页显示数量
        //如果用户点击第i页，则应该从索引为size*(i-1)的数据(索引从0开始)开始展示
        Integer offset = size * (page - 1);

        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        if (questionList != null) {
            for (Question question : questionList) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOS.add(questionDTO);
            }
        }
        paginationDTO.setQuestions(questionDTOS);

        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();

        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(example);
        Integer totalPage;
        if (totalCount != 0) {
            if (totalCount % size == 0) {
                totalPage = totalCount / size;
            } else {
                totalPage = totalCount / size + 1;
            }

            if (page < 1) {
                page = 1;
            }
            if (page > totalPage) {
                page = totalPage;
            }
        } else {
            totalPage = 1;
            page = 1;
        }

        paginationDTO.setPagination(totalPage, page);
        //page为页数(以1开始)，size为每页显示数量
        //如果用户点击第i页，则应该从索引为size*(i-1)的数据(索引从0开始)开始展示
        Integer offset = size * (page - 1);


        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(example1, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        if (questionList != null) {
            for (Question question : questionList) {
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                questionDTO.setUser(user);
                questionDTOS.add(questionDTO);
            }
        }
        paginationDTO.setQuestions(questionDTOS);

        return paginationDTO;
    }


    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        //如果question的id不存在，就新建问题，如果存在，就更新问题
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            Question record = new Question();
            record.setGmtModified(System.currentTimeMillis());
            record.setTitle(question.getTitle());
            record.setDescription(question.getDescription());
            record.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(record, example);
            //不为1 说明没有更新
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question record = new Question();
        record.setId(id);
        record.setViewCount(1);
        questionExtMapper.incView(record);
    }
}
