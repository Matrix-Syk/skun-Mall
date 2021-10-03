package com.sykun.baizhimall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SYK
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    //    页面重定向
//    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e) throws IOException, ServletException {
        // 响应json数据
        res.setCharacterEncoding("utf-8");
        res.setContentType("application/json;charset=utf-8;");
        PrintWriter writer = res.getWriter();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("code",10011);
        resultMap.put("msg","error");
        resultMap.put("data","当前未认证，请先登录！");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultMap);
        writer.write(json);
        writer.flush();
        writer.close();
        // 流程跳转
//        redirectStrategy.sendRedirect(req,res,"http://localhost:8080");
    }
}
