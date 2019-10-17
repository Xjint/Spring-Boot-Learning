package com.bjt.xint.mapper;

import com.bjt.xint.model.Question;

public interface QuestionExtMapper {
    int incView(Question record);

    int incCommentCount(Question record);
}