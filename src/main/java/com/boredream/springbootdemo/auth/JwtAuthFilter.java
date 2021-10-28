package com.boredream.springbootdemo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String CUR_USER_ID = "curUserId";

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // 从http头部读取jwt
        String token = request.getHeader("token");
        if (jwtUtil.validateToken(token)) {
            // 如果token有效，设置auth信息
            String userId = jwtUtil.getUserIdFromToken(token);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, null);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 解析的userId直接传递
            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {

                @Override
                public String getParameter(String name) {
                    return super.getParameter(name);
                }

                @Override
                public Map<String, String[]> getParameterMap() {
                    return super.getParameterMap();
                }

                @Override
                public ServletInputStream getInputStream() throws IOException {
                    return super.getInputStream();
                }

                @Override
                public String[] getParameterValues(String name) {
                    if (CUR_USER_ID.equals(name)) {
                        return new String[]{userId};
                    }
                    return super.getParameterValues(name);
                }

                @Override
                public Enumeration<String> getParameterNames() {
                    Set<String> paramNames = new LinkedHashSet<>();
                    paramNames.add(CUR_USER_ID);
                    Enumeration<String> names = super.getParameterNames();
                    while (names.hasMoreElements()) {
                        paramNames.add(names.nextElement());
                    }
                    return Collections.enumeration(paramNames);
                }
            };
            chain.doFilter(requestWrapper, response);
            return;
        }

        // 调用下一个过滤器
        chain.doFilter(request, response);
    }
}