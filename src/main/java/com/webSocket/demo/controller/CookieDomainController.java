package com.webSocket.demo.controller;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/CookieDomain")
public class CookieDomainController {

    @RequestMapping(value = "/setCookie")
    public boolean setCookie(String data, HttpServletRequest request, HttpServletResponse response){
        if(StringUtils.isEmpty(data)){
            return false;
        }
        if(!data.contains("=")){
            return false;
        }
        String cookieKey = data.substring(0, data.indexOf("="));
        String cookieValue = data.substring(data.indexOf("=") + 1, data.length());
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setDomain(".iqiyi.com");
        cookie.setPath("/");
        response.addCookie(cookie);
        return true;
    }

}
