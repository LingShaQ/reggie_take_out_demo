package com.lingsha.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lingsha.reggie.common.R;
import com.lingsha.reggie.entity.Employee;
import com.lingsha.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @auther Ling.Sha
 * @date 2022/8/5 - 9:59
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee)//username password 对应上
    {
        //1、根据页面提交的数据查询密码
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//md5加密处理

        //2、查询数据库 username   username具有唯一约束
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //  3、如果没有查到返回失败
        if(emp==null)
        {
            return R.error("登陆失败");
        }
        //4、密码比对,不一致失败
        if(!emp.getPassword().equals(password))
        {
            return R.error("登陆失败");
        }
        //5、查询员工状态
        if(emp.getStatus()==0)
        {
            //禁用状态
            return R.error("账号被禁用");
        }
        //6、账号正常 ,员工id放到session并且返回登陆成功结果
        HttpSession session = request.getSession();
        session.setAttribute("employee",emp.getId());
        return R.success(emp);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request)
    {
        //清理Session中保存的当前登陆员工的id
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee)
    {
        log.info("新增员工,员工信息{}",employee.toString());//获取成功

        //设置初始化密码123456,进行md5加秘
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
       // employee.setCreateTime(LocalDateTime.now());
       // employee.setUpdateTime(LocalDateTime.now());

     //  Long empId = (Long) request.getSession().getAttribute("employee");
      // employee.setCreateUser(empId);
     //  employee.setUpdateUser(empId);
       employeeService.save(employee);

        return R.success("新增员工成功");
    }


    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page,int pageSize,String name)
    {
        //log.info("page={},pageSize={},name={}",page,pageSize,name);

        //构造    分页构造器
        Page pageinfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageinfo, queryWrapper);
        return R.success(pageinfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping()
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee)
    {

        log.info("employee"+employee.toString());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查看员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id)
    {
        log.info("根据id查看员工信息");
        Employee employee = employeeService.getById(id);

        return R.success(employee);
    }
}
