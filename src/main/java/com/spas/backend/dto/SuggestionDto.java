package com.spas.backend.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SuggestionDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;  // 保存之后会显示

  private String supervisedName;

  private String content;

  private LocalDateTime deadline;

  private String caseId;

  private String creatorId;

  private String officeId;

  private String[] email;
}
