package com.yousheng.app1.casclient1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author ：lq
 * @date ：2020/3/3
 * @time：15:57
 */
@Controller
public class CasLogout1 {

    @GetMapping("/logout1")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:https://www.uni.sso.com:8443/cas/logout?service=http://192.168.1.104:8088/logout/success1";
    }
}
