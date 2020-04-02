package com.yousheng.app1.casclient1.config;

import com.yousheng.app1.casclient1.filter.JwtAuthenticationEntryPoint;
import com.yousheng.app1.casclient1.filter.JwtAuthenticationTokenFilter;
import com.yousheng.app1.casclient1.filter.RestAuthenticationAccessDeniedHandler;
import com.yousheng.app1.casclient1.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import net.unicon.cas.client.configuration.CasClientConfigurationProperties;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：lq
 * @date ：2020/3/6
 * @time：21:53
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
@EnableConfigurationProperties(CasClientConfigurationProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final RestAuthenticationAccessDeniedHandler accessDeniedHandler;

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                // 设置UserDetailsService
                .userDetailsService(this.customUserDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    private final CasClientConfigurationProperties configProps;


    /*************************************   SSO配置-开始   ************************************************/

    /**
     * SingleSignOutHttpSessionListener 添加监听器
     * 用于单点退出，该过滤器用于实现单点登出功能，可选配置
     *
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListenerBean() {
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listenerRegistrationBean = new ServletListenerRegistrationBean<>();
        listenerRegistrationBean.setEnabled(true);
        listenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        listenerRegistrationBean.setOrder(1);
        System.out.println("================================singleListener执行");
        return listenerRegistrationBean;
    }

    /**
     * SingleSignOutFilter 登出过滤器
     * 该过滤器用于实现单点登出功能，可选配置
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilterBean(){
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new SingleSignOutFilter());
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>(16);
        initParameters.put("casServerUrlPrefix", configProps.getServerUrlPrefix());
        filterRegistrationBean.setInitParameters(initParameters);
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setName("singleFilter");
        System.out.println("================================singleFilter执行");
        return filterRegistrationBean;
    }

    /**
     * AuthenticationFilter 认证过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        Map<String, String> initParameters = new HashMap<>(16);

        registration.setFilter(new AuthenticationFilter());
        initParameters.put("casServerLoginUrl", configProps.getServerLoginUrl());
        initParameters.put("serverName", configProps.getClientHostUrl());

        // 表示过滤所有
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }


    /**
     * Cas30ProxyReceivingTicketValidationFilter 验证过滤器
     * 该过滤器负责对Ticket的校验工作，必须启用它
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterValidationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        // 设定匹配的路径
        Map<String, String> initParameters = new HashMap<>(16);
        initParameters.put("casServerUrlPrefix", configProps.getServerUrlPrefix());
        initParameters.put("serverName", configProps.getClientHostUrl());

        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(2);
        return registration;
    }

    /**
     * HttpServletRequestWrapperFilter wraper过滤器
     * 该过滤器负责实现HttpServletRequest请求的包裹，
     * 比如允许开发者通过HttpServletRequest的getRemoteUser()方法获得SSO登录用户的登录名，可选配置。
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterWrapperRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestWrapperFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        // 设定加载的顺序
        registration.setOrder(2);
        return registration;
    }


    /*************************************   SSO配置-结束   ************************************************/



    /**
     * 装载BCrypt密码编码器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/index1","/api/v1/login", "/api/v1/sign", "/error/**","/favicon.ico").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();

        // 添加JWT filter
        httpSecurity
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/favicon.ico",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.ttf"
                );
        web.ignoring().antMatchers("/v2/api-docs","/index1");

        StrictHttpFirewall firewall = new StrictHttpFirewall();
        //去掉";"黑名单
        firewall.setAllowSemicolon(true);
        //加入自定义的防火墙
        web.httpFirewall(firewall);
        super.configure(web);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() throws Exception {
        return new JwtAuthenticationTokenFilter(authenticationManager());
    }
}
