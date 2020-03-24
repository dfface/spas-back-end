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
 * 关系：整改计划与整改报告.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PlanReport对象", description = "整改计划-整改报告")
public class PlanReport implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "rep_id", type = IdType.INPUT)
  private String repId;

  @TableId(value = "pla_id", type = IdType.INPUT)
  private String plaId;

  private LocalDateTime createTime;

  @TableField(update = "now()")
  private LocalDateTime updateTime;

  @TableLogic(value = "b'0'", delval = "b'1'")
  private Boolean isDeleted;

  private String officeId;


}
