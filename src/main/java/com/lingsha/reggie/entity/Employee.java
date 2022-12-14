package com.lingsha.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @auther Ling.Sha
 * @date 2022/8/5 - 9:43
 */

/**
 * 员工实体
 */
@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;//身份正

    private Integer status;
    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入时和更新时填充
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)//插入时候填充
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和更新时候填充
    private Long updateUser;

}
