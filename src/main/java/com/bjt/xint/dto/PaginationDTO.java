package com.bjt.xint.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showFirstPage; //“去往第一页”
    private boolean showPrevious; // "去往前一页"
    private boolean showNext; // "去往下一页"
    private boolean showEndPage;  //"去往最后一页"
    private Integer page;   //page是当前所在的页数，pages是当前显示的所有页数
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer totalCount, Integer page, Integer size) {
        if (totalCount % size == 0) {
            totalPage = totalCount % size;
        } else {
            totalPage = totalCount % size + 1;
        }
        //当输入的页数小于1或者大于当前最大页数，就把它置为1或者最大页数
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        this.page = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        //当现在在第一页时，不显示左边的“返回前一页”的按钮
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }
        //当现在在最后一页时，不显示右边的“去往下一页”的按钮
        if (page == totalPage) {
            showNext = false;
        } else {
            showNext = true;
        }
        //若当前显示的页中已经包括了第一页，那就不显示”去往第一页“按钮
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }
        //若当前显示的页中已经包括了最后一页，那就不显示”去往最后一页“按钮
        if (pages.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }
    }
}
