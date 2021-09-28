package com.boredream.springbootdemo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class HttpResponseFilter extends OncePerRequestFilter {

   private static final Logger log = LoggerFactory.getLogger(HttpResponseFilter.class);

   @Override
   protected void doFilterInternal(
           HttpServletRequest request,
           HttpServletResponse response,
           FilterChain chain) throws ServletException, IOException {

      // 解决跨域问题
      response.addHeader("Access-Control-Allow-Origin", "*");
      response.addHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
      response.addHeader("Access-Control-Allow-Headers", "accept,x-requested-with,Content-Type");

      // 调用下一个过滤器
      chain.doFilter(request, response);
   }
}