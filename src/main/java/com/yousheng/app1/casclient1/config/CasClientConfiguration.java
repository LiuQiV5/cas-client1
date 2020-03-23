//package com.yousheng.app1.casclient1.config;
//
//import lombok.AllArgsConstructor;
//import net.unicon.cas.client.configuration.CasClientConfigurationProperties;
//import org.jasig.cas.client.authentication.AuthenticationFilter;
//import org.jasig.cas.client.session.SingleSignOutFilter;
//import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author ：lq
// * @date ：2020/3/3
// * @time：15:40
// */
//@Configuration
//@EnableConfigurationProperties(CasClientConfigurationProperties.class)
//@AllArgsConstructor
//public class CasClientConfiguration {
//
//    private final CasClientConfigurationProperties configProps;
//
//    @Bean
//    public FilterRegistrationBean singleSignOutFilterBean(){
//        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(new SingleSignOutFilter());
//        filterRegistrationBean.setEnabled(true);
//        filterRegistrationBean.addUrlPatterns("/*");
//        Map<String,String> initParameters = new HashMap<>(16);
//        initParameters.put("casServerUrlPrefix", configProps.getServerUrlPrefix());
//        filterRegistrationBean.setInitParameters(initParameters);
//        filterRegistrationBean.setOrder(1);
//        filterRegistrationBean.setName("singleFilter");
//        System.out.println("================================singleFilter执行");
//        return filterRegistrationBean;
//    }
//
//
//    @Bean
//    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListenerBean() {
//        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> listenerRegistrationBean = new ServletListenerRegistrationBean<>();
//        listenerRegistrationBean.setEnabled(true);
//        listenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
//        listenerRegistrationBean.setOrder(1);
//        System.out.println("================================singleListener执行");
//        return listenerRegistrationBean;
//    }
//
//}
