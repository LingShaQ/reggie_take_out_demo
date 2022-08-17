package com.lingsha.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lingsha.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @auther Ling.Sha
 * @date 2022/8/9 - 10:32
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
