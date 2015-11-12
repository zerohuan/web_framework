package com.yjh.cg.site.repository;

import com.yjh.base.site.repository.CustomRepository;
import com.yjh.cg.site.entities.BUserEntity;

/**
 * This is first repository with jpa and Spring DATA.
 * If you can using CrudRepository, avoiding using JpaRepository.
 * Using JpaRepository will expose more details.
 *
 * Created by yjh on 15-10-8.
 */
public interface UserRepository extends CustomRepository<BUserEntity, Long> {
    BUserEntity getByUsernameAndPasswordAndRole(String username, String password, String role);
    BUserEntity save(BUserEntity userEntity);
    BUserEntity getByUsername(String username);
}
