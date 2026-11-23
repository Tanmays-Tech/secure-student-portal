package com.example.security;
import jakarta.servlet.http.*; import java.security.SecureRandom; import java.util.Base64;
public final class Csrf {
  private Csrf(){}
  public static String token(HttpSession session){
    String token=(String)session.getAttribute("csrf");
    if(token==null){byte[] bytes=new byte[32];new SecureRandom().nextBytes(bytes);token=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);session.setAttribute("csrf",token);}
    return token;
  }
  public static boolean valid(HttpSession s,String submitted){return s!=null && submitted!=null && submitted.equals(s.getAttribute("csrf"));}
}