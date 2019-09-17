package com.bjt.xint.dto;


import lombok.Data;

@Data
public class CommentDTO {
    private Long parentId;
    private String comment;
    private Integer type;
    private String content;
}
