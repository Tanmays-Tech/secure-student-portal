package com.example.security;
import jakarta.servlet.annotation.WebServlet;import jakarta.servlet.http.*;import jakarta.servlet.*;import java.io.*;import java.sql.*;import org.mindrot.jbcrypt.BCrypt;
@WebServlet(urlPatterns={"/login","/register","/logout"})
public class AuthServlet extends HttpServlet {
 protected void doGet(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
  String p=req.getServletPath();if(p.equals("/logout")){HttpSession s=req.getSession(false);if(s!=null)s.invalidate();res.sendRedirect(req.getContextPath()+"/login");return;}
  req.setAttribute("csrf",Csrf.token(req.getSession()));req.getRequestDispatcher(p.equals("/register")?"/register.jsp":"/login.jsp").forward(req,res);
 }
 protected void doPost(HttpServletRequest req,HttpServletResponse res)throws IOException,ServletException{
  String path=req.getServletPath();if(path.equals("/logout")){if(!Csrf.valid(req.getSession(false),req.getParameter("csrf"))){res.sendError(403,"Invalid CSRF token");return;}HttpSession session=req.getSession(false);if(session!=null)session.invalidate();res.sendRedirect(req.getContextPath()+"/login");return;}if(!Csrf.valid(req.getSession(false),req.getParameter("csrf"))){res.sendError(403,"Invalid CSRF token");return;}
  String username=req.getParameter("username"),password=req.getParameter("password");
  if(username==null||password==null||!username.matches("[A-Za-z0-9_]{3,40}")||password.length()<10||password.length()>72){
   req.setAttribute("error","Username: 3-40 letters, digits or underscore. Password: 10-72 characters.");req.setAttribute("csrf",Csrf.token(req.getSession()));req.getRequestDispatcher(path.equals("/register")?"/register.jsp":"/login.jsp").forward(req,res);return;
  }
  try(Connection c=Database.connect()){
   if(path.equals("/register")){
    try(PreparedStatement ps=c.prepareStatement("INSERT INTO users(username,password_hash) VALUES(?,?)")){ps.setString(1,username);ps.setString(2,BCrypt.hashpw(password,BCrypt.gensalt(12)));ps.executeUpdate();}
    res.sendRedirect(req.getContextPath()+"/login?registered=1");
   }else{
    try(PreparedStatement ps=c.prepareStatement("SELECT password_hash FROM users WHERE username=?")){ps.setString(1,username);try(ResultSet rs=ps.executeQuery()){
     if(rs.next()&&BCrypt.checkpw(password,rs.getString(1))){HttpSession old=req.getSession(false);if(old!=null)old.invalidate();HttpSession session=req.getSession(true);session.setAttribute("user",username);session.setMaxInactiveInterval(15*60);res.sendRedirect(req.getContextPath()+"/dashboard");}
     else{req.setAttribute("error","Invalid username or password.");req.setAttribute("csrf",Csrf.token(req.getSession()));req.getRequestDispatcher("/login.jsp").forward(req,res);}
    }}
   }
  }catch(SQLException e){req.setAttribute("error",path.equals("/register")?"Username may already exist.":"Unable to sign in.");req.setAttribute("csrf",Csrf.token(req.getSession()));req.getRequestDispatcher(path.equals("/register")?"/register.jsp":"/login.jsp").forward(req,res);}
 }
}