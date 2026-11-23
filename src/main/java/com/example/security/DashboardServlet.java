package com.example.security;
import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import jakarta.servlet.*;import java.io.IOException;
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
 protected void doGet(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
  req.setAttribute("username",req.getSession().getAttribute("user"));req.setAttribute("csrf",Csrf.token(req.getSession()));req.getRequestDispatcher("/dashboard.jsp").forward(req,res);
 }
}