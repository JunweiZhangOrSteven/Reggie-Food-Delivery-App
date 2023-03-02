package com.example.reggie.controller;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie.common.R;
import com.example.reggie.entity.User;
import com.example.reggie.service.UserService;
import com.example.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * send verification code for sms message
     *
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //get phone number
        String phone = user.getPhone();

        if(!StringUtils.isEmpty(phone)){
            //generate validate code randomly
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //save validate code in session
            session.setAttribute(phone,code);

            return R.success("sms message send successfully");
        }
        return R.success("message sending failed");
    }

    /**
     * mobile user login
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //get phone number
        String phone = map.get("phone").toString();

        //get validate code
        //String code = map.get("code").toString();

        //get validate code msg saved in session;
//        Object codeInSession = session.getAttribute(phone);

        //compared validate code
        if(phone != null){
            //if the comparison succeed, the login succeed
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            if(user == null){
                //Check whether the user is a new user.
                //If the user is a new user, the registration is automatically completed
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("login failed");
    }

    // loginout
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("userPhone");
        return R.success("successfully log out！");
    }

}
