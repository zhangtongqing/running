package com.peipao.framework.config;


import com.peipao.framework.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Intecepter Adapter
 *
 * @author Meteor.wu
 * @since 2017/9/11 13:32
 */
@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .maxAge(0)
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .allowedMethods("*")
//                .allowedOrigins("*");
//    }

    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/user/pub/register")
//                .excludePathPatterns("/user/pub/forgetPwd")
//                .excludePathPatterns("/user/pub/sendVerCode")
//                .excludePathPatterns("/user/pub/login")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/v2/**");
        //registry.addInterceptor(new UserRoleInteceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}
