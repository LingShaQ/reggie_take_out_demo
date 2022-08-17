package com.lingsha.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lingsha.reggie.entity.Employee;
import com.lingsha.reggie.mapper.EmployeeMapper;
import com.lingsha.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @auther Ling.Sha
 * @date 2022/8/5 - 9:58
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
