package com.bjt.xint.controller;


import com.bjt.xint.dto.AccessTokenDTO;
import com.bjt.xint.dto.GitHubUser;
import com.bjt.xint.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    GitHubProvider gitHubProvider;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id("3e9a0e7fcb670458c29a");
        accessTokenDTO.setClient_secret("f47bd900d4010298da56e60e4ab56ab9b39c7eb3");
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setState(state);
        String token = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser user = gitHubProvider.getUser(token);
        System.out.println(user.getId());
        return "index";
    }
}
