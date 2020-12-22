package edu.ncsu.csc.iTrust2.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.filter.GenericFilterBean;

public class IPFilter extends GenericFilterBean {

    @Override
    public void doFilter ( final ServletRequest request, final ServletResponse response, final FilterChain chain )
            throws IOException, ServletException {
        chain.doFilter( request, response );

    }

}
