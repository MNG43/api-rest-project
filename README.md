# Consommateur API REST Universite

Application consommatrice du fournisseur `api_rest` :

- Backend Spring Boot sur le port `8083`
- Frontend React + Tailwind CSS sur le port `5173`
- Communication HTTP/JSON avec le fournisseur Spring Boot `api_rest` sur le port `8082`
- Aucune partie Docker

## Structure

```text
api_rest_consumer/
├── pom.xml
├── src/main/java/com/universite/consumer/
│   ├── ApplicationConsommateur.java
│   ├── config/
│   ├── controller/
│   ├── exception/
│   └── service/
├── src/main/resources/
│   ├── application.properties
│   └── static/              # build React genere par Vite
└── frontend/
    ├── src/
    ├── package.json
    ├── tailwind.config.js
    └── vite.config.js
```

## Lancement

1. Lancer le fournisseur :

```bash
cd C:\Users\ng\Downloads\api_rest
mvn spring-boot:run
```

2. Lancer le consommateur :

```bash
cd C:\Users\ng\Downloads\api_rest_consumer
mvn spring-boot:run
```

3. Lancer le frontend en developpement :

```bash
cd C:\Users\ng\Downloads\api_rest_consumer\frontend
npm install
npm run dev
```

Frontend : `http://localhost:5173`

Backend consommateur : `http://localhost:8083`

Fournisseur : `http://localhost:8082`

## Endpoints consommateur

| Methode | Endpoint | Role |
| --- | --- | --- |
| GET | `/api/v1/consumer/health` | Etat du consommateur et disponibilite du fournisseur |
| GET | `/api/v1/consumer/services` | Services exposes par le fournisseur |
| GET | `/api/v1/consumer/service/{name}` | Informations gateway enrichies |
| GET | `/api/v1/consumer/data/{resource}` | Donnees enrichies d'une ressource |
| GET | `/api/v1/consumer/data/{resource}/{id}` | Element enrichi par identifiant |
| POST | `/api/v1/consumer/data/{resource}` | Creation via le fournisseur |
| PUT | `/api/v1/consumer/data/{resource}/{id}` | Modification via le fournisseur |
| DELETE | `/api/v1/consumer/data/{resource}/{id}` | Suppression via le fournisseur |
| GET | `/api/v1/consumer/resume` | Synthese agregee des ressources |
| POST | `/api/v1/consumer/test-data` | Chargement des donnees de test du fournisseur |

Ressources disponibles : `etudiants`, `professeurs`, `modules`, `cours`, `notes`, `paiements`, `salles`, `emploi-du-temps`, `notifications`.

## Frontend

Le dashboard React/Tailwind fournit :

- indicateurs de disponibilite du fournisseur et volumes par ressource ;
- navigation par ressource universitaire ;
- tableau filtrable des donnees enrichies ;
- panneau JSON pour creer, modifier ou supprimer via le consommateur ;
- visualisation de la derniere reponse enrichie ;
- chargement des donnees de test du fournisseur.

Pour produire les fichiers statiques servis par Spring :

```bash
cd C:\Users\ng\Downloads\api_rest_consumer\frontend
npm run build
```

Le build est copie dans `src/main/resources/static`.

## Verification

```bash
cd C:\Users\ng\Downloads\api_rest_consumer
mvn test
```

```bash
cd C:\Users\ng\Downloads\api_rest_consumer\frontend
npm run lint
npm run build
```
