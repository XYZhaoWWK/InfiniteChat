package com.shanyangcode.infinitechat.authenticationservice.conf;

import com.alibaba.fastjson.JSON;
import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class SourceHandler implements HandlerInterceptor {

    private final HttpServletResponse httpServletResponse;

    public SourceHandler(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("X-Request-Source");
        if(!"InfiniteChat-GateWay".equals(header)) {
            refuseResult(response);
            return false;
        }
        return true;
    }
//            - AddRequestHeader=X-Request-Source, InfiniteChat-GateWay
    private void refuseResult(HttpServletResponse response) throws Exception {
        httpServletResponse.setContentType("text/html;charset=utf-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        Result<Object> result = new Result<>().setCode(40301).setMsg("非法请求来源");
        httpServletResponse.getWriter().print(JSON.toJSONString(result));
        httpServletResponse.getWriter().flush();
    }

}
