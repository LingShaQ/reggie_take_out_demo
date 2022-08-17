package com.lingsha.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingsha.reggie.entity.User;
import com.lingsha.reggie.mapper.UserMapper;
import com.lingsha.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @auther Ling.Sha
 * @date 2022/8/9 - 10:31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
