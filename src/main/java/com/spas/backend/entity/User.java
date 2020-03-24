package com.spas.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户实体.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "User对象", description = "用户")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.ASSIGN_UUID)
  private String id;

  private String name;

  @ApiModelProperty(value = "职务")
  private String position;

  private String email;

  private String phone;

  private String departmentId;

  private String officeId;

  private String avatar;

  @ApiModelProperty(value = "类型")
  private Integer type;

  private LocalDateTime createTime;

  @TableField(update = "now()")
  private LocalDateTime updateTime;

  @TableLogic(value = "b'0'", delval = "b'1'")
  private Boolean isDeleted;

  private String officeName;

}
