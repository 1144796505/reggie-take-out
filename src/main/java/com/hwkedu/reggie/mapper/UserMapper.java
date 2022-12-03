package com.hwkedu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwkedu.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: UserMapper
 * Package: com.hwkedu.reggie.mapper
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/1 - 16:36
 * @Version: v1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
