# 🎓 API REST UNIVERSITÉ - Gestion Complète

## 📋 Description

Architecture microservices pour la gestion complète d'une université, composée de deux applications Spring Boot indépendantes :

- **Service Fournisseur** (Port 8082) : API REST développée en Java avec Spring Boot et AOP (Aspect-Oriented Programming). Elle expose les données de l'université via une API REST avec une séparation claire des couches (Controller, Service, Repository) et des aspects transversaux (logging, validation).

- **Service Consommateur** (Port 8083) : Application Spring Boot qui consomme l'API du service fournisseur via HTTP. Elle enrichit les données reçues et gère les cas d'indisponibilité du service fournisseur.

Les deux services communiquent exclusivement via HTTP au format JSON et tournent sur des ports différents.


## 📁 Structure du projet

### Service Fournisseur (api_rest)
```
api_rest/
├── pom.xml                                    # Configuration Maven
├── src/
│   └── main/
│       ├── java/
│       │   └── com/universite/apirest/
│       │       ├── ApiRestApplication.java    # Point d'entrée
│       │       ├── entity/                    # Entités JPA
│       │       │   ├── Etudiant.java
│       │       │   ├── Professeur.java
│       │       │   ├── Module.java
│       │       │   ├── Cours.java
│       │       │   ├── Note.java
│       │       │   ├── Paiement.java
│       │       │   ├── Salle.java
│       │       │   ├── EmploiDuTemps.java
│       │       │   ├── Notification.java
│       │       │   ├── Utilisateur.java
│       │       │   ├── Role.java
│       │       │   ├── Fillier.java
│       │       │   └── Niveau.java
│       │       ├── repository/                # Repositories
│       │       ├── service/                   # Services métier
│       │       ├── controller/                # REST Controllers
│       │       │   ├── TestDataController.java # Endpoint données test
│       │       ├── gateway/                   # API Gateway
│       │       ├── aspect/                    # AOP Aspects
│       │       ├── config/                    # Configuration
│       │       └── security/                  # Sécurité JWT
│       └── resources/
│           └── application.properties         # Configuration (Port 8082)
└── DATA_TEST.md                               # Documentation données test
```

### Service Consommateur (api_rest_consumer)
```
api_rest_consumer/
├── pom.xml                                    # Configuration Maven
├── src/
│   └── main/
│       ├── java/
│       │   └── com/universite/consumer/
│       │       ├── ConsumerApplication.java   # Point d'entrée
│       │       ├── config/                    # Configuration
│       │       │   └── ProviderServiceConfig.java
│       │       ├── service/                   # Service HTTP
│       │       │   ├── ProviderClientService.java
│       │       │   └── ProviderServiceUnavailableException.java
│       │       ├── controller/                # REST Controllers
│       │       │   └── ConsumerController.java
│       │       └── exception/                 # Gestion erreurs
│       │           └── GlobalExceptionHandler.java
│       └── resources/
│           └── application.properties         # Configuration (Port 8083)
├── README.md                                   # Documentation service consommateur
└── DEMO.md                                    # Démonstration communication
```
```

## 🚀 Fonctionnalités

### Service Fournisseur (Port 8082)

| Service | Méthodes HTTP | Endpoints | Description |
|---------|---------------|-----------|-------------|
| Étudiants | GET, POST, PUT, DELETE | `/api/v1/students` | CRUD étudiants |
| Professeurs | GET, POST, PUT, DELETE | `/api/v1/professeurs` | CRUD professeurs |
| Modules | GET, POST, PUT, DELETE | `/api/v1/modules` | CRUD modules |
| Cours | GET, POST, PUT, DELETE | `/api/v1/cours` | CRUD cours |
| Notes | GET, POST, PUT, DELETE | `/api/v1/notes` | CRUD notes |
| Paiements | GET, POST, PUT, DELETE | `/api/v1/paiements` | CRUD paiements |
| Salles | GET, POST, PUT, DELETE | `/api/v1/salles` | CRUD salles |
| Emploi du temps | GET, POST, PUT, DELETE | `/api/v1/emploi-du-temps` | CRUD emploi du temps |
| Notifications | GET, POST, PUT, DELETE | `/api/v1/notifications` | CRUD notifications |
| Authentification | POST | `/api/v1/auth` | Login, inscription, JWT |
| API Gateway | GET | `/api` | Routage vers services |
| Données de test | POST | `/api/v1/test/load-data` | Charger données de test |

### Service Consommateur (Port 8083)

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/v1/consumer/health` | GET | Vérifier santé du service |
| `/api/v1/consumer/services` | GET | Lister services disponibles |
| `/api/v1/consumer/service/{name}` | GET | Obtenir infos enrichies d'un service (gateway) |
| `/api/v1/consumer/data/{resource}` | GET | Appeler le fournisseur, enrichir les données métier et exposer le résultat |
| `/api/v1/consumer/data/{resource}/{id}` | GET | Obtenir un élément enrichi par identifiant |
| `/api/v1/consumer/resume` | GET | Synthèse agrégée de toutes les ressources universitaires |

## 📦 Prérequis

### Service Fournisseur (Spring Boot)
- **Java** : Version 17 ou supérieure
- **Maven** : Version 3.6 ou supérieure
- **MySQL** : Version 5.7 ou supérieure
- **IDE** : IntelliJ IDEA, Eclipse, ou VS Code

### Service Consommateur (Spring Boot)
- **Java** : Version 17 ou supérieure
- **Maven** : Version 3.6 ou supérieure

## 🔧 Installation

### 1. Créer la base de données

Via phpMyAdmin ou en ligne de commande MySQL :

```sql
CREATE DATABASE universite;
```

Les tables seront créées automatiquement par Hibernate (ddl-auto=update).

### 2. Configurer la connexion BDD

Modifiez `src/main/resources/application.properties` avec vos identifiants :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/universite
spring.datasource.username=root
spring.datasource.password=
```

### 3. Lancer le Service Fournisseur (Port 8082)

```bash
cd api_rest
mvn clean install
mvn spring-boot:run
```

Ou directement avec l'IDE :
- Ouvrez le projet
- Lancez la classe `ApiRestApplication.java`

Le service fournisseur démarrera sur le port **8082**.

### 4. Lancer le Service Consommateur (Port 8083)

```bash
cd api_rest_consumer
mvn clean install
mvn spring-boot:run
```

Le service consommateur démarrera sur le port **8083**.

### 5. Charger les données de test

```bash
POST http://localhost:8082/api/v1/test/load-data
```

Cela insérera 36 enregistrements de test dans toutes les tables.

## 🧪 Tests

### Charger les données de test

```bash
POST http://localhost:8082/api/v1/test/load-data
```

### Tester le Service Fournisseur

```bash
# Étudiants
GET http://localhost:8082/api/v1/students
GET http://localhost:8082/api/v1/students/1

# Professeurs
GET http://localhost:8082/api/v1/professeurs

# Modules
GET http://localhost:8082/api/v1/modules

# Cours
GET http://localhost:8082/api/v1/cours

# Notes
GET http://localhost:8082/api/v1/notes

# Paiements
GET http://localhost:8082/api/v1/paiements

# Salles
GET http://localhost:8082/api/v1/salles

# Emploi du temps
GET http://localhost:8082/api/v1/emploi-du-temps

# Notifications
GET http://localhost:8082/api/v1/notifications

# API Gateway
GET http://localhost:8082/api?service=etudiants
```

### Tester le Service Consommateur

```bash
# Vérifier la santé
GET http://localhost:8083/api/v1/consumer/health

# Lister les services disponibles
GET http://localhost:8083/api/v1/consumer/services

# Obtenir des infos enrichies
GET http://localhost:8083/api/v1/consumer/service/etudiants
GET http://localhost:8083/api/v1/consumer/service/professeurs

# Données métier enrichies (appel HTTP + traitement + exposition)
GET http://localhost:8083/api/v1/consumer/data/etudiants
GET http://localhost:8083/api/v1/consumer/data/etudiants/1
GET http://localhost:8083/api/v1/consumer/resume
```

### Communication entre Services

Le service consommateur appelle le fournisseur via HTTP et enrichit les données avec :
- Timestamp de traitement
- Version du service consommateur
- Statut de traitement (SUCCESS/FALLBACK)
- Source des données

Si le fournisseur est indisponible, le consommateur renvoie une réponse de dégradation gracieuse (503).

## 📊 Exemples de réponses

### Succès (GET all) - Code 200

```json
[
  {
    "id": 1,
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@universite.fr",
    "created_at": "2026-06-04 10:30:00"
  },
  {
    "id": 2,
    "nom": "Martin",
    "prenom": "Paul",
    "email": "paul.martin@universite.fr",
    "created_at": "2026-06-04 11:45:00"
  }
]
```

### Succès (GET one) - Code 200

```json
{
  "id": 1,
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@universite.fr",
  "created_at": "2026-06-04 10:30:00"
}
```

### Succès (POST) - Code 201

```json
{
  "message": "Étudiant créé avec succès"
}
```

### Erreur (404) - Non trouvé

```json
{
  "message": "Étudiant non trouvé"
}
```

### Erreur (405) - Méthode non autorisée

```json
{
  "message": "Méthode non autorisée",
  "allowed_methods": ["GET", "POST", "PUT", "DELETE"]
}
```

## 🔒 Codes HTTP utilisés

| Code | Signification | Utilisation |
|------|---------------|-------------|
| 200 | OK | Requête GET/PUT/DELETE réussie |
| 201 | Created | Ressource créée (POST) |
| 400 | Bad Request | Données invalides ou manquantes |
| 404 | Not Found | Ressource inexistante |
| 405 | Method Not Allowed | Méthode HTTP non supportée |
| 500 | Internal Server Error | Erreur serveur |

## 🛠️ Technologies utilisées

### Service Fournisseur (Backend)
- **Java 17** : Langage de programmation
- **Spring Boot 3.2** : Framework d'application
- **Spring Data JPA** : Accès aux données
- **Spring AOP** : Programmation orientée aspect
- **Spring Security** : Sécurité et authentification JWT
- **Spring Boot Mail** : Envoi d'emails
- **MySQL** : Base de données relationnelle
- **Maven** : Gestion des dépendances
- **Lombok** : Réduction du code boilerplate

### Service Consommateur (Backend)
- **Java 17** : Langage de programmation
- **Spring Boot 3.2** : Framework d'application
- **RestTemplate** : Client HTTP
- **Maven** : Gestion des dépendances
- **Lombok** : Réduction du code boilerplate

### Architecture
- **Microservices** : Architecture distribuée
- **REST API** : Architecture des web services
- **JSON** : Format d'échange de données
- **HTTP** : Protocole de communication entre services
- **JWT** : Authentification par token

## 📈 Évolutions possibles

- [x] Authentification JWT
- [x] Service Professeurs
- [x] Service Cours
- [x] Service Modules
- [x] Service Notes
- [x] Service Paiements
- [x] Service Salles
- [x] Service Emploi du temps
- [x] Service Notifications
- [x] Service Consommateur indépendant
- [x] Données de test
- [ ] Validation avancée des données
- [ ] Pagination des résultats
- [ ] Filtres de recherche (nom, email)
- [ ] Documentation Swagger/OpenAPI
- [ ] Rate limiting
- [ ] Tests unitaires et d'intégration
- [ ] Dockerisation des services
- [ ] CI/CD pipeline
- [ ] Frontend React/Angular/Vue

## 🤝 Contribution

Les contributions sont les bienvenues ! Merci de suivre ces étapes :

1. Forkez le projet
2. Créez votre branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Pushez sur la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 👨‍💻 Auteur

    MNG
- GitHub : MNG43
- Email : ngmoussa1919@gmail.com

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus d'informations.

## 🙏 Remerciements

- Merci à l'équipe pédagogique pour leur accompagnement
- À la communauté Spring Boot pour sa documentation
- À la communauté React pour ses ressources
- Aux contributeurs open-source

---

## 📞 Support

Pour toute question ou signalement de bug :
- Ouvrez une issue sur GitHub
- Contactez-moi par email

---

**Dernière mise à jour :** Juin 2026  
**Version :** 2.0.0  
**Statut :** ✅ Production ready

## 📚 Documentation supplémentaire

- [DATA_TEST.md](./DATA_TEST.md) - Documentation complète des données de test
- [api_rest_consumer/README.md](../api_rest_consumer/README.md) - Documentation du service consommateur
- [api_rest_consumer/DEMO.md](../api_rest_consumer/DEMO.md) - Démonstration de la communication entre services

```

## Comment ajouter ce README à votre projet dans VS Code

1. **Dans VS Code**, ouvrez votre dossier `api_rest`

2. **Créez un nouveau fichier** :
   - Cliquez sur l'icône "Nouveau fichier" dans l'explorateur
   - Ou utilisez le raccourci `Ctrl + N`

3. **Nommez le fichier** : `README.md`

4. **Copiez tout le texte ci-dessus** (`Ctrl + A` puis `Ctrl + C`)

5. **Collez dans le fichier** (`Ctrl + V`)

6. **Enregistrez** (`Ctrl + S`)

## Aperçu dans VS Code

VS Code affiche automatiquement le README formaté. Vous pouvez basculer entre :
- **Mode Édition** : Modifier le texte
- **Mode Aperçu** : `Ctrl + Shift + V` pour voir le rendu

C'est fait ! Votre projet a maintenant une documentation professionnelle complète.