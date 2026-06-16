package com.universite.apirest.service;

import com.universite.apirest.entity.Etudiant;
import com.universite.apirest.entity.Notification;
import com.universite.apirest.repository.EtudiantRepository;
import com.universite.apirest.repository.NotificationRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EtudiantRepository etudiantRepository;
    private final JavaMailSender javaMailSender;

    public NotificationService(NotificationRepository notificationRepository,
                               EtudiantRepository etudiantRepository,
                               JavaMailSender javaMailSender) {
        this.notificationRepository = notificationRepository;
        this.etudiantRepository = etudiantRepository;
        this.javaMailSender = javaMailSender;
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> getNotificationsByEtudiant(Long etudiantId) {
        return notificationRepository.findByEtudiantId(etudiantId);
    }

    public List<Notification> getNotificationsByEtudiantAndLu(Long etudiantId, boolean lu) {
        return notificationRepository.findByEtudiantIdAndLu(etudiantId, lu);
    }

    public List<Notification> getNotificationsByStatut(String statut) {
        return notificationRepository.findByStatut(statut);
    }

    public List<Notification> getNotificationsByType(String type) {
        return notificationRepository.findByType(type);
    }

    public Notification createNotification(Notification notification) {
        if (notification.getEtudiant() != null) {
            Etudiant etudiant = etudiantRepository.findById(notification.getEtudiant().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));
            notification.setEtudiant(etudiant);
        }
        
        notification.setStatut("EN_ATTENTE");
        notification.setLu(false);
        
        return notificationRepository.save(notification);
    }

    public Notification envoyerNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification non trouvée"));

        boolean succes = false;

        if ("EMAIL".equals(notification.getCanal())) {
            succes = envoyerEmail(notification);
        } else if ("SMS".equals(notification.getCanal())) {
            succes = envoyerSMS(notification);
        }

        if (succes) {
            notification.setStatut("ENVOYE");
            notification.setDateEnvoi(LocalDateTime.now());
        } else {
            notification.setStatut("ECHOUE");
        }

        return notificationRepository.save(notification);
    }

    private boolean envoyerEmail(Notification notification) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            
            if (notification.getEtudiant() != null) {
                message.setTo(notification.getEtudiant().getEmail());
            } else {
                message.setTo("admin@universite.sn");
            }
            
            message.setSubject(notification.getTitre());
            message.setText(notification.getMessage());
            
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean envoyerSMS(Notification notification) {
        try {
            // Simulation d'envoi SMS
            Random random = new Random();
            boolean succes = random.nextDouble() > 0.2; // 80% de succès
            
            if (succes && notification.getEtudiant() != null) {
                System.out.println("SMS envoyé à " + notification.getEtudiant().getTelephone() + 
                                 ": " + notification.getTitre() + " - " + notification.getMessage());
            }
            
            return succes;
        } catch (Exception e) {
            return false;
        }
    }

    public Notification marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification non trouvée"));
        
        notification.setLu(true);
        return notificationRepository.save(notification);
    }

    public Notification creerNotificationNote(Long etudiantId, String titre, String message) {
        Notification notification = new Notification();
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType("NOTE");
        notification.setCanal("EMAIL");
        
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));
        notification.setEtudiant(etudiant);
        
        return createNotification(notification);
    }

    public Notification creerNotificationPaiement(Long etudiantId, String titre, String message) {
        Notification notification = new Notification();
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType("PAIEMENT");
        notification.setCanal("EMAIL");
        
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));
        notification.setEtudiant(etudiant);
        
        return createNotification(notification);
    }

    public Notification creerNotificationCours(Long etudiantId, String titre, String message) {
        Notification notification = new Notification();
        notification.setTitre(titre);
        notification.setMessage(message);
        notification.setType("COURS");
        notification.setCanal("EMAIL");
        
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new IllegalArgumentException("Étudiant non trouvé"));
        notification.setEtudiant(etudiant);
        
        return createNotification(notification);
    }

    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification non trouvée avec l'ID: " + id));
        notificationRepository.delete(notification);
    }
}
