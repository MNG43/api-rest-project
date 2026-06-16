# 🎓 UniGestion - Frontend Application

Application frontend moderne pour la gestion universitaire avec React, TypeScript et TailwindCSS.

## 📋 Table des matières

- [Technologies](#technologies)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Démarrage](#démarrage)
- [Architecture](#architecture)
- [Fonctionnalités](#fonctionnalités)
- [Développement](#développement)

## 🔧 Technologies

- **React 19** avec TypeScript
- **Vite** pour le build et le développement
- **TailwindCSS** pour le styling
- **React Router v7** pour la navigation
- **Axios** pour les appels API
- **Lucide React** pour les icônes
- **Context API** pour la gestion d'état

## 📦 Prérequis

- **Node.js** 18+ et **npm**
- **Backend Consumer Service** running on port 8083

## 🚀 Installation

### 1. Installer les dépendances

```bash
npm install
```

### 2. Installer les dépendances manquantes

```bash
npm install react-router-dom lucide-react
```

### 3. Configurer l'API

Éditez `src/services/ApiClient.ts` et configurez l'URL de base de l'API:

```typescript
const API_BASE_URL = 'http://localhost:8083/api/v1/consumer';
```

## ⚙️ Configuration

### Variables d'environnement

Créez un fichier `.env` à la racine du projet:

```env
VITE_API_BASE_URL=http://localhost:8083/api/v1/consumer
VITE_APP_NAME=UniGestion
```

### Configuration TailwindCSS

Le fichier `tailwind.config.js` est déjà configuré. Vous pouvez personnaliser les couleurs et les thèmes selon vos besoins.

## 🎮 Démarrage

### Mode développement

```bash
npm run dev
```

L'application sera accessible sur `http://localhost:5173` (ou le port disponible suivant).

### Mode production

```bash
npm run build
npm run preview
```

## 🏗️ Architecture

```
frontend/src/
├── components/
│   ├── ui/              # Composants UI réutilisables
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   ├── Card.tsx
│   │   ├── Table.tsx
│   │   └── Modal.tsx
│   ├── layout/          # Composants de layout
│   │   ├── Sidebar.tsx
│   │   └── Topbar.tsx
│   └── common/         # Composants communs
├── pages/              # Pages de l'application
│   ├── Auth/
│   │   ├── LoginPage.tsx
│   │   └── RegisterPage.tsx
│   ├── Dashboard/
│   │   └── Dashboard.tsx
│   ├── Students/
│   │   └── StudentsPage.tsx
│   ├── Teachers/
│   │   └── TeachersPage.tsx
│   ├── Courses/
│   │   └── CoursesPage.tsx
│   ├── Grades/
│   │   └── GradesPage.tsx
│   └── Admin/
│       └── AdminPanel.tsx
├── services/           # Services API
│   └── ApiClient.ts
├── context/            # Contextes React
│   ├── AuthContext.tsx
│   └── ThemeContext.tsx
├── layouts/            # Layouts
│   └── MainLayout.tsx
├── routes/             # Configuration des routes
│   └── AppRoutes.tsx
├── hooks/              # Hooks personnalisés
├── utils/              # Utilitaires
├── lib/                # Bibliothèques externes
├── App.tsx
└── main.tsx
```

## ✨ Fonctionnalités

### Pages implémentées

1. **Dashboard** - Vue d'ensemble avec statistiques et graphiques
2. **Étudiants** - Gestion complète (CRUD) des étudiants
3. **Professeurs** - Gestion complète (CRUD) des professeurs
4. **Cours** - Gestion complète (CRUD) des cours
5. **Notes** - Saisie et calcul automatique des moyennes
6. **Authentification** - Login et Register avec JWT
7. **Admin Panel** - Gestion globale du système

### Composants UI

- **Button** - Bouton avec variants (primary, secondary, danger, success, ghost)
- **Input** - Champ de saisie avec validation
- **Card** - Carte avec header, body et footer
- **Table** - Tableau moderne avec pagination
- **Modal** - Modal responsive

### Fonctionnalités UX

- Design moderne type SaaS
- Sidebar responsive + topbar
- Mode sombre/clair (à implémenter)
- Animations légères
- Toast notifications (à implémenter)
- Loading skeletons (à implémenter)

## 💻 Développement

### Ajouter une nouvelle page

1. Créez le composant dans `src/pages/`
2. Ajoutez la route dans `src/routes/AppRoutes.tsx`
3. Ajoutez le lien dans `src/components/layout/Sidebar.tsx`

### Appeler l'API

Utilisez le service `ApiClient` dans vos services personnalisés:

```typescript
import api from '../services/ApiClient';

export const fetchData = async () => {
  const response = await api.get('/data/etudiants');
  return response.data;
};
```

### Gestion d'état

Utilisez le Context API pour la gestion d'état globale:

```typescript
import { useAuth } from '../context/AuthContext';

const { user, login, logout } = useAuth();
```

## 🔐 Authentification

### Compte de démonstration

- **Email:** admin@universite.fr
- **Mot de passe:** admin123

### JWT Token

Le token est stocké dans `localStorage` sous la clé `token`.

## 📝 Notes

- Le frontend utilise des données mockées pour le développement
- Remplacez les appels API mockés par de vrais appels au backend
- Les composants sont prêts pour l'intégration avec l'API Consumer

## 🤝 Contribution

Pour contribuer au projet :

1. Fork le projet
2. Créez une branche (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📄 Licence

Ce projet est sous licence MIT.
