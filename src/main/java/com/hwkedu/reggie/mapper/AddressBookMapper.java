package com.hwkedu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hwkedu.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: AddressBookMapper
 * Package: com.hwkedu.reggie.mapper
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/1 - 20:39
 * @Version: v1.0
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
