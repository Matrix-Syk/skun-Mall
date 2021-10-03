package com.sykun.baizhimall.log;
import java.util.Collection;
import java.util.Date;


import com.sykun.baizhimall.entity.AdminEntity;
import com.sykun.baizhimall.entity.LogEntity;
import com.sykun.baizhimall.service.LogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * @author Sykun
 */
@Component
@Aspect
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger(LogAspect.class);
    @Resource
    private LogService logService;
    @Pointcut("@annotation(com.sykun.baizhimall.log.LogAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) {
        Object proceed = null;
        try {
            // 获取requestAttributes对象，可用于获取request对象
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            // 获取登陆者的IP地址
            String remoteAddr = requestAttributes.getRequest().getRemoteAddr();
            // 常规session认证的，获取session再从session中获取对象
            // AdminEntity user = (AdminEntity) requestAttributes.getAttribute("user", RequestAttributes.SCOPE_SESSION);
            // 从springSecurity中获取对象
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // 得到一个存储了用户名和密码的框架定义的用户对象
            UserDetails user = (UserDetails) authentication.getPrincipal();
            //logger.info("username",user.getUsername());
            // 获取当前执行的方法名子
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            //logger.info("method",method.getName());
            LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
            TypeEnum type = annotation.type();
            String desc = type.getDesc();
            StringBuffer sb = null;
            Object[] args = joinPoint.getArgs();
            if (args!=null&&args.length>0){
                sb = new StringBuffer();
                for (Object arg : args) {
                    sb.append(arg).append(",");
                }
            }

            proceed = joinPoint.proceed();
            //logger.info("操作",desc);
            LogEntity logEntity = new LogEntity();
            logEntity.setLogType(desc);
            logEntity.setLogCreateUser(user.getUsername());
            logEntity.setLogIp(remoteAddr);
            logEntity.setLogCreateTime(new Date());
            logEntity.setLogContent(annotation.content());
            logEntity.setLogParams(sb!=null ? sb.toString().substring(0,sb.length()-1) : "");
            logService.save(logEntity);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return proceed;
    }
}
