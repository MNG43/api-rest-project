package com.universite.apirest.repository;

import com.universite.apirest.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByEtudiantId(Long etudiantId);
    List<Notification> findByEtudiantIdAndLu(Long etudiantId, boolean lu);
    List<Notification> findByStatut(String statut);
    List<Notification> findByType(String type);
}
