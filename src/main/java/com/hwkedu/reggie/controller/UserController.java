package com.hwkedu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hwkedu.reggie.common.R;
import com.hwkedu.reggie.entity.User;
import com.hwkedu.reggie.service.UserService;
import com.hwkedu.reggie.utils.SMSUtils;
import com.hwkedu.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * ClassName: UserController
 * Package: com.hwkedu.reggie.controller
 * Description:
 *
 * @Author: 侯文柯
 * @Create: 2022/12/1 - 16:51
 * @Version: v1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode4String(4);
            log.info("code={}",code);

            //调用阿里云提供的短信服务API完成短信发送
            //SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);

            //需要将生成的验证码保存到session
            session.setAttribute(phone,code);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map ,HttpSession session ){
        log.info("信息为{}",map);
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //判断和session域中的数据是否匹配
        String codeInSession = session.getAttribute(phone).toString();
        if (code != null && code.equalsIgnoreCase(codeInSession)){
            //说明匹配成功//判断用户是不是新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user == null){
                //说明是新用户,添加到user表中
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            //保存id到session域中,让其不被过滤器拦截
            session.setAttribute("user",user.getId());

            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 退出功能
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session){
        //删除当前登录的session
        session.removeAttribute("user");
        return R.success("推出成功");
    }
}
