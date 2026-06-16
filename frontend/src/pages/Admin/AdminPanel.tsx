import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardBody } from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import { Settings, Users, Shield, Database, Activity, Bell, Save, Trash2, Edit } from 'lucide-react';

interface SystemSettings {
  siteName: string;
  siteUrl: string;
  adminEmail: string;
  maintenanceMode: boolean;
  registrationEnabled: boolean;
}

interface User {
  id: string;
  nom: string;
  prenom: string;
  email: string;
  role: string;
  statut: string;
}

const AdminPanel: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'settings' | 'users' | 'roles' | 'logs'>('settings');
  const [settings, setSettings] = useState<SystemSettings>({
    siteName: 'UniGestion',
    siteUrl: 'http://localhost:8083',
    adminEmail: 'admin@universite.fr',
    maintenanceMode: false,
    registrationEnabled: true,
  });
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    setLoading(true);
    // Mock data - replace with actual API call
    setTimeout(() => {
      setUsers([
        { id: '1', nom: 'Admin', prenom: 'User', email: 'admin@universite.fr', role: 'ADMIN', statut: 'Actif' },
        { id: '2', nom: 'Dupont', prenom: 'Jean', email: 'jean.dupont@universite.fr', role: 'USER', statut: 'Actif' },
        { id: '3', nom: 'Martin', prenom: 'Marie', email: 'marie.martin@universite.fr', role: 'USER', statut: 'Actif' },
      ]);
      setLoading(false);
    }, 1000);
  };

  const handleSaveSettings = () => {
    // Mock API call - replace with actual API call
    console.log('Saving settings:', settings);
    alert('Paramètres sauvegardés avec succès');
  };

  const tabs = [
    { id: 'settings' as const, label: 'Paramètres', icon: Settings },
    { id: 'users' as const, label: 'Utilisateurs', icon: Users },
    { id: 'roles' as const, label: 'Rôles', icon: Shield },
    { id: 'logs' as const, label: 'Logs système', icon: Activity },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Panneau d'administration</h1>
        <p className="text-gray-600 mt-1">Gestion globale du système</p>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="flex space-x-8">
          {tabs.map((tab) => {
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`flex items-center space-x-2 py-4 px-1 border-b-2 font-medium text-sm transition-colors ${
                  activeTab === tab.id
                    ? 'border-blue-500 text-blue-600'
                    : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                }`}
              >
                <Icon className="h-4 w-4" />
                <span>{tab.label}</span>
              </button>
            );
          })}
        </nav>
      </div>

      {/* Settings Tab */}
      {activeTab === 'settings' && (
        <div className="space-y-6">
          <Card>
            <CardHeader>
              <h3 className="text-lg font-semibold text-gray-900">Paramètres généraux</h3>
            </CardHeader>
            <CardBody>
              <div className="space-y-4">
                <Input
                  label="Nom du site"
                  value={settings.siteName}
                  onChange={(e) => setSettings({ ...settings, siteName: e.target.value })}
                />
                <Input
                  label="URL du site"
                  value={settings.siteUrl}
                  onChange={(e) => setSettings({ ...settings, siteUrl: e.target.value })}
                />
                <Input
                  label="Email administrateur"
                  type="email"
                  value={settings.adminEmail}
                  onChange={(e) => setSettings({ ...settings, adminEmail: e.target.value })}
                />
                <div className="flex items-center space-x-3">
                  <input
                    type="checkbox"
                    id="maintenance"
                    checked={settings.maintenanceMode}
                    onChange={(e) => setSettings({ ...settings, maintenanceMode: e.target.checked })}
                    className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                  />
                  <label htmlFor="maintenance" className="text-sm text-gray-700">
                    Mode maintenance
                  </label>
                </div>
                <div className="flex items-center space-x-3">
                  <input
                    type="checkbox"
                    id="registration"
                    checked={settings.registrationEnabled}
                    onChange={(e) => setSettings({ ...settings, registrationEnabled: e.target.checked })}
                    className="rounded border-gray-300 text-blue-600 focus:ring-blue-500"
                  />
                  <label htmlFor="registration" className="text-sm text-gray-700">
                    Activer l'inscription
                  </label>
                </div>
              </div>
              <div className="flex justify-end mt-6">
                <Button variant="primary" leftIcon={<Save className="h-4 w-4" />} onClick={handleSaveSettings}>
                  Sauvegarder
                </Button>
              </div>
            </CardBody>
          </Card>

          <Card>
            <CardHeader>
              <h3 className="text-lg font-semibold text-gray-900">Statistiques système</h3>
            </CardHeader>
            <CardBody>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="p-4 bg-blue-50 rounded-lg">
                  <div className="flex items-center space-x-3">
                    <Database className="h-6 w-6 text-blue-600" />
                    <div>
                      <p className="text-sm text-gray-600">Base de données</p>
                      <p className="text-lg font-semibold text-gray-900">Connectée</p>
                    </div>
                  </div>
                </div>
                <div className="p-4 bg-green-50 rounded-lg">
                  <div className="flex items-center space-x-3">
                    <Activity className="h-6 w-6 text-green-600" />
                    <div>
                      <p className="text-sm text-gray-600">API Consumer</p>
                      <p className="text-lg font-semibold text-gray-900">Opérationnel</p>
                    </div>
                  </div>
                </div>
                <div className="p-4 bg-purple-50 rounded-lg">
                  <div className="flex items-center space-x-3">
                    <Bell className="h-6 w-6 text-purple-600" />
                    <div>
                      <p className="text-sm text-gray-600">Notifications</p>
                      <p className="text-lg font-semibold text-gray-900">Actives</p>
                    </div>
                  </div>
                </div>
              </div>
            </CardBody>
          </Card>
        </div>
      )}

      {/* Users Tab */}
      {activeTab === 'users' && (
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-semibold text-gray-900">Gestion des utilisateurs</h3>
              <Button variant="primary" size="sm">
                Ajouter un utilisateur
              </Button>
            </div>
          </CardHeader>
          <CardBody>
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Nom
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Email
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Rôle
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Statut
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Actions
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {users.map((user) => (
                    <tr key={user.id}>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        {user.prenom} {user.nom}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                        {user.email}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                          user.role === 'ADMIN' ? 'bg-purple-100 text-purple-800' : 'bg-blue-100 text-blue-800'
                        }`}>
                          {user.role}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${
                          user.statut === 'Actif' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                        }`}>
                          {user.statut}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                        <div className="flex items-center space-x-2">
                          <button className="text-blue-600 hover:text-blue-700">
                            <Edit className="h-4 w-4" />
                          </button>
                          <button className="text-red-600 hover:text-red-700">
                            <Trash2 className="h-4 w-4" />
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Roles Tab */}
      {activeTab === 'roles' && (
        <Card>
          <CardHeader>
            <h3 className="text-lg font-semibold text-gray-900">Gestion des rôles et permissions</h3>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="p-4 border border-gray-200 rounded-lg">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center space-x-3">
                    <Shield className="h-5 w-5 text-purple-600" />
                    <span className="font-medium text-gray-900">Administrateur</span>
                  </div>
                  <span className="text-sm text-gray-500">Accès complet</span>
                </div>
                <p className="text-sm text-gray-600">Accès à toutes les fonctionnalités du système</p>
              </div>
              <div className="p-4 border border-gray-200 rounded-lg">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center space-x-3">
                    <Users className="h-5 w-5 text-blue-600" />
                    <span className="font-medium text-gray-900">Utilisateur</span>
                  </div>
                  <span className="text-sm text-gray-500">Accès limité</span>
                </div>
                <p className="text-sm text-gray-600">Accès aux fonctionnalités de base (consultation, modification)</p>
              </div>
              <div className="p-4 border border-gray-200 rounded-lg">
                <div className="flex items-center justify-between mb-3">
                  <div className="flex items-center space-x-3">
                    <Shield className="h-5 w-5 text-green-600" />
                    <span className="font-medium text-gray-900">Professeur</span>
                  </div>
                  <span className="text-sm text-gray-500">Accès enseignant</span>
                </div>
                <p className="text-sm text-gray-600">Accès à la gestion des cours et des notes</p>
              </div>
            </div>
          </CardBody>
        </Card>
      )}

      {/* Logs Tab */}
      {activeTab === 'logs' && (
        <Card>
          <CardHeader>
            <h3 className="text-lg font-semibold text-gray-900">Logs système</h3>
          </CardHeader>
          <CardBody>
            <div className="space-y-2 font-mono text-sm">
              <div className="p-2 bg-gray-50 rounded">
                <span className="text-gray-500">[2026-06-13 11:15:22]</span>{' '}
                <span className="text-green-600">INFO</span>{' '}
                Service Consumer démarré sur le port 8083
              </div>
              <div className="p-2 bg-gray-50 rounded">
                <span className="text-gray-500">[2026-06-13 11:15:25]</span>{' '}
                <span className="text-green-600">INFO</span>{' '}
                Connexion au service fournisseur établie
              </div>
              <div className="p-2 bg-gray-50 rounded">
                <span className="text-gray-500">[2026-06-13 11:20:30]</span>{' '}
                <span className="text-blue-600">DEBUG</span>{' '}
                Requête GET /api/v1/consumer/health
              </div>
              <div className="p-2 bg-gray-50 rounded">
                <span className="text-gray-500">[2026-06-13 11:25:45]</span>{' '}
                <span className="text-yellow-600">WARN</span>{' '}
                Taux de requêtes élevé détecté
              </div>
            </div>
          </CardBody>
        </Card>
      )}
    </div>
  );
};

export default AdminPanel;
