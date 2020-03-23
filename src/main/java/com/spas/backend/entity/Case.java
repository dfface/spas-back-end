package com.spas.backend.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Case对象", description="")
public class Case implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "标题")
    private String name;

    @ApiModelProperty(value = "涉及的行政单位")
    private String government;

    @ApiModelProperty(value = "基本案情")
    private String description;

    @ApiModelProperty(value = "调查情况")
    private String invastigation;

    private String category;

    @ApiModelProperty(value = "审核意见")
    private String opinion;

    private Integer state;

    private LocalDateTime createTime;

    private LocalDateTime terminateTime;

    private LocalDateTime updateTime;

    private Boolean isDeleted;

    @ApiModelProperty(value = "创建人冗余字段")
    private String creatorId;

    private String officeId;


}
