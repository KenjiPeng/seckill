package io.kenji.seckill.interfaces.config;

import io.kenji.seckill.interfaces.interceptor.AuthInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-03
 **/
@Component
public class SecurityAdaptor implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;

    public SecurityAdaptor(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }


    /**
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }
}
