package org.yuanmeng.zwolfserver.sso.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author wangtonghe
 * @since 2018/11/30 00:23
 */
@Entity
@Data
@Table(name = "zwere_sso_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String userName;

    private String password;

    private long createTime;

    private long updateTime;
}
