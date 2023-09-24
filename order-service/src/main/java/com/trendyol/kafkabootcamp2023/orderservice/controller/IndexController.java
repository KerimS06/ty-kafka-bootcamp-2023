package com.trendyol.kafkabootcamp2023.orderservice.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequestMapping("/")
public class IndexController {

    public static final String SWAGGER_PAGE = "/swagger-ui.html";

    @GetMapping
    public RedirectView redirectToSwaggerUi() {
        return new RedirectView(SWAGGER_PAGE);
    }

}
