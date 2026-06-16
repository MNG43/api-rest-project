# Démonstration de la Communication entre Services

Ce document démontre la communication HTTP entre le service fournisseur et le service consommateur.

## Architecture

```
┌─────────────────────────┐         HTTP          ┌─────────────────────────┐
│   Service Fournisseur   │ ◄────────────────────► │   Service Consommateur  │
│   Port: 8082            │                        │   Port: 8083            │
│   Endpoint: /api        │                        │   Endpoint: /api/v1/    │
│                         │                        │   consumer/service/{name}│
└─────────────────────────┘                        └─────────────────────────┘
```

## Scénario 1: Communication Réussie

### Requête au Service Consommateur

```bash
GET http://localhost:8083/api/v1/consumer/service/etudiants
```

### Réponse du Service Consommateur (avec enrichissement)

```json
{
  "endpoint": "/api/v1/students",
  "description": "CRUD étudiants, profil, historique",
  "message": "Service etudiants disponible",
  "consumer_timestamp": "2026-06-10T06:10:09.301732400",
  "consumer_version": "1.0.0",
  "consumer_service": "API REST Consumer",
  "processing_status": "SUCCESS",
  "data_source": "HTTP call to provider service"
}
```

**Note:** Les champs ajoutés par le consommateur sont :
- `consumer_timestamp`: Horodatage du traitement
- `consumer_version`: Version du service consommateur
- `consumer_service`: Nom du service consommateur
- `processing_status`: Statut du traitement (SUCCESS)
- `data_source`: Source des données (HTTP call to provider service)

## Scénario 2: Vérification de Santé

### Requête

```bash
GET http://localhost:8083/api/v1/consumer/health
```

### Réponse (fournisseur disponible)

```json
{
  "status": "UP",
  "service": "API REST Consumer",
  "timestamp": "2026-06-10T06:10:34.675039300",
  "port": "8083",
  "provider_available": true,
  "provider_url": "http://localhost:8082"
}
```

### Réponse (fournisseur indisponible)

```json
{
  "status": "UP",
  "service": "API REST Consumer",
  "timestamp": "2026-06-10T06:10:34.675039300",
  "port": "8083",
  "provider_available": false,
  "provider_url": "http://localhost:8082"
}
```

## Scénario 3: Liste des Services Disponibles

### Requête

```bash
GET http://localhost:8083/api/v1/consumer/services
```

### Réponse (fournisseur disponible)

```json
{
  "available_services": [
    "auth",
    "etudiants",
    "professeurs",
    "modules",
    "cours",
    "notes",
    "paiements",
    "salles",
    "emploi-du-temps",
    "notifications"
  ],
  "total_services": 10,
  "consumer_timestamp": "2026-06-10T06:03:56.606736100",
  "usage_example": "GET /api/v1/consumer/service/{service_name}",
  "provider_status": "AVAILABLE"
}
```

### Réponse (fournisseur indisponible)

```json
{
  "available_services": [
    "auth",
    "etudiants",
    "professeurs",
    "modules",
    "cours",
    "notes",
    "paiements",
    "salles",
    "emploi-du-temps",
    "notifications"
  ],
  "total_services": 10,
  "consumer_timestamp": "2026-06-10T06:03:56.606736100",
  "usage_example": "GET /api/v1/consumer/service/{service_name}",
  "provider_status": "UNAVAILABLE",
  "warning": "Le service fournisseur est actuellement indisponible"
}
```

## Scénario 4: Gestion d'Erreur (Fournisseur Indisponible)

### Requête

```bash
GET http://localhost:8083/api/v1/consumer/service/etudiants
```

### Réponse (Code HTTP: 503 Service Unavailable)

```json
{
  "error": "SERVICE_UNAVAILABLE",
  "message": "Le service fournisseur est temporairement indisponible",
  "requested_service": "etudiants",
  "consumer_timestamp": "2026-06-10T06:10:09.301732400",
  "consumer_service": "API REST Consumer",
  "processing_status": "FALLBACK",
  "suggestion": "Veuillez réessayer ultérieurement"
}
```

**Note:** Le service consommateur gère gracieusement l'indisponibilité du fournisseur en renvoyant une réponse informative plutôt qu'une erreur brute.

## Scénario 5: Appel Direct au Fournisseur

### Requête

```bash
GET http://localhost:8082/api?service=professeurs
```

### Réponse du Fournisseur (sans enrichissement)

```json
{
  "message": "Service professeurs disponible",
  "endpoint": "/api/v1/professeurs",
  "description": "CRUD professeurs, spécialités"
}
```

**Comparaison:** Notez que la réponse du fournisseur ne contient pas les métadonnées ajoutées par le consommateur.

## Comparaison: Fournisseur vs Consommateur

### Service Fournisseur (Port 8082)
- Endpoint: `/api?service={name}`
- Réponse brute sans enrichissement
- Gère les données de l'université
- Ne connaît pas le consommateur

### Service Consommateur (Port 8083)
- Endpoint: `/api/v1/consumer/service/{name}`
- Appelle le fournisseur via HTTP
- Enrichit les données avec métadonnées
- Gère l'indisponibilité du fournisseur
- Ajoute timestamp, version, et statut de traitement

## Points Clés de la Communication

✅ **Ports différents:** Fournisseur (8082) et Consommateur (8083)
✅ **Communication HTTP uniquement:** Pas d'import direct de code
✅ **Format JSON:** Échange de données en JSON
✅ **Enrichissement des données:** Le consommateur ajoute des métadonnées
✅ **Gestion d'erreur:** Dégradation gracieuse si fournisseur indisponible
✅ **Indépendance:** Les deux services peuvent être déployés séparément

## Commandes de Test

```bash
# Test de santé
curl http://localhost:8083/api/v1/consumer/health

# Liste des services
curl http://localhost:8083/api/v1/consumer/services

# Test d'un service spécifique
curl http://localhost:8083/api/v1/consumer/service/etudiants
curl http://localhost:8083/api/v1/consumer/service/professeurs
curl http://localhost:8083/api/v1/consumer/service/paiements

# Test direct du fournisseur
curl http://localhost:8082/api?service=etudiants
```

## Conclusion

Cette démonstration montre que :
1. Les deux services communiquent correctement via HTTP
2. Le service consommateur enrichit les données reçues
3. La gestion d'erreur fonctionne correctement
4. Les services sont indépendants et peuvent être déployés séparément
