package com.dream.common.entity.shiro;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class User extends Model<User> implements Serializable {
    private static final long serialVersionUID = -297287665617580L;
    private Long userId;
    /**登录用户名*/
    private String userName;
    /**名称（昵称或者真实姓名，根据实际情况定义）*/
    private String name;
    private String password;
    /**加密密码的盐*/
    private String salt;
    /**用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.*/
    private byte state;
    /**一个用户具有多个角色*/
    @TableField(exist = false)
    private List<Role> roleList;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    /**创建时间*/
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    /**过期日期*/
    private Date expiredDate;
    private String email;

    @Override
    protected Serializable pkVal() {
        return userId;
    }
    /**
     * 密码盐. 重新对盐重新进行了定义，用户名+salt，这样就不容易被破解，可以采用多种方式定义加盐
     * @return
     */
}
