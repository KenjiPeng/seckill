package io.kenji.seckill.interfaces.interceptor;

import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.constants.SeckillConstants;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.infrastructure.shiro.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-03
 **/
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String USER_ID = "userId";

    /**
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userIdObject = request.getAttribute(USER_ID);
        if (userIdObject != null) return true;
        String token = request.getHeader(SeckillConstants.TOKEN_HEADER_NAME);
        if (StringUtils.isBlank(token)) throw new SeckillException(HttpCode.USER_NOT_LOGIN);
        Long userId = JwtUtils.getUserId(token);
        if (userId == null) {
            throw new SeckillException(HttpCode.USER_NOT_LOGIN);
        }
        HttpServletRequestWrapper authRequestWrapper = new HttpServletRequestWrapper(request);
        authRequestWrapper.setAttribute(USER_ID, userId);
        return true;
    }
}
