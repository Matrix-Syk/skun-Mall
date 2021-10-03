package com.sykun.baizhimall.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sykun.baizhimall.entity.AdminEntity;
import com.sykun.baizhimall.entity.AdminRoleEntity;
import com.sykun.baizhimall.entity.ResourceEntity;
import com.sykun.baizhimall.entity.RoleResourceEntity;
import com.sykun.baizhimall.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SYK
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private AdminService adminService;
    @Resource
    private AdminRoleService adminRoleService;
    @Resource
    private RoleResourceService roleResourceService;
    @Resource
    private ResourceService resourceService;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private LoginSuccessHandler loginSuccessHandler;
    @Resource
    private LoginFailureHandler loginFailureHandler;
    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Resource
    private AuthoriAccessDeniedHandler authoriAccessDeniedHandler;
    @Resource
    private OutSuccessHandler outSuccessHandler;
    // 配置账户以及密码
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                // 自定义登录用户名
                .withUser("xiaobai")
                // 自定义密码
                .password("123456")
                // 授权相关 目前忽略
                .roles("admins").and()
                // SpringSecurity 默认使用密码加密匹配  NoOpPasswordEncoder.getInstance() 不使用加密配置
                .passwordEncoder(NoOpPasswordEncoder.getInstance());*/
        auth.userDetailsService(new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                AdminEntity admin = adminService.getOne(new QueryWrapper<AdminEntity>().eq("username", s));
                if (admin == null) {
                    throw new UsernameNotFoundException("用户名或密码错误");
                }
                // 根据查出的用户ID从用户角色中间表对象集合
                List<AdminRoleEntity> adminRoleList = adminRoleService.list(new QueryWrapper<AdminRoleEntity>().eq("admin_id", admin.getId()));
                // 根据用户-角色集合中获取角色id集合
                List<String> roleIds = adminRoleList.stream().map(AdminRoleEntity::getRoleId).collect(Collectors.toList());
                // 根据角色id集合获取获取角色-菜单对象集合并以流处理获取它的resourceid
                List<String> resourceIds = new ArrayList<>();
                for (String roleId : roleIds) {
                    List<String> resources = roleResourceService.list(new QueryWrapper<RoleResourceEntity>().eq("role_id", roleId)).stream().map(RoleResourceEntity::getResourceId).collect(Collectors.toList());
                    resourceIds.addAll(resources);
                }
                // 根据菜单id集合查询对应的菜单的权限标识集合
                List<String> perms = resourceService.listByIds(resourceIds).stream().filter(resource -> StringUtils.isNotEmpty(resource.getPerms())).map(ResourceEntity::getPerms).collect(Collectors.toList());
                User user = new User(admin.getUsername(), admin.getPassword(), AuthorityUtils.createAuthorityList(perms.toArray(new String[perms.size()])));
                return user;
            }
        }).passwordEncoder(bCryptPasswordEncoder);
    }

    // 自定义拦截规则
    // 配置登录页面
    // 配置登录地址
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 自定义拦截规则 验证码请求不需要拦截
        http.authorizeRequests()
                // 设置不需要拦截的请求地址 permitAll 放行
                .antMatchers("/api/code").permitAll()
                // 剩余的任意一个请求都需要 登录认证通过后访问
                .anyRequest().authenticated();
        // 自定义登录页面
        http.formLogin().loginPage("http://localhost:8080");
        // 设置自定义的登录地址 SpringSecurity 默认是 login
        http.formLogin().loginProcessingUrl("/admin-entity/login")
                // 定义成功后的处理类
                .successHandler(loginSuccessHandler)
                // 定义失败后的处理类
                .failureHandler(loginFailureHandler);

        // 配置未认证处理
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                // 设置权限校验失败后的响应信息
                .accessDeniedHandler(authoriAccessDeniedHandler);
        // 关闭csrf保护，否则需要在请求头中携带 CSRF-token 信息
        http.csrf().disable();
        // 登出
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(outSuccessHandler)
                .deleteCookies("JSESSIONID");
    }
}
