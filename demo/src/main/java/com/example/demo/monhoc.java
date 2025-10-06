package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class monhoc  {

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello, dao dang tai";
    }

    @GetMapping("/about")
    @ResponseBody
    public String about() {
        return """
               <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px;">
                   <h1 style="color: #333; text-align: center;">Thông Tin Cá Nhân</h1>
                   <div style="background-color: #f9f9f9; padding: 20px; border-radius: 8px;">
                       <p><strong>MSSV:</strong> TH02797</p>
                       <p><strong>Họ và tên:</strong> dao dang tai</p>
                       <p><strong>Sở thích:</strong> choi game doc truyen</p>
                       <p><strong>Môn học:</strong> Lập trình Java 4 - SOF3012</p>
                   </div>
               </div>
               """;
    }


}