package com.wowo.wowo.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("mail")
@Tag(name = "Mail", description = "Gửi mail")
public class MailController {

}
