package com.spas.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *   关系：用户-角色.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserRole对象", description = "用户-角色")
public class UserRole implements Serializable {

  public static final long serialVersionUID = 1L;

  @TableId(type = IdType.INPUT)
  private String useId;

  @TableId(type = IdType.INPUT)
  private String rolId;

  private LocalDateTime createTime;

  @TableField(update = "now()")
  private LocalDateTime updateTime;

  @TableLogic(value = "b'0'", delval = "b'1'")
  private Boolean isDeleted;

  private String officeId;
}
