package com.dream.entity;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity(name = "qiao_user")
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "u_id", length = 11)
    private Integer uId;
    private String uName;
}
