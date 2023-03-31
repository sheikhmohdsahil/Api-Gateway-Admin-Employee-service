package com.PWS.AdminService.entity;

import com.PWS.AdminService.Utility.AuditModel;
import com.PWS.AdminService.Utility.AuditModel;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission  extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer permId;
    @ColumnDefault("true")
    private Boolean isActive=true;
    @Column(nullable = false)
    private Boolean isView;
    @Column(nullable = false)
    private Boolean isAdd;

    @Column(nullable = false)
    private Boolean isUpdate;
    @Column(nullable = false)
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;



    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;



}
