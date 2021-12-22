package main.model;

import lombok.Data;
import main.enumerated.ValuesGlobalSetting;

import javax.persistence.*;
@Data
@Entity
@Table(name = "global_settings")
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ValuesGlobalSetting.Code code;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ValuesGlobalSetting.Value value;


}
