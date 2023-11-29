package com.intallysh.widom.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rId;
    private int roleId;

    private String roleTitle;
    private String roleDesc;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Roles(int roleId, String roleTitle, String roleDesc, User user) {
        this.roleId = roleId;
        this.roleTitle = roleTitle;
        this.roleDesc = roleDesc;
        this.user = user;
    }
}
