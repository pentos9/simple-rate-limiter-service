package com.demo.web.rest;

import com.demo.service.UserService;
import com.demo.service.dto.pagination.SearchReq;
import com.demo.service.dto.UserDTO;
import com.demo.web.rest.util.WebUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("search")
    public List<UserDTO> get(HttpServletRequest request) {
        String clientIP = WebUtils.getClientIpAddr(request);
        SearchReq req = new SearchReq();
        req.setClientIP(clientIP);
        return userService.search(req);
    }
}
