package com.hirshi001.springbootmicroservices.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.hibernate.validator.internal.metadata.provider.MetaDataProvider;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Slf4j
@Component
public class SecurityAuthHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info(MessageFormat.format("login success: user={0}, roles={1}",
                SecUtil.sec_subject_name(),
                StringUtils.join(SecUtil.sec_get_grants(), ',')));

        // httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/index.html");



    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException _ae) throws IOException, ServletException {
        String _msg = _ae.getMessage();
        log.info(MessageFormat.format("login failed: {0}", _msg));
        httpServletRequest.setAttribute("SPRING_SECURITY_MESSAGE", _msg);
        httpServletRequest.getRequestDispatcher("/login_error").forward(httpServletRequest, httpServletResponse);

    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info(MessageFormat.format("logout: user={0}", authentication == null ? "(null)" : authentication.getPrincipal()));
        httpServletResponse.sendRedirect(httpServletRequest.getServletContext().getContextPath() + "/index.html");
    }
}
