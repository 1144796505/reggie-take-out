package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.entity.Employee;
import com.hwkedu.reggie.mapper.EmployeeMapper;
import com.hwkedu.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author 侯文柯
 * @version 1.0
 */
@Service//交给spring容器进行管理
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
