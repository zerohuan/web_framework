package com.yjh.cg.site.repository;

import com.yjh.cg.site.entities.BUserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * This is first repository with jpa and Spring DATA.
 * If you can using CrudRepository, avoiding using JpaRepository.
 * Using JpaRepository will expose more details.
 *
 * Created by yjh on 15-10-8.
 */
public interface UserRepository extends PagingAndSortingRepository<BUserEntity, Long> {
    BUserEntity getByUsernameAndPasswordAndRole(String username, String password, String role);

}
