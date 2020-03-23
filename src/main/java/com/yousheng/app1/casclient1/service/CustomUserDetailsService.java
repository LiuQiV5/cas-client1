package com.yousheng.app1.casclient1.service;

import com.yousheng.app1.casclient1.domain.auth.UserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/**
 * @author ：lq
 * @date ：2020/3/6
 * @time：22:02
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail userDetail = new UserDetail(username,username);
        return userDetail;
    }
}
