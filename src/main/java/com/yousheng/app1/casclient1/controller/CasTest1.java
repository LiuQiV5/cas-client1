package com.yousheng.app1.casclient1.controller;

import com.yousheng.app1.casclient1.domain.ResultCode;
import com.yousheng.app1.casclient1.domain.ResultJson;
import com.yousheng.app1.casclient1.domain.auth.ResponseUserToken;
import com.yousheng.app1.casclient1.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author ：lq
 * @date ：2020/2/27
 * @time：21:53
 */
@RestController
@AllArgsConstructor
public class CasTest1 {

    private final AuthService authService;

    @RequestMapping("/index1")
    public ResultJson<ResponseUserToken> test1(HttpServletRequest request){
//        Principal userPrincipal = request.getUserPrincipal();
//        System.out.println(userPrincipal.getName());
//        final ResponseUserToken response = authService.login(userPrincipal.getName(), userPrincipal.getName());
        final ResponseUserToken response = authService.login("13906160328", "13906160328");
        return ResultJson.ok(response);
    }

    @RequestMapping("logout/success1")
    public String logoutsuccess(HttpSession session) {
        return "logoutsuccess1";
    }

    @GetMapping(value = "/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResultJson getUser(HttpServletRequest request){
        return ResultJson.ok("admin 有权访问.....");
    }

    @GetMapping(value = "/test")
    @PreAuthorize("hasAuthority('TEST')")
    public ResultJson getTest(HttpServletRequest request){
        return ResultJson.ok("test 有权访问.....");
    }

    @GetMapping(value = "/manager")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResultJson getManager(HttpServletRequest request){
        return ResultJson.ok("manager 有权访问");
    }
}
