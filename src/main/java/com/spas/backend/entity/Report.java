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
 * 整改报告实体.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Report对象", description = "整改报告")
public class Report implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.ASSIGN_UUID)
  private String id;

  @ApiModelProperty(value = "内容")
  private String content;

  @ApiModelProperty(value = "针对内容评价")
  private String judge;

  @ApiModelProperty(value = "针对内容评分")
  private String score;

  private Integer state;

  private String caseId;

  private String suggestionId;

  private LocalDateTime createTime;

  @TableField(update = "now()")
  private LocalDateTime updateTime;

  @TableLogic(value = "b'0'", delval = "b'1'")
  private Boolean isDeleted;

  private String creatorId;

  private String officeId;

}
