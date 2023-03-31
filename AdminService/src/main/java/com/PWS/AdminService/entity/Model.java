package com.PWS.AdminService.entity;

import com.PWS.AdminService.Utility.AuditModel;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Model extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @Column(name = "model_id")
    private Integer id;
    @Column(nullable = false)
    private String name;

    @ColumnDefault("true")
    private Boolean isActive=true;

}




