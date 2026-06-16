package com.universite.apirest.controller;

import com.universite.apirest.entity.*;
import com.universite.apirest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestDataController {

    private final UtilisateurRepository utilisateurRepository;
    private final EtudiantRepository etudiantRepository;
    private final ProfesseurRepository professeurRepository;
    private final ModuleRepository moduleRepository;
    private final CoursRepository coursRepository;
    private final SalleRepository salleRepository;
    private final NoteRepository noteRepository;
    private final PaiementRepository paiementRepository;
    private final EmploiDuTempsRepository emploiDuTempsRepository;
    private final NotificationRepository notificationRepository;

    @PostMapping("/load-data")
    public ResponseEntity<Map<String, Object>> loadTestData() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Supprimer les données existantes
            notificationRepository.deleteAll();
            emploiDuTempsRepository.deleteAll();
            paiementRepository.deleteAll();
            noteRepository.deleteAll();
            coursRepository.deleteAll();
            etudiantRepository.deleteAll();
            salleRepository.deleteAll();
            moduleRepository.deleteAll();
            professeurRepository.deleteAll();
            utilisateurRepository.deleteAll();

            // 1. Créer des utilisateurs
            Utilisateur admin = new Utilisateur();
            admin.setUsername("admin");
            admin.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"); // password: admin123
            admin.setEmail("admin@uadb.sn");
            admin.setRole(Role.ADMIN);
            admin.setActif(true);
            utilisateurRepository.save(admin);

            Utilisateur profUser = new Utilisateur();
            profUser.setUsername("prof1");
            profUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
            profUser.setEmail("prof1@uadb.sn");
            profUser.setRole(Role.PROFESSEUR);
            profUser.setActif(true);
            utilisateurRepository.save(profUser);

            Utilisateur etudUser = new Utilisateur();
            etudUser.setUsername("etud1");
            etudUser.setPassword("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy");
            etudUser.setEmail("etud1@uadb.sn");
            etudUser.setRole(Role.ETUDIANT);
            etudUser.setActif(true);
            utilisateurRepository.save(etudUser);

            result.put("utilisateurs", 3);

            // 2. Créer des professeurs
            Professeur prof1 = new Professeur();
            prof1.setNom("Diop");
            prof1.setPrenom("Amadou");
            prof1.setEmail("amadou.diop@uadb.sn");
            prof1.setTelephone("778899001");
            prof1.setSpecialite("Informatique");
            prof1.setGrade("Maître de Conférences");
            professeurRepository.save(prof1);

            Professeur prof2 = new Professeur();
            prof2.setNom("Ndiaye");
            prof2.setPrenom("Fatou");
            prof2.setEmail("fatou.ndiaye@uadb.sn");
            prof2.setTelephone("771122334");
            prof2.setSpecialite("Mathématiques");
            prof2.setGrade("Professeur Titulaire");
            professeurRepository.save(prof2);

            Professeur prof3 = new Professeur();
            prof3.setNom("Fall");
            prof3.setPrenom("Cheikh");
            prof3.setEmail("cheikh.fall@uadb.sn");
            prof3.setTelephone("775566778");
            prof3.setSpecialite("Physique");
            prof3.setGrade("Assistant");
            professeurRepository.save(prof3);

            result.put("professeurs", 3);

            // 3. Créer des modules
            com.universite.apirest.entity.Module module1 = new com.universite.apirest.entity.Module();
            module1.setCode("INF101");
            module1.setNom("Introduction à la Programmation");
            module1.setDescription("Module d'introduction aux concepts de base de la programmation");
            module1.setCredit(6);
            module1.setVolumeHoraire(45);
            moduleRepository.save(module1);

            com.universite.apirest.entity.Module module2 = new com.universite.apirest.entity.Module();
            module2.setCode("MAT201");
            module2.setNom("Algèbre Linéaire");
            module2.setDescription("Module d'algèbre linéaire et espaces vectoriels");
            module2.setCredit(5);
            module2.setVolumeHoraire(40);
            moduleRepository.save(module2);

            com.universite.apirest.entity.Module module3 = new com.universite.apirest.entity.Module();
            module3.setCode("PHY301");
            module3.setNom("Mécanique Quantique");
            module3.setDescription("Introduction à la mécanique quantique");
            module3.setCredit(4);
            module3.setVolumeHoraire(35);
            moduleRepository.save(module3);

            com.universite.apirest.entity.Module module4 = new com.universite.apirest.entity.Module();
            module4.setCode("INF202");
            module4.setNom("Structures de Données");
            module4.setDescription("Structures de données avancées et algorithmes");
            module4.setCredit(6);
            module4.setVolumeHoraire(50);
            moduleRepository.save(module4);

            result.put("modules", 4);

            // 4. Créer des salles
            Salle salle1 = new Salle();
            salle1.setCode("A101");
            salle1.setNom("Amphi A");
            salle1.setBatiment("A");
            salle1.setCapacite(150);
            salle1.setType("AMPHI");
            salle1.setEquipementVideo(true);
            salleRepository.save(salle1);

            Salle salle2 = new Salle();
            salle2.setCode("B205");
            salle2.setNom("Salle TD B205");
            salle2.setBatiment("B");
            salle2.setCapacite(40);
            salle2.setType("TD");
            salle2.setEquipementVideo(false);
            salleRepository.save(salle2);

            Salle salle3 = new Salle();
            salle3.setCode("C301");
            salle3.setNom("Laboratoire C");
            salle3.setBatiment("C");
            salle3.setCapacite(25);
            salle3.setType("LABORATOIRE");
            salle3.setEquipementVideo(true);
            salleRepository.save(salle3);

            Salle salle4 = new Salle();
            salle4.setCode("D102");
            salle4.setNom("Salle Classe D102");
            salle4.setBatiment("D");
            salle4.setCapacite(60);
            salle4.setType("SALLE_CLASSE");
            salle4.setEquipementVideo(true);
            salleRepository.save(salle4);

            result.put("salles", 4);

            // 5. Créer des étudiants
            Etudiant etud1 = new Etudiant();
            etud1.setNom("Sow");
            etud1.setPrenom("Moussa");
            etud1.setEmail("moussa.sow@uadb.sn");
            etud1.setSexe("M");
            etud1.setDateNaissance(LocalDate.of(2000, 5, 15));
            etud1.setLieuNaissance("Dakar");
            etud1.setNationalite("Sénégalaise");
            etud1.setTelephone("771234567");
            etud1.setAdresse("Dakar, Sénégal");
            etud1.setFiliere(Fillier.D2A);
            etud1.setNiveau(Niveau.Licence1);
            etudiantRepository.save(etud1);

            Etudiant etud2 = new Etudiant();
            etud2.setNom("Ba");
            etud2.setPrenom("Aminata");
            etud2.setEmail("aminata.ba@uadb.sn");
            etud2.setSexe("F");
            etud2.setDateNaissance(LocalDate.of(2001, 8, 22));
            etud2.setLieuNaissance("Saint-Louis");
            etud2.setNationalite("Sénégalaise");
            etud2.setTelephone("772345678");
            etud2.setAdresse("Saint-Louis, Sénégal");
            etud2.setFiliere(Fillier.SRT);
            etud2.setNiveau(Niveau.Licence2);
            etudiantRepository.save(etud2);

            Etudiant etud3 = new Etudiant();
            etud3.setNom("Diagne");
            etud3.setPrenom("Ibrahima");
            etud3.setEmail("ibrahima.diagne@uadb.sn");
            etud3.setSexe("M");
            etud3.setDateNaissance(LocalDate.of(1999, 12, 10));
            etud3.setLieuNaissance("Thiès");
            etud3.setNationalite("Sénégalaise");
            etud3.setTelephone("773456789");
            etud3.setAdresse("Thiès, Sénégal");
            etud3.setFiliere(Fillier.MPCI);
            etud3.setNiveau(Niveau.Licence3);
            etudiantRepository.save(etud3);

            Etudiant etud4 = new Etudiant();
            etud4.setNom("Ndiaye");
            etud4.setPrenom("Mariama");
            etud4.setEmail("mariama.ndiaye@uadb.sn");
            etud4.setSexe("F");
            etud4.setDateNaissance(LocalDate.of(2000, 3, 8));
            etud4.setLieuNaissance("Kaolack");
            etud4.setNationalite("Sénégalaise");
            etud4.setTelephone("774567890");
            etud4.setAdresse("Kaolack, Sénégal");
            etud4.setFiliere(Fillier.MPI);
            etud4.setNiveau(Niveau.Master1);
            etudiantRepository.save(etud4);

            Etudiant etud5 = new Etudiant();
            etud5.setNom("Kane");
            etud5.setPrenom("Ousmane");
            etud5.setEmail("ousmane.kane@uadb.sn");
            etud5.setSexe("M");
            etud5.setDateNaissance(LocalDate.of(2002, 7, 30));
            etud5.setLieuNaissance("Ziguinchor");
            etud5.setNationalite("Sénégalaise");
            etud5.setTelephone("775678901");
            etud5.setAdresse("Ziguinchor, Sénégal");
            etud5.setFiliere(Fillier.PC);
            etud5.setNiveau(Niveau.Master2);
            etudiantRepository.save(etud5);

            result.put("etudiants", 5);

            // 6. Créer des cours
            Cours cours1 = new Cours();
            cours1.setTitre("Programmation Java");
            cours1.setDescription("Cours de programmation orientée objet avec Java");
            cours1.setModule(module1);
            cours1.setProfesseur(prof1);
            cours1.setFiliere(Fillier.D2A);
            cours1.setNiveau(Niveau.Licence1);
            cours1.setSemestre(1);
            coursRepository.save(cours1);

            Cours cours2 = new Cours();
            cours2.setTitre("Algèbre Avancée");
            cours2.setDescription("Cours d'algèbre linéaire appliquée");
            cours2.setModule(module2);
            cours2.setProfesseur(prof2);
            cours2.setFiliere(Fillier.MPCI);
            cours2.setNiveau(Niveau.Licence2);
            cours2.setSemestre(1);
            coursRepository.save(cours2);

            Cours cours3 = new Cours();
            cours3.setTitre("Physique Quantique");
            cours3.setDescription("Introduction aux principes de la mécanique quantique");
            cours3.setModule(module3);
            cours3.setProfesseur(prof3);
            cours3.setFiliere(Fillier.PC);
            cours3.setNiveau(Niveau.Licence3);
            cours3.setSemestre(2);
            coursRepository.save(cours3);

            Cours cours4 = new Cours();
            cours4.setTitre("Algorithmes et Structures de Données");
            cours4.setDescription("Algorithmes avancés et structures de données");
            cours4.setModule(module4);
            cours4.setProfesseur(prof1);
            cours4.setFiliere(Fillier.SRT);
            cours4.setNiveau(Niveau.Licence2);
            cours4.setSemestre(2);
            coursRepository.save(cours4);

            result.put("cours", 4);

            // 7. Créer des notes
            Note note1 = new Note();
            note1.setEtudiant(etud1);
            note1.setCours(cours1);
            note1.setValeur(new BigDecimal("15.50"));
            note1.setTypeNote("CC");
            note1.setSemestre(1);
            noteRepository.save(note1);

            Note note2 = new Note();
            note2.setEtudiant(etud1);
            note2.setCours(cours1);
            note2.setValeur(new BigDecimal("14.00"));
            note2.setTypeNote("TP");
            note2.setSemestre(1);
            noteRepository.save(note2);

            Note note3 = new Note();
            note3.setEtudiant(etud1);
            note3.setCours(cours1);
            note3.setValeur(new BigDecimal("16.50"));
            note3.setTypeNote("Examen");
            note3.setSemestre(1);
            noteRepository.save(note3);

            Note note4 = new Note();
            note4.setEtudiant(etud2);
            note4.setCours(cours4);
            note4.setValeur(new BigDecimal("13.00"));
            note4.setTypeNote("CC");
            note4.setSemestre(2);
            noteRepository.save(note4);

            Note note5 = new Note();
            note5.setEtudiant(etud3);
            note5.setCours(cours2);
            note5.setValeur(new BigDecimal("17.50"));
            note5.setTypeNote("Examen");
            note5.setSemestre(1);
            noteRepository.save(note5);

            result.put("notes", 5);

            // 8. Créer des paiements
            Paiement paiement1 = new Paiement();
            paiement1.setEtudiant(etud1);
            paiement1.setType("INSCRIPTION");
            paiement1.setMontant(new BigDecimal("50000.00"));
            paiement1.setMethodePaiement("WAVE");
            paiement1.setStatut("PAYE");
            paiement1.setReferenceTransaction("WAVE123456789");
            paiement1.setAnneeAcademique(2024);
            paiementRepository.save(paiement1);

            Paiement paiement2 = new Paiement();
            paiement2.setEtudiant(etud2);
            paiement2.setType("SCOLARITE");
            paiement2.setMontant(new BigDecimal("150000.00"));
            paiement2.setMethodePaiement("ORANGE_MONEY");
            paiement2.setStatut("PAYE");
            paiement2.setReferenceTransaction("OM987654321");
            paiement2.setAnneeAcademique(2024);
            paiementRepository.save(paiement2);

            Paiement paiement3 = new Paiement();
            paiement3.setEtudiant(etud3);
            paiement3.setType("INSCRIPTION");
            paiement3.setMontant(new BigDecimal("50000.00"));
            paiement3.setMethodePaiement("ESPECES");
            paiement3.setStatut("EN_ATTENTE");
            paiement3.setAnneeAcademique(2024);
            paiementRepository.save(paiement3);

            Paiement paiement4 = new Paiement();
            paiement4.setEtudiant(etud4);
            paiement4.setType("SCOLARITE");
            paiement4.setMontant(new BigDecimal("150000.00"));
            paiement4.setMethodePaiement("CARTE");
            paiement4.setStatut("PAYE");
            paiement4.setReferenceTransaction("CARD456789123");
            paiement4.setAnneeAcademique(2024);
            paiementRepository.save(paiement4);

            result.put("paiements", 4);

            // 9. Créer des emplois du temps
            EmploiDuTemps edt1 = new EmploiDuTemps();
            edt1.setCours(cours1);
            edt1.setSalle(salle1);
            edt1.setJour(DayOfWeek.MONDAY);
            edt1.setHeureDebut(LocalTime.of(8, 0));
            edt1.setHeureFin(LocalTime.of(10, 0));
            edt1.setSemestre(1);
            edt1.setAnneeAcademique(2024);
            emploiDuTempsRepository.save(edt1);

            EmploiDuTemps edt2 = new EmploiDuTemps();
            edt2.setCours(cours2);
            edt2.setSalle(salle2);
            edt2.setJour(DayOfWeek.WEDNESDAY);
            edt2.setHeureDebut(LocalTime.of(10, 0));
            edt2.setHeureFin(LocalTime.of(12, 0));
            edt2.setSemestre(1);
            edt2.setAnneeAcademique(2024);
            emploiDuTempsRepository.save(edt2);

            EmploiDuTemps edt3 = new EmploiDuTemps();
            edt3.setCours(cours3);
            edt3.setSalle(salle3);
            edt3.setJour(DayOfWeek.FRIDAY);
            edt3.setHeureDebut(LocalTime.of(14, 0));
            edt3.setHeureFin(LocalTime.of(16, 0));
            edt3.setSemestre(2);
            edt3.setAnneeAcademique(2024);
            emploiDuTempsRepository.save(edt3);

            EmploiDuTemps edt4 = new EmploiDuTemps();
            edt4.setCours(cours4);
            edt4.setSalle(salle4);
            edt4.setJour(DayOfWeek.TUESDAY);
            edt4.setHeureDebut(LocalTime.of(9, 0));
            edt4.setHeureFin(LocalTime.of(11, 0));
            edt4.setSemestre(2);
            edt4.setAnneeAcademique(2024);
            emploiDuTempsRepository.save(edt4);

            result.put("emploi_du_temps", 4);

            // 10. Créer des notifications
            Notification notif1 = new Notification();
            notif1.setEtudiant(etud1);
            notif1.setTitre("Note publiée");
            notif1.setMessage("Votre note pour le cours de Programmation Java a été publiée");
            notif1.setType("NOTE");
            notif1.setCanal("EMAIL");
            notif1.setLu(false);
            notif1.setStatut("ENVOYE");
            notificationRepository.save(notif1);

            Notification notif2 = new Notification();
            notif2.setEtudiant(etud2);
            notif2.setTitre("Paiement reçu");
            notif2.setMessage("Votre paiement de scolarité a été reçu avec succès");
            notif2.setType("PAIEMENT");
            notif2.setCanal("SMS");
            notif2.setLu(false);
            notif2.setStatut("ENVOYE");
            notificationRepository.save(notif2);

            Notification notif3 = new Notification();
            notif3.setEtudiant(etud3);
            notif3.setTitre("Cours annulé");
            notif3.setMessage("Le cours d'Algèbre Avancée de mercredi est annulé");
            notif3.setType("COURS");
            notif3.setCanal("EMAIL");
            notif3.setLu(true);
            notif3.setStatut("ENVOYE");
            notificationRepository.save(notif3);

            Notification notif4 = new Notification();
            notif4.setEtudiant(etud4);
            notif4.setTitre("Rappel inscription");
            notif4.setMessage("N'oubliez pas de vous inscrire pour le semestre prochain");
            notif4.setType("GENERAL");
            notif4.setCanal("EMAIL");
            notif4.setLu(false);
            notif4.setStatut("EN_ATTENTE");
            notificationRepository.save(notif4);

            result.put("notifications", 4);

            result.put("status", "SUCCESS");
            result.put("message", "Données de test insérées avec succès");

        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", "Erreur lors de l'insertion des données: " + e.getMessage());
        }

        return ResponseEntity.ok(result);
    }
}
