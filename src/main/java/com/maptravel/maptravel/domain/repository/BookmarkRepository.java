package com.maptravel.maptravel.domain.repository;

import com.maptravel.maptravel.domain.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
