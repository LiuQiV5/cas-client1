package com.yousheng.app1.casclient1.service;

import com.yousheng.app1.casclient1.domain.ResultCode;
import com.yousheng.app1.casclient1.domain.auth.ResponseUserToken;
import com.yousheng.app1.casclient1.domain.auth.Role;
import com.yousheng.app1.casclient1.domain.auth.UserDetail;
import com.yousheng.app1.casclient1.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author ：lq
 * @date ：2020/3/6
 * @time：22:35
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public ResponseUserToken login(String username, String password) {
        //用户验证
        final Authentication authentication = authenticate(username, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final UserDetail userDetail = (UserDetail) authentication.getPrincipal();
//        UserDetail userDetail = new UserDetail("13906160328","13906160328", Role.builder().name("ADMIN").build());
        final String token = new JwtUtils().generateAccessToken(userDetail);
        //存储token
        return new ResponseUserToken(token, userDetail);

    }

    private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            log.info("错误码:{},错误信息:{}",ResultCode.LOGIN_ERROR,e.getMessage());
        }
        return null;
    }
}
