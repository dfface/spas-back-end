package com.spas.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 关联：角色-操作项.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RoleOperation对象", description = "角色-操作项")
public class RoleOperation implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "ope_id", type = IdType.INPUT)
  private String opeId;

  @TableId(value = "rol_id", type = IdType.INPUT)
  private String rolId;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  private Boolean isDeleted;

  private String officeId;

}
