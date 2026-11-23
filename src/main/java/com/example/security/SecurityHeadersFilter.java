package com.example.security;
import jakarta.servlet.*;import jakarta.servlet.http.*;import java.io.IOException;
public class SecurityHeadersFilter implements Filter {
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
  HttpServletResponse r=(HttpServletResponse)res;
  r.setHeader("X-Content-Type-Options","nosniff");r.setHeader("X-Frame-Options","DENY");
  r.setHeader("Referrer-Policy","strict-origin-when-cross-origin");
  r.setHeader("Content-Security-Policy","default-src 'self'; style-src 'self'; form-action 'self'; frame-ancestors 'none'; base-uri 'self'");
  r.setHeader("Permissions-Policy","camera=(), microphone=(), geolocation=()");
  chain.doFilter(req,res);
 }
}