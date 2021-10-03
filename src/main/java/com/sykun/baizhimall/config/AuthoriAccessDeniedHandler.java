package com.sykun.baizhimall.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
@Component
public class AuthoriAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException e) throws IOException, ServletException {
        // 响应json数据
        res.setCharacterEncoding("utf-8");
        res.setContentType("application/json;charset=utf-8;");
        PrintWriter writer = res.getWriter();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("code",10012);
        resultMap.put("msg","error");
        resultMap.put("data","您没有访问权限，请联系管理员！");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultMap);
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
