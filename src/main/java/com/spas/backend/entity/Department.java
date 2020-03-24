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
 * 部门实体.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Department对象", description = "部门实体")
public class Department implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.ASSIGN_UUID)
  private String id;

  private String name;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  private Boolean isDeleted;

  private String officeId;

}
