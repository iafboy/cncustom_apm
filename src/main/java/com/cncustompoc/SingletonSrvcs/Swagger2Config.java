package com.cncustompoc.SingletonSrvcs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cncustompoc.SingletonSrvcs.controllers"))
                .paths(PathSelectors.any())
                .build()
                ;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("H2018 POC Restful APIs 文档")
                .description("本文档主要说明数据服务的 Restful APIs 用法，以及多版本的使用。")
                .contact("Oracle").build();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
