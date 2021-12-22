package main.model;

import lombok.Data;


import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(columnDefinition = "TINYTEXT",nullable = false)
    private String code;     //код, отображаемый на картинкке капчи

    @Column(columnDefinition = "TINYTEXT",name = "secret_code",nullable = false)
    private String secretCode;//код, передаваемый в параметре

  }
