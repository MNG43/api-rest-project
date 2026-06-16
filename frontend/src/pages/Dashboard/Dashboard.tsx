import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardBody } from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import { Users, GraduationCap, BookOpen, FileText, CreditCard, TrendingUp, Activity, Calendar } from 'lucide-react';

interface StatCard {
  title: string;
  value: string | number;
  change: string;
  changeType: 'positive' | 'negative';
  icon: React.ElementType;
  color: string;
}

const Dashboard: React.FC = () => {
  const [stats, setStats] = useState<StatCard[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulate API call
    setTimeout(() => {
      setStats([
        {
          title: 'Étudiants',
          value: 150,
          change: '+12%',
          changeType: 'positive',
          icon: Users,
          color: 'blue',
        },
        {
          title: 'Professeurs',
          value: 25,
          change: '+5%',
          changeType: 'positive',
          icon: GraduationCap,
          color: 'green',
        },
        {
          title: 'Cours',
          value: 50,
          change: '+8%',
          changeType: 'positive',
          icon: BookOpen,
          color: 'purple',
        },
        {
          title: 'Notes',
          value: 100,
          change: '+15%',
          changeType: 'positive',
          icon: FileText,
          color: 'orange',
        },
      ]);
      setLoading(false);
    }, 1000);
  }, []);

  const recentActivities = [
    { id: 1, action: 'Nouvel étudiant inscrit', time: 'Il y a 5 minutes', type: 'student' },
    { id: 2, action: 'Cours modifié: Mathématiques', time: 'Il y a 1 heure', type: 'course' },
    { id: 3, action: 'Note saisie: Jean Dupont', time: 'Il y a 2 heures', type: 'grade' },
    { id: 4, action: 'Paiement reçu: #1234', time: 'Il y a 3 heures', type: 'payment' },
  ];

  const upcomingEvents = [
    { id: 1, title: 'Conseil de département', date: 'Demain, 14:00', location: 'Salle A101' },
    { id: 2, title: 'Examens finaux', date: '15 Juin 2026', location: 'Toutes les salles' },
    { id: 3, title: 'Rentrée universitaire', date: '1 Septembre 2026', location: 'Campus' },
  ];

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Tableau de bord</h1>
        <p className="text-gray-600 mt-1">Vue d'ensemble de l'université</p>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, index) => {
          const Icon = stat.icon;
          const colorClasses = {
            blue: 'bg-blue-100 text-blue-600',
            green: 'bg-green-100 text-green-600',
            purple: 'bg-purple-100 text-purple-600',
            orange: 'bg-orange-100 text-orange-600',
          };

          return (
            <Card key={index} hover>
              <CardBody>
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm font-medium text-gray-600">{stat.title}</p>
                    <p className="text-3xl font-bold text-gray-900 mt-1">{stat.value}</p>
                    <p className={`text-sm mt-2 ${stat.changeType === 'positive' ? 'text-green-600' : 'text-red-600'}`}>
                      {stat.change}
                    </p>
                  </div>
                  <div className={`h-14 w-14 rounded-xl flex items-center justify-center ${colorClasses[stat.color as keyof typeof colorClasses]}`}>
                    <Icon className="h-7 w-7" />
                  </div>
                </div>
              </CardBody>
            </Card>
          );
        })}
      </div>

      {/* Charts and Activity Section */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Recent Activity */}
        <Card className="lg:col-span-2">
          <CardHeader>
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-semibold text-gray-900">Activité récente</h3>
              <Button variant="ghost" size="sm">Voir tout</Button>
            </div>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              {recentActivities.map((activity) => (
                <div key={activity.id} className="flex items-center space-x-4 p-3 bg-gray-50 rounded-lg">
                  <div className="h-10 w-10 bg-blue-100 rounded-full flex items-center justify-center">
                    <Activity className="h-5 w-5 text-blue-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm font-medium text-gray-900">{activity.action}</p>
                    <p className="text-xs text-gray-500">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>

        {/* Upcoming Events */}
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <h3 className="text-lg font-semibold text-gray-900">Événements à venir</h3>
              <Calendar className="h-5 w-5 text-gray-500" />
            </div>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              {upcomingEvents.map((event) => (
                <div key={event.id} className="p-3 border border-gray-200 rounded-lg hover:border-blue-300 transition-colors cursor-pointer">
                  <p className="text-sm font-medium text-gray-900">{event.title}</p>
                  <p className="text-xs text-gray-500 mt-1">{event.date}</p>
                  <p className="text-xs text-gray-400">{event.location}</p>
                </div>
              ))}
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Quick Actions */}
      <Card>
        <CardHeader>
          <h3 className="text-lg font-semibold text-gray-900">Actions rapides</h3>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <Button variant="primary" fullWidth leftIcon={<Users className="h-5 w-5" />}>
              Gérer les étudiants
            </Button>
            <Button variant="secondary" fullWidth leftIcon={<GraduationCap className="h-5 w-5" />}>
              Gérer les professeurs
            </Button>
            <Button variant="success" fullWidth leftIcon={<BookOpen className="h-5 w-5" />}>
              Gérer les cours
            </Button>
          </div>
        </CardBody>
      </Card>

      {/* Performance Overview */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <h3 className="text-lg font-semibold text-gray-900">Performance académique</h3>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-sm mb-1">
                  <span className="text-gray-600">Moyenne générale</span>
                  <span className="font-medium">14.5/20</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div className="bg-blue-600 h-2 rounded-full" style={{ width: '72%' }}></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-sm mb-1">
                  <span className="text-gray-600">Taux de réussite</span>
                  <span className="font-medium">85%</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div className="bg-green-600 h-2 rounded-full" style={{ width: '85%' }}></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-sm mb-1">
                  <span className="text-gray-600">Assiduité</span>
                  <span className="font-medium">92%</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div className="bg-purple-600 h-2 rounded-full" style={{ width: '92%' }}></div>
                </div>
              </div>
            </div>
          </CardBody>
        </Card>

        <Card>
          <CardHeader>
            <h3 className="text-lg font-semibold text-gray-900">Statistiques financières</h3>
          </CardHeader>
          <CardBody>
            <div className="space-y-4">
              <div className="flex items-center justify-between p-3 bg-green-50 rounded-lg">
                <div className="flex items-center space-x-3">
                  <CreditCard className="h-5 w-5 text-green-600" />
                  <span className="text-sm text-gray-900">Paiements reçus</span>
                </div>
                <span className="font-bold text-green-600">45 000€</span>
              </div>
              <div className="flex items-center justify-between p-3 bg-yellow-50 rounded-lg">
                <div className="flex items-center space-x-3">
                  <TrendingUp className="h-5 w-5 text-yellow-600" />
                  <span className="text-sm text-gray-900">En attente</span>
                </div>
                <span className="font-bold text-yellow-600">12 500€</span>
              </div>
              <div className="flex items-center justify-between p-3 bg-red-50 rounded-lg">
                <div className="flex items-center space-x-3">
                  <Activity className="h-5 w-5 text-red-600" />
                  <span className="text-sm text-gray-900">Retards</span>
                </div>
                <span className="font-bold text-red-600">3 200€</span>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>
    </div>
  );
};

export default Dashboard;
