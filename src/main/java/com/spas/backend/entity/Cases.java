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
 * Case 实体.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "Cases对象", description = "案件实体")
public class Cases implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.ASSIGN_UUID)
  private String id;

  @ApiModelProperty(value = "标题")
  private String name;

  @ApiModelProperty(value = "涉及的行政单位")
  private String government;

  @ApiModelProperty(value = "基本案情")
  private String description;

  @ApiModelProperty(value = "调查情况")
  private String investigation;

  @ApiModelProperty(value = "审核意见")
  private String opinion;

  private Integer state;

  private LocalDateTime createTime;

  private LocalDateTime terminateTime;

  @TableField(update = "now()")
  private LocalDateTime updateTime;

  @TableLogic(value = "b'0'", delval = "b'1'")
  private Boolean isDeleted;

  @ApiModelProperty(value = "创建人冗余字段")
  private String creatorId;

  private String officeId;


}
