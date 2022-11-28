package com.hwkedu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwkedu.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 侯文柯
 * @version 1.0
 */
//可以写在类里也可以在ReggieApplication里指定扫描mapper包
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
