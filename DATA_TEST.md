# Données de Test Insérées

Ce document décrit les données de test insérées dans chaque table de la base de données.

## Endpoint de Chargement

```bash
POST http://localhost:8082/api/v1/test/load-data
```

## Résumé des Données Insérées

| Table | Nombre d'enregistrements |
|-------|-------------------------|
| utilisateurs | 3 |
| professeurs | 3 |
| modules | 4 |
| salles | 4 |
| etudiants | 5 |
| cours | 4 |
| notes | 5 |
| paiements | 4 |
| emploi_du_temps | 4 |
| notifications | 4 |
| **Total** | **36** |

## Détail par Table

### 1. Utilisateurs (3 enregistrements)

| Username | Email | Rôle |
|----------|-------|------|
| admin | admin@uadb.sn | ADMIN |
| prof1 | prof1@uadb.sn | PROFESSEUR |
| etud1 | etud1@uadb.sn | ETUDIANT |

**Mot de passe pour tous**: `admin123`

### 2. Professeurs (3 enregistrements)

| Nom | Prénom | Email | Téléphone | Spécialité | Grade |
|-----|--------|-------|-----------|------------|-------|
| Diop | Amadou | amadou.diop@uadb.sn | 778899001 | Informatique | Maître de Conférences |
| Ndiaye | Fatou | fatou.ndiaye@uadb.sn | 771122334 | Mathématiques | Professeur Titulaire |
| Fall | Cheikh | cheikh.fall@uadb.sn | 775566778 | Physique | Assistant |

### 3. Modules (4 enregistrements)

| Code | Nom | Crédits | Volume Horaire |
|------|-----|---------|----------------|
| INF101 | Introduction à la Programmation | 6 | 45 |
| MAT201 | Algèbre Linéaire | 5 | 40 |
| PHY301 | Mécanique Quantique | 4 | 35 |
| INF202 | Structures de Données | 6 | 50 |

### 4. Salles (4 enregistrements)

| Code | Nom | Bâtiment | Capacité | Type | Vidéo |
|------|-----|----------|----------|------|-------|
| A101 | Amphi A | A | 150 | AMPHI | Oui |
| B205 | Salle TD B205 | B | 40 | TD | Non |
| C301 | Laboratoire C | C | 25 | LABORATOIRE | Oui |
| D102 | Salle Classe D102 | D | 60 | SALLE_CLASSE | Oui |

### 5. Étudiants (5 enregistrements)

| Nom | Prénom | Email | Filière | Niveau |
|-----|--------|-------|---------|--------|
| Sow | Moussa | moussa.sow@uadb.sn | D2A | Licence1 |
| Ba | Aminata | aminata.ba@uadb.sn | SRT | Licence2 |
| Diagne | Ibrahima | ibrahima.diagne@uadb.sn | MPCI | Licence3 |
| Ndiaye | Mariama | mariama.ndiaye@uadb.sn | MPI | Master1 |
| Kane | Ousmane | ousmane.kane@uadb.sn | PC | Master2 |

### 6. Cours (4 enregistrements)

| Titre | Module | Professeur | Filière | Niveau | Semestre |
|-------|--------|------------|---------|--------|----------|
| Programmation Java | INF101 | Diop Amadou | D2A | Licence1 | 1 |
| Algèbre Avancée | MAT201 | Ndiaye Fatou | MPCI | Licence2 | 1 |
| Physique Quantique | PHY301 | Fall Cheikh | PC | Licence3 | 2 |
| Algorithmes et Structures de Données | INF202 | Diop Amadou | SRT | Licence2 | 2 |

### 7. Notes (5 enregistrements)

| Étudiant | Cours | Valeur | Type | Semestre |
|----------|-------|--------|------|----------|
| Sow Moussa | Programmation Java | 15.50 | CC | 1 |
| Sow Moussa | Programmation Java | 14.00 | TP | 1 |
| Sow Moussa | Programmation Java | 16.50 | Examen | 1 |
| Ba Aminata | Algorithmes et Structures de Données | 13.00 | CC | 2 |
| Diagne Ibrahima | Algèbre Avancée | 17.50 | Examen | 1 |

### 8. Paiements (4 enregistrements)

| Étudiant | Type | Montant | Méthode | Statut | Année |
|----------|------|---------|---------|--------|-------|
| Sow Moussa | INSCRIPTION | 50,000 FCFA | WAVE | PAYE | 2024 |
| Ba Aminata | SCOLARITE | 150,000 FCFA | ORANGE_MONEY | PAYE | 2024 |
| Diagne Ibrahima | INSCRIPTION | 50,000 FCFA | ESPECES | EN_ATTENTE | 2024 |
| Ndiaye Mariama | SCOLARITE | 150,000 FCFA | CARTE | PAYE | 2024 |

### 9. Emploi du Temps (4 enregistrements)

| Cours | Salle | Jour | Heure Début | Heure Fin | Semestre |
|-------|-------|------|-------------|-----------|----------|
| Programmation Java | A101 | Lundi | 08:00 | 10:00 | 1 |
| Algèbre Avancée | B205 | Mercredi | 10:00 | 12:00 | 1 |
| Physique Quantique | C301 | Vendredi | 14:00 | 16:00 | 2 |
| Algorithmes et Structures de Données | D102 | Mardi | 09:00 | 11:00 | 2 |

### 10. Notifications (4 enregistrements)

| Étudiant | Titre | Type | Canal | Statut | Lu |
|----------|-------|------|-------|--------|-----|
| Sow Moussa | Note publiée | NOTE | EMAIL | ENVOYE | Non |
| Ba Aminata | Paiement reçu | PAIEMENT | SMS | ENVOYE | Non |
| Diagne Ibrahima | Cours annulé | COURS | EMAIL | ENVOYE | Oui |
| Ndiaye Mariama | Rappel inscription | GENERAL | EMAIL | EN_ATTENTE | Non |

## Instructions de Test

### 1. Charger les données de test
```bash
POST http://localhost:8082/api/v1/test/load-data
```

### 2. Vérifier les données insérées

#### Vérifier les étudiants
```bash
GET http://localhost:8082/api/v1/students
```

#### Vérifier les professeurs
```bash
GET http://localhost:8082/api/v1/professeurs
```

#### Vérifier les modules
```bash
GET http://localhost:8082/api/v1/modules
```

#### Vérifier les salles
```bash
GET http://localhost:8082/api/v1/salles
```

#### Vérifier les cours
```bash
GET http://localhost:8082/api/v1/cours
```

#### Vérifier les notes
```bash
GET http://localhost:8082/api/v1/notes
```

#### Vérifier les paiements
```bash
GET http://localhost:8082/api/v1/paiements
```

#### Vérifier l'emploi du temps
```bash
GET http://localhost:8082/api/v1/emploi-du-temps
```

#### Vérifier les notifications
```bash
GET http://localhost:8082/api/v1/notifications
```

## Notes Importantes

- L'endpoint `/api/v1/test/load-data` supprime d'abord toutes les données existantes avant d'insérer les nouvelles données de test
- Les données sont cohérentes et respectent les contraintes d'intégrité référentielle
- Les mots de passe sont hashés avec BCrypt (mot de passe: `admin123`)
- Les données couvrent différents scénarios: différents niveaux, filières, types de paiements, etc.
