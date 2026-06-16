import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardBody } from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Table from '../../components/ui/Table';
import Modal from '../../components/ui/Modal';
import { GraduationCap, Plus, Search, Edit, Trash2, Filter, Mail, Phone } from 'lucide-react';
import { TeacherService, Teacher } from '../../services/TeacherService';

const TeachersPage: React.FC = () => {
  const [teachers, setTeachers] = useState<Teacher[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [selectedTeacher, setSelectedTeacher] = useState<Teacher | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    specialite: '',
    departement: '',
    dateEmbauche: '',
  });

  useEffect(() => {
    fetchTeachers();
  }, []);

  const fetchTeachers = async () => {
    setLoading(true);
    try {
      const data = await TeacherService.getAll();
      setTeachers(data);
    } catch (error) {
      console.error('Erreur lors de la récupération des professeurs:', error);
      setTeachers([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = () => {
    setIsEditMode(false);
    setSelectedTeacher(null);
    setFormData({
      nom: '',
      prenom: '',
      email: '',
      telephone: '',
      specialite: '',
      departement: '',
      dateEmbauche: '',
    });
    setIsModalOpen(true);
  };

  const handleEdit = (teacher: Teacher) => {
    setIsEditMode(true);
    setSelectedTeacher(teacher);
    setFormData({
      nom: teacher.nom,
      prenom: teacher.prenom,
      email: teacher.email,
      telephone: teacher.telephone || '',
      specialite: teacher.specialite || '',
      departement: teacher.departement || '',
      dateEmbauche: teacher.dateEmbauche || '',
    });
    setIsModalOpen(true);
  };

  const handleDelete = (teacher: Teacher) => {
    setSelectedTeacher(teacher);
    setIsDeleteModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (isEditMode && selectedTeacher && selectedTeacher.id) {
        await TeacherService.update(selectedTeacher.id, formData);
        await fetchTeachers();
      } else {
        await TeacherService.create(formData);
        await fetchTeachers();
      }
      setIsModalOpen(false);
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      alert('Erreur lors de la sauvegarde du professeur');
    }
  };

  const confirmDelete = async () => {
    if (selectedTeacher && selectedTeacher.id) {
      try {
        await TeacherService.delete(selectedTeacher.id);
        await fetchTeachers();
        setIsDeleteModalOpen(false);
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        alert('Erreur lors de la suppression du professeur');
      }
    }
  };

  const filteredTeachers = teachers.filter(teacher =>
    teacher.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    teacher.prenom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    teacher.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (teacher.specialite || '').toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredTableData = filteredTeachers.map(t => ({ ...t, id: String(t.id || '') }));

  const columns = [
    { key: 'nom' as keyof Teacher, header: 'Nom' },
    { key: 'prenom' as keyof Teacher, header: 'Prénom' },
    { key: 'specialite' as keyof Teacher, header: 'Spécialité' },
    { key: 'departement' as keyof Teacher, header: 'Département' },
    { key: 'email' as keyof Teacher, header: 'Email' },
    { key: 'statut' as keyof Teacher, header: 'Statut' },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Gestion des Professeurs</h1>
          <p className="text-gray-600 mt-1">Gérez les professeurs de l'université</p>
        </div>
        <Button variant="primary" leftIcon={<Plus className="h-5 w-5" />} onClick={handleCreate}>
          Ajouter un professeur
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-blue-100 rounded-lg flex items-center justify-center">
                <GraduationCap className="h-5 w-5 text-blue-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Total</p>
                <p className="text-2xl font-bold text-gray-900">{teachers.length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-green-100 rounded-lg flex items-center justify-center">
                <GraduationCap className="h-5 w-5 text-green-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Actifs</p>
                <p className="text-2xl font-bold text-gray-900">{teachers.filter(t => t.statut === 'Actif').length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-purple-100 rounded-lg flex items-center justify-center">
                <GraduationCap className="h-5 w-5 text-purple-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Départements</p>
                <p className="text-2xl font-bold text-gray-900">5</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-orange-100 rounded-lg flex items-center justify-center">
                <GraduationCap className="h-5 w-5 text-orange-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Cours assignés</p>
                <p className="text-2xl font-bold text-gray-900">45</p>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Table Card */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <h3 className="text-lg font-semibold text-gray-900">Liste des professeurs</h3>
            <div className="flex items-center space-x-2">
              <Button variant="ghost" size="sm" leftIcon={<Filter className="h-4 w-4" />}>
                Filtrer
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardBody>
          {/* Search */}
          <div className="mb-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <Input
                placeholder="Rechercher un professeur..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
                fullWidth
              />
            </div>
          </div>

          {/* Table */}
          <Table
            columns={columns}
            data={filteredTableData}
            loading={loading}
            emptyMessage="Aucun professeur trouvé"
          />

          {/* Actions */}
          <div className="mt-4 flex items-center justify-between">
            <p className="text-sm text-gray-600">
              Affichage de {filteredTeachers.length} sur {teachers.length} professeurs
            </p>
            <div className="flex items-center space-x-2">
              <Button variant="ghost" size="sm" disabled>
                Précédent
              </Button>
              <Button variant="ghost" size="sm" disabled>
                Suivant
              </Button>
            </div>
          </div>
        </CardBody>
      </Card>

      {/* Create/Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={isEditMode ? 'Modifier le professeur' : 'Ajouter un professeur'}
        size="lg"
      >
        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Nom"
              value={formData.nom}
              onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
              required
            />
            <Input
              label="Prénom"
              value={formData.prenom}
              onChange={(e) => setFormData({ ...formData, prenom: e.target.value })}
              required
            />
            <Input
              label="Email"
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              required
              leftIcon={<Mail className="h-4 w-4" />}
            />
            <Input
              label="Téléphone"
              value={formData.telephone}
              onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
              leftIcon={<Phone className="h-4 w-4" />}
            />
            <Input
              label="Spécialité"
              value={formData.specialite}
              onChange={(e) => setFormData({ ...formData, specialite: e.target.value })}
              required
            />
            <Input
              label="Département"
              value={formData.departement}
              onChange={(e) => setFormData({ ...formData, departement: e.target.value })}
              required
            />
            <Input
              label="Date d'embauche"
              type="date"
              value={formData.dateEmbauche}
              onChange={(e) => setFormData({ ...formData, dateEmbauche: e.target.value })}
              required
            />
          </div>
          <div className="flex justify-end space-x-3 mt-6">
            <Button variant="secondary" onClick={() => setIsModalOpen(false)}>
              Annuler
            </Button>
            <Button type="submit" variant="primary">
              {isEditMode ? 'Modifier' : 'Ajouter'}
            </Button>
          </div>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={isDeleteModalOpen}
        onClose={() => setIsDeleteModalOpen(false)}
        title="Confirmer la suppression"
        size="sm"
      >
        <div className="space-y-4">
          <p className="text-gray-700">
            Êtes-vous sûr de vouloir supprimer le professeur{' '}
            <span className="font-semibold">
              {selectedTeacher?.prenom} {selectedTeacher?.nom}
            </span>
            ?
          </p>
          <p className="text-sm text-gray-500">Cette action est irréversible.</p>
          <div className="flex justify-end space-x-3">
            <Button variant="secondary" onClick={() => setIsDeleteModalOpen(false)}>
              Annuler
            </Button>
            <Button variant="danger" onClick={confirmDelete}>
              Supprimer
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default TeachersPage;
