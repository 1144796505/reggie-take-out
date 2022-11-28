package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.entity.Employee;
import com.hwkedu.reggie.mapper.EmployeeMapper;
import com.hwkedu.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author 侯文柯
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    //自动装配注入service
    @Autowired
    private EmployeeService employeeService;

    /**
     *@RequestBody可以获取请求体，需要在控制器方法设置一个形参，使用@RequestBody进行标识，当
     * 前请求的请求体就会为当前注解所标识的形参赋值
     *request获取session对象如果登录成功,将employeeid保存
     * @return
     */





    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //  1.  将页面提交的密码进行MD5加密处理
        String password = employee.getPassword();
      password =  DigestUtils.md5DigestAsHex(password.getBytes());

        //        2    根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);
        //  3  如果没有查询到则返回登录失败结果
        if (emp == null){
         return    R.error("登录失败");
        }

        // 4   密码比对，如果不一致则返回登录结果
        if (!emp.getPassword().equals(password)){
            return    R.error("登录失败");
        }

        //   5 查看员工状态，如果已为禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            return R.error("账号已经被禁用");
        }

        // 6   登录成功，将员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 退出功能
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //删除session的employee
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息{}",employee.toString());
        //设置初始密码，需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //创建时间
      //  employee.setCreateTime(LocalDateTime.now());
        //修改时间
       // employee.setUpdateTime(LocalDateTime.now());

       // Long emp = (Long) request.getSession().getAttribute("employee");

     //   employee.setCreateUser(emp);
     //   employee.setUpdateUser(emp);

        employeeService.save(employee);
        return R.success("添加员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize ,  String name){
        log.info("page{} pageSize{} name{}",page,pageSize,name);
       //构造分页查询器 第一个参数是当前页,第二个是每页显示条数
        Page<Employee> pageInfo = new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
            queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getUsername,name);
        //添加排序条件
            queryWrapper.orderByDesc(Employee::getUpdateTime);

            //执行查询
        employeeService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改用户状态信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping()
    public R<String> update(HttpServletRequest request ,@RequestBody Employee employee){
        log.info("获取的数据{}",employee);

        //获取当前线程id
        long id = Thread.currentThread().getId();
        log.info("当前线程的id:{}",id);

        //设置修改时间
       // employee.setUpdateTime(LocalDateTime.now());
       // Long empId = (Long) request.getSession().getAttribute("employee");
        //设置修改的用户的id  ,保存在了session域中
       // employee.setUpdateUser(empId);
        //根据id进行修改 ,传进去的参数是entity实体类对象
        employeeService.updateById(employee);
        return R.success("修改用户信息成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        Employee emp = employeeService.getById(id);
      return   R.success(emp);
    }
}
