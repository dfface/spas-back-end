package com.spas.backend.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "SuggestionVo 实体", description = "检察建议大纲返回内容")
public class SuggestionVo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String supervisedName;

  private String content;

  private LocalDateTime deadline;

  private String caseId;

  private String creatorId;

  private String officeId;

  private Integer state;

  private LocalDateTime createTime;

  private Double score;

  private Boolean sue;
}
