package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.entity.User;
import com.hwkedu.reggie.mapper.UserMapper;
import com.hwkedu.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * ClassName: UserServiceImpl
 * Package: com.hwkedu.reggie.service.impl
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/1 - 16:37
 * @Version: v1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
