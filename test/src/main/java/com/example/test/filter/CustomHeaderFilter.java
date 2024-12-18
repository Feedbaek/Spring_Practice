package com.example.test.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var customHeaderRequestWrapper = new CustomHeaderRequestWrapper(request);
        // 임의로 생성한 커스텀 헤더 추가
        customHeaderRequestWrapper.addHeader("user_id", "admin");
        customHeaderRequestWrapper.addHeader("accept-encoding", "gzip");
        customHeaderRequestWrapper.addHeader("content-length", "121");
        customHeaderRequestWrapper.addHeader("host", "192.168.208.202:8029");
        customHeaderRequestWrapper.addHeader("user-agent", "ReactorNetty/1.0.6");
        customHeaderRequestWrapper.addHeader("proobject_simulation_tx", "false");
        customHeaderRequestWrapper.addHeader("content-type", "application/json");
        customHeaderRequestWrapper.addHeader("accept", "*/*");

        filterChain.doFilter(customHeaderRequestWrapper, response);
    }
}
