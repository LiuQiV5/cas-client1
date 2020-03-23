package com.yousheng.app1.casclient1.filter;

import com.yousheng.app1.casclient1.domain.auth.UserDetail;
import com.yousheng.app1.casclient1.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ：lq
 * @date ：2020/3/6
 * @time：21:54
 */
public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("sessionid: "+request.getSession().getId());
        JwtUtils jwtUtils = new JwtUtils();
        String auth_token = request.getHeader("Authorization");
        final String auth_token_start = "Bearer ";
        if (!StringUtils.isEmpty(auth_token) && auth_token.startsWith(auth_token_start)) {
            auth_token = auth_token.substring(auth_token_start.length());
        } else {
            // 不按规范,不允许通过验证
            auth_token = "eyJhbGciOiJIUzI1NiIsInppcCI6IkRFRiJ9.eNoky0sKwjAURuG9_OMEcvMyNzPBiQPdgiQmYpy0tCkopXu34vTjnBXzkhFBhpUnr4wOEFjmOt1aQVQC830YK-KKtPTnMLX-2fPj6XK-YhOo7xGRXLBsPRkt0FL_Q3B8-MGrt30wmTkQPaTxVktL2cpcqpY6FybrjEqJsX0BAAD__w.5vTSKobk90VmiXqKi47xZJBvL_70tdJPteXvCyCCE5U";
        }

        String username = jwtUtils.getUsernameFromToken(auth_token);

        logger.info(String.format("Checking authentication for user %s.", username));

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetail user = jwtUtils.getUserFromToken(auth_token);
            if (jwtUtils.validateToken(auth_token, user)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info(String.format("Authenticated user %s, setting security context", username));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
