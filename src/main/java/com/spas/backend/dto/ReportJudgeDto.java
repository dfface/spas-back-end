package com.spas.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ReportJudgeDto实体", description = "评价报告的数据传输对象")
public class ReportJudgeDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String judge;

  private Double score;

  private Integer nextState;
}
