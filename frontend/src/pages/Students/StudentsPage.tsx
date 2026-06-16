import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardBody } from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Table from '../../components/ui/Table';
import Modal from '../../components/ui/Modal';
import { Users, Plus, Search, Edit, Trash2, Eye, Filter } from 'lucide-react';
import { StudentService, Student } from '../../services/StudentService';

const StudentsPage: React.FC = () => {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState<Student | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    dateNaissance: '',
    filiere: '',
    annee: '',
  });

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    setLoading(true);
    try {
      const data = await StudentService.getAll();
      setStudents(data);
    } catch (error) {
      console.error('Erreur lors de la récupération des étudiants:', error);
      setStudents([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = () => {
    setIsEditMode(false);
    setSelectedStudent(null);
    setFormData({
      nom: '',
      prenom: '',
      email: '',
      telephone: '',
      dateNaissance: '',
      filiere: '',
      annee: '',
    });
    setIsModalOpen(true);
  };

  const handleEdit = (student: Student) => {
    setIsEditMode(true);
    setSelectedStudent(student);
    setFormData({
      nom: student.nom,
      prenom: student.prenom,
      email: student.email,
      telephone: student.telephone,
      dateNaissance: student.dateNaissance,
      filiere: student.filiere,
      annee: student.annee,
    });
    setIsModalOpen(true);
  };

  const handleDelete = (student: Student) => {
    setSelectedStudent(student);
    setIsDeleteModalOpen(true);
  };

  const handleView = (student: any) => {
    // Implement view functionality
    console.log('View student:', student);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (isEditMode && selectedStudent && selectedStudent.id) {
        await StudentService.update(selectedStudent.id, formData);
        await fetchStudents();
      } else {
        await StudentService.create(formData);
        await fetchStudents();
      }
      setIsModalOpen(false);
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      alert('Erreur lors de la sauvegarde de l\'étudiant');
    }
  };

  const confirmDelete = async () => {
    if (selectedStudent && selectedStudent.id) {
      try {
        await StudentService.delete(selectedStudent.id);
        await fetchStudents();
        setIsDeleteModalOpen(false);
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        alert('Erreur lors de la suppression de l\'étudiant');
      }
    }
  };

  const filteredStudents = students.filter(student =>
    student.nom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    student.prenom.toLowerCase().includes(searchTerm.toLowerCase()) ||
    student.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredTableData = filteredStudents.map(s => ({ ...s, id: String(s.id || '') }));

  const columns = [
    { key: 'nom' as keyof Student, header: 'Nom' },
    { key: 'prenom' as keyof Student, header: 'Prénom' },
    { key: 'email' as keyof Student, header: 'Email' },
    { key: 'filiere' as keyof Student, header: 'Filière' },
    { key: 'annee' as keyof Student, header: 'Année' },
    { key: 'statut' as keyof Student, header: 'Statut' },
  ];

  // Convertir les étudiants pour le tableau avec id string
  const tableData = students.map(s => ({ ...s, id: String(s.id || '') }));

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Gestion des Étudiants</h1>
          <p className="text-gray-600 mt-1">Gérez les étudiants de l'université</p>
        </div>
        <Button variant="primary" leftIcon={<Plus className="h-5 w-5" />} onClick={handleCreate}>
          Ajouter un étudiant
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-blue-100 rounded-lg flex items-center justify-center">
                <Users className="h-5 w-5 text-blue-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Total</p>
                <p className="text-2xl font-bold text-gray-900">{students.length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-green-100 rounded-lg flex items-center justify-center">
                <Users className="h-5 w-5 text-green-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Actifs</p>
                <p className="text-2xl font-bold text-gray-900">{students.filter(s => s.statut === 'Actif').length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-purple-100 rounded-lg flex items-center justify-center">
                <Users className="h-5 w-5 text-purple-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Nouveaux</p>
                <p className="text-2xl font-bold text-gray-900">5</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-orange-100 rounded-lg flex items-center justify-center">
                <Users className="h-5 w-5 text-orange-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Diplômés</p>
                <p className="text-2xl font-bold text-gray-900">12</p>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Table Card */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <h3 className="text-lg font-semibold text-gray-900">Liste des étudiants</h3>
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
                placeholder="Rechercher un étudiant..."
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
            onRowClick={handleView}
          />

          {/* Actions */}
          <div className="mt-4 flex items-center justify-between">
            <p className="text-sm text-gray-600">
              Affichage de {filteredStudents.length} sur {students.length} étudiants
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
        title={isEditMode ? 'Modifier l\'étudiant' : 'Ajouter un étudiant'}
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
            />
            <Input
              label="Téléphone"
              value={formData.telephone}
              onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
            />
            <Input
              label="Date de naissance"
              type="date"
              value={formData.dateNaissance}
              onChange={(e) => setFormData({ ...formData, dateNaissance: e.target.value })}
              required
            />
            <Input
              label="Filière"
              value={formData.filiere}
              onChange={(e) => setFormData({ ...formData, filiere: e.target.value })}
              required
            />
            <Input
              label="Année"
              value={formData.annee}
              onChange={(e) => setFormData({ ...formData, annee: e.target.value })}
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
            Êtes-vous sûr de vouloir supprimer l'étudiant{' '}
            <span className="font-semibold">
              {selectedStudent?.prenom} {selectedStudent?.nom}
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

export default StudentsPage;
