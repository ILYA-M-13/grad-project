package main.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "datetime",nullable = false)
    private Date time;

    @Column(columnDefinition = "TINYTEXT",nullable = false)
    private String code;

    @Column(columnDefinition = "TINYTEXT",name = "secret_code",nullable = false)
    private String secretCode;

}
