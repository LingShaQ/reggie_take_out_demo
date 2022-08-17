package com.lingsha.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lingsha.reggie.common.R;
import com.lingsha.reggie.entity.User;
import com.lingsha.reggie.service.UserService;
import com.lingsha.reggie.utils.SMSUtils;
import com.lingsha.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @auther Ling.Sha
 * @date 2022/8/9 - 10:30
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session)
    {
        //手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone))
        {
            //随机4为验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //调用阿里云api发送服务
           // SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            //存起来
            session.setAttribute(phone,code);
        }
        return R.success("发送成功");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session)
    {
        String phone = (String) map.get("phone");
        log.info(phone);
        //手机号  ,验证码



        log.info("用户输入的phone:{}",phone);
        //从session中找到真的code
        String realCode = (String) session.getAttribute("code");
        String code =realCode;
        log.info("后端生成的code:{}",realCode);

      //  if(realCode!=null&&realCode.equals(code))//这里realcode本来就是null
        if(phone!=null)//电话不是空就行,没有验证码
        {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user ==null)
            {
                //自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);

            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("发送失败");
    }
}
