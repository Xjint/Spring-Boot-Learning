package com.bjt.xint.controller;


import com.bjt.xint.dto.QuestionDTO;
import com.bjt.xint.mapper.QuestionMapper;
import com.bjt.xint.model.Question;
import com.bjt.xint.model.User;
import com.bjt.xint.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {


    @Autowired
    private QuestionService questionService;


    @GetMapping("/publish/{id}")  //这个id是question的id
    public String edit(@PathVariable(name = "id") Integer id,
                       Model model) {
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        return "/publish";
    }

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "id", required = false) Integer id,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);

        if (title == null || title.equals("")) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description.equals("")) {
            model.addAttribute("error", "问题描述不能为空");
            return "publish";
        }
        if (tag == null || tag.equals("")) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        //提交问题的时候，还要知道是哪一个用户提交的，这个user就是用来保存这个用户的信息的

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录！");
            //用户没有登陆 就返回提交问题的页面
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());   //将用户的id保存在creator中
        question.setId(id);
        //添加了编辑问题功能之后，这里得重写逻辑，如果是更新问题的话，就不需要插入新问题了
        questionService.createOrUpdate(question);
        //成功提交问题，就再次返回主页即跟目录
        return "redirect:/";
    }
}
