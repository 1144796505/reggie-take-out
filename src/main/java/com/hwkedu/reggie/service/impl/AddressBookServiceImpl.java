package com.hwkedu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwkedu.reggie.entity.AddressBook;
import com.hwkedu.reggie.mapper.AddressBookMapper;
import com.hwkedu.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * ClassName: AddressBookServiceImpl
 * Package: com.hwkedu.reggie.service.impl
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/1 - 20:40
 * @Version: v1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
