package com.sykun.baizhimall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * security登录成功的处理类
 *
 * @author SYK
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        // 响应json数据
        res.setCharacterEncoding("utf-8");
        res.setContentType("application/json;charset=utf-8;");
        PrintWriter writer = res.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 10000);
        resultMap.put("msg", "success");
        resultMap.put("data", "登录成功！");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultMap);
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
