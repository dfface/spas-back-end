package com.spas.backend.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ReportDto实体", description = "新建报告的数据传输对象")
public class ReportDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String content;

  private String caseId;

  private String suggestionId;

  private String creatorId;

  private String officeId;
}
