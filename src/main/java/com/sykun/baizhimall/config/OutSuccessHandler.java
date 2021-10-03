package com.sykun.baizhimall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

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
public class OutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        // 响应json数据
        res.setCharacterEncoding("utf-8");
        res.setContentType("application/json;charset=utf-8;");
        PrintWriter writer = res.getWriter();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 10000);
        resultMap.put("msg", "success");
        resultMap.put("data", "退出登陆成功！");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultMap);
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
