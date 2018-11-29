package org.yuanmeng.zwolfserver.sso.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yuanmeng.zwolfserver.sso.entity.UserEntity;

/**
 * @author wangtonghe
 * @since 2018/11/30 00:22
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {


}
