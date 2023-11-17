package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

}
