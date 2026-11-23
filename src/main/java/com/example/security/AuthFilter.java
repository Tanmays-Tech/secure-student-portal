package com.example.security;
import jakarta.servlet.*;import jakarta.servlet.http.*;import java.io.IOException;
public class AuthFilter implements Filter {
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
  HttpServletRequest request=(HttpServletRequest)req;HttpServletResponse response=(HttpServletResponse)res;
  String path=request.getRequestURI();
  if(path.endsWith("/login")||path.endsWith("/register")||path.endsWith("/index.jsp")||path.endsWith("/")){
    chain.doFilter(req,res);return;
  }
  if(request.getSession(false)==null||request.getSession(false).getAttribute("user")==null){
    response.sendRedirect(request.getContextPath()+"/login");return;
  }
  chain.doFilter(req,res);
 }
}