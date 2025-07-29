package com.neero.ticketing_service.repository;

import com.neero.ticketing_service.entity.Media;
import com.neero.ticketing_service.enums.MediaContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    Optional<Media> findByMediaId(String mediaId);
    
    List<Media> findByContextAndContextIdAndActiveOrderByDisplayOrder(
            MediaContext context, String contextId, Boolean active);
    
    List<Media> findByContextAndContextId(MediaContext context, String contextId);
    
    @Query("SELECT m FROM Media m WHERE m.context = :context AND m.contextId = :contextId AND m.active = true ORDER BY m.displayOrder ASC")
    List<Media> findActiveMediaByContext(@Param("context") MediaContext context, @Param("contextId") String contextId);
    
    void deleteByMediaId(String mediaId);
    
    boolean existsByMediaId(String mediaId);
}