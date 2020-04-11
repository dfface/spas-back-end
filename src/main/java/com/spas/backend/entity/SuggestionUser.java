package com.spas.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "SuggestionUser 实体" ,description = "检察建议与用户关联")
public class SuggestionUser implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.INPUT)
  private String sugId;

  @TableId(type = IdType.INPUT)
  private String useId;

  private LocalDateTime createTime;

  @TableField(update = "now()")
  private LocalDateTime updateTime;

  @TableLogic(value = "b'0'", delval = "b'1'")
  private Boolean isDeleted;

  private String officeId;

}
