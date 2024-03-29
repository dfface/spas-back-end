package com.spas.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 配置Swagger接口文档，建议只在开发时使用，@Profile().
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("检察建议流程辅助办案系统")
                .description("API 接口文档")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.spas.backend"))
                .paths(PathSelectors.any())
                .build();
    }
}
