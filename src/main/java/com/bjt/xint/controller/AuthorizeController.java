package com.bjt.xint.controller;


import com.bjt.xint.dto.AccessTokenDTO;
import com.bjt.xint.dto.GitHubUser;
import com.bjt.xint.model.User;
import com.bjt.xint.provider.GitHubProvider;
import com.bjt.xint.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    GitHubProvider gitHubProvider;


    @Autowired
    UserService userService;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUrl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        accessTokenDTO.setState(state);
        String token = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getUser(token);
        if (gitHubUser != null && gitHubUser.getId() != null) {
            //user不为空，登陆成功
            User user = new User();
            String token1 = UUID.randomUUID().toString();
            user.setToken(token1);
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setName(gitHubUser.getName());
            user.setAvatarUrl(gitHubUser.getAvatar_url());
            //如果当前用户已存在，则更新，若不存在，就插入新用户数据
            userService.createOrUpdate(user);
            //将token作为cookie
            response.addCookie(new Cookie("token", token1));
            return "redirect:/";
        } else {
            // 登陆失败
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        //退出登陆，需要清除Session和Cookie
        //清除Cookie的方法：新建一个值为null的Cookie,设置生存时间为0（即立即删除），然后用这个Cookie替换之前的Cookie
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";

    }
}
