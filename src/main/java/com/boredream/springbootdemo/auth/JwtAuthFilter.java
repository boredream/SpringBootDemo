package com.boredream.springbootdemo.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // 从http头部读取jwt
        String token = request.getHeader("token");
        String username = null, role = null;

        // 从jwt中解出账号与角色信息
        try {
            username = jwtUtil.getUsernameFromToken(token);
            role = jwtUtil.getClaimFromToken(token, "role", String.class);
        } catch (Exception e) {
            log.debug("异常详情", e);
        }

        // 如果jwt正确解出账号信息，说明是合法用户，设置认证信息，认证通过
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, AuthUser.getAuthoritiesByRole(role));

            // 把请求的信息设置到UsernamePasswordAuthenticationToken details对象里面，包括发请求的ip等
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 设置认证信息
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        // 调用下一个过滤器
        chain.doFilter(request, response);
    }
}