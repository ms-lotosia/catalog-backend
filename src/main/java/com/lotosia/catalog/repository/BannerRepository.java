package com.lotosia.catalog.repository;

import com.lotosia.catalog.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: nijataghayev
 */

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findByActiveTrueOrderByCreatedDateDesc();
}