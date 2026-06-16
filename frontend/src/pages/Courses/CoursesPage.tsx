import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardBody } from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Table from '../../components/ui/Table';
import Modal from '../../components/ui/Modal';
import { BookOpen, Plus, Search, Edit, Trash2, Filter, Clock, Users } from 'lucide-react';
import { CourseService, Course } from '../../services/CourseService';

const CoursesPage: React.FC = () => {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [selectedCourse, setSelectedCourse] = useState<Course | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState({
    code: '',
    titre: '',
    description: '',
    professeurId: '',
    module: '',
    credits: '',
    heures: '',
    semestre: '',
  });

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    setLoading(true);
    try {
      const data = await CourseService.getAll();
      setCourses(data);
    } catch (error) {
      console.error('Erreur lors de la récupération des cours:', error);
      setCourses([]);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = () => {
    setIsEditMode(false);
    setSelectedCourse(null);
    setFormData({
      code: '',
      titre: '',
      description: '',
      professeurId: '',
      module: '',
      credits: '',
      heures: '',
      semestre: '',
    });
    setIsModalOpen(true);
  };

  const handleEdit = (course: Course) => {
    setIsEditMode(true);
    setSelectedCourse(course);
    setFormData({
      code: course.code,
      titre: course.titre,
      description: course.description || '',
      professeurId: String(course.professeurId || ''),
      module: course.module || '',
      credits: String(course.credits || 0),
      heures: String(course.heures || 0),
      semestre: course.semestre || '',
    });
    setIsModalOpen(true);
  };

  const handleDelete = (course: Course) => {
    setSelectedCourse(course);
    setIsDeleteModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const courseData = {
        ...formData,
        professeurId: Number(formData.professeurId),
        credits: Number(formData.credits),
        heures: Number(formData.heures),
      };
      if (isEditMode && selectedCourse && selectedCourse.id) {
        await CourseService.update(selectedCourse.id, courseData);
        await fetchCourses();
      } else {
        await CourseService.create(courseData);
        await fetchCourses();
      }
      setIsModalOpen(false);
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      alert('Erreur lors de la sauvegarde du cours');
    }
  };

  const confirmDelete = async () => {
    if (selectedCourse && selectedCourse.id) {
      try {
        await CourseService.delete(selectedCourse.id);
        await fetchCourses();
        setIsDeleteModalOpen(false);
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        alert('Erreur lors de la suppression du cours');
      }
    }
  };

  const filteredCourses = courses.filter(course =>
    course.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
    course.titre.toLowerCase().includes(searchTerm.toLowerCase()) ||
    (course.module || '').toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredTableData = filteredCourses.map(c => ({ ...c, id: String(c.id || '') }));

  const columns = [
    { key: 'code' as keyof Course, header: 'Code' },
    { key: 'titre' as keyof Course, header: 'Titre' },
    { key: 'module' as keyof Course, header: 'Module' },
    { key: 'professeurNom' as keyof Course, header: 'Professeur' },
    { key: 'credits' as keyof Course, header: 'Crédits' },
    { key: 'heures' as keyof Course, header: 'Heures' },
    { key: 'semestre' as keyof Course, header: 'Semestre' },
    { key: 'statut' as keyof Course, header: 'Statut' },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Gestion des Cours</h1>
          <p className="text-gray-600 mt-1">Gérez les cours de l'université</p>
        </div>
        <Button variant="primary" leftIcon={<Plus className="h-5 w-5" />} onClick={handleCreate}>
          Ajouter un cours
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-blue-100 rounded-lg flex items-center justify-center">
                <BookOpen className="h-5 w-5 text-blue-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Total</p>
                <p className="text-2xl font-bold text-gray-900">{courses.length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-green-100 rounded-lg flex items-center justify-center">
                <BookOpen className="h-5 w-5 text-green-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Actifs</p>
                <p className="text-2xl font-bold text-gray-900">{courses.filter(c => c.statut === 'Actif').length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-purple-100 rounded-lg flex items-center justify-center">
                <Clock className="h-5 w-5 text-purple-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Heures totales</p>
                <p className="text-2xl font-bold text-gray-900">{courses.reduce((sum, c) => sum + c.heures, 0)}</p>
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
                <p className="text-sm text-gray-600">Étudiants inscrits</p>
                <p className="text-2xl font-bold text-gray-900">320</p>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Table Card */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <h3 className="text-lg font-semibold text-gray-900">Liste des cours</h3>
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
                placeholder="Rechercher un cours..."
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
            emptyMessage="Aucun cours trouvé"
          />

          {/* Actions */}
          <div className="mt-4 flex items-center justify-between">
            <p className="text-sm text-gray-600">
              Affichage de {filteredCourses.length} sur {courses.length} cours
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
        title={isEditMode ? 'Modifier le cours' : 'Ajouter un cours'}
        size="lg"
      >
        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Code"
              value={formData.code}
              onChange={(e) => setFormData({ ...formData, code: e.target.value })}
              required
              placeholder="Ex: INF101"
            />
            <Input
              label="Titre"
              value={formData.titre}
              onChange={(e) => setFormData({ ...formData, titre: e.target.value })}
              required
            />
            <Input
              label="Module"
              value={formData.module}
              onChange={(e) => setFormData({ ...formData, module: e.target.value })}
              required
            />
            <Input
              label="Professeur ID"
              value={formData.professeurId}
              onChange={(e) => setFormData({ ...formData, professeurId: e.target.value })}
              required
            />
            <Input
              label="Crédits"
              type="number"
              value={formData.credits}
              onChange={(e) => setFormData({ ...formData, credits: e.target.value })}
              required
            />
            <Input
              label="Heures"
              type="number"
              value={formData.heures}
              onChange={(e) => setFormData({ ...formData, heures: e.target.value })}
              required
            />
            <Input
              label="Semestre"
              value={formData.semestre}
              onChange={(e) => setFormData({ ...formData, semestre: e.target.value })}
              required
            />
          </div>
          <div className="mt-4">
            <Input
              label="Description"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              fullWidth
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
            Êtes-vous sûr de vouloir supprimer le cours{' '}
            <span className="font-semibold">
              {selectedCourse?.code} - {selectedCourse?.titre}
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

export default CoursesPage;
