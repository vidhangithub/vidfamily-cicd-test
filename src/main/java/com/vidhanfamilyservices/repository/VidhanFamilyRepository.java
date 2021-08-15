package com.vidhanfamilyservices.repository;

import com.vidhanfamilyservices.model.VidhanFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VidhanFamilyRepository extends JpaRepository<VidhanFamily, Long> {
}
