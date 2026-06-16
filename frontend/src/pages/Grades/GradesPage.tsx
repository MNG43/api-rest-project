import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardBody } from '../../components/ui/Card';
import Button from '../../components/ui/Button';
import Input from '../../components/ui/Input';
import Table from '../../components/ui/Table';
import Modal from '../../components/ui/Modal';
import { FileText, Plus, Search, Edit, Trash2, Filter, Calculator, TrendingUp } from 'lucide-react';
import { GradeService, Grade } from '../../services/GradeService';

const GradesPage: React.FC = () => {
  const [grades, setGrades] = useState<Grade[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [selectedGrade, setSelectedGrade] = useState<Grade | null>(null);
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState({
    etudiantId: '',
    coursId: '',
    note: '',
    coefficient: '',
    semestre: '',
    annee: '',
  });

  useEffect(() => {
    fetchGrades();
  }, []);

  const fetchGrades = async () => {
    setLoading(true);
    try {
      const data = await GradeService.getAll();
      setGrades(data);
    } catch (error) {
      console.error('Erreur lors de la récupération des notes:', error);
      setGrades([]);
    } finally {
      setLoading(false);
    }
  };

  const calculateAverage = async (studentId: number) => {
    try {
      return await GradeService.calculateAverage(studentId);
    } catch (error) {
      console.error('Erreur lors du calcul de la moyenne:', error);
      return 0;
    }
  };

  const handleCreate = () => {
    setIsEditMode(false);
    setSelectedGrade(null);
    setFormData({
      etudiantId: '',
      coursId: '',
      note: '',
      coefficient: '',
      semestre: '',
      annee: '',
    });
    setIsModalOpen(true);
  };

  const handleEdit = (grade: Grade) => {
    setIsEditMode(true);
    setSelectedGrade(grade);
    setFormData({
      etudiantId: String(grade.etudiantId),
      coursId: String(grade.coursId),
      note: String(grade.note),
      coefficient: String(grade.coefficient),
      semestre: grade.semestre || '',
      annee: grade.annee || '',
    });
    setIsModalOpen(true);
  };

  const handleDelete = (grade: Grade) => {
    setSelectedGrade(grade);
    setIsDeleteModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const noteValue = Number(formData.note);
    if (noteValue < 0 || noteValue > 20) {
      alert('La note doit être entre 0 et 20');
      return;
    }

    try {
      const gradeData = {
        etudiantId: Number(formData.etudiantId),
        coursId: Number(formData.coursId),
        note: noteValue,
        coefficient: Number(formData.coefficient),
        semestre: formData.semestre,
        annee: formData.annee,
      };
      if (isEditMode && selectedGrade && selectedGrade.id) {
        await GradeService.update(selectedGrade.id, gradeData);
        await fetchGrades();
      } else {
        await GradeService.create(gradeData);
        await fetchGrades();
      }
      setIsModalOpen(false);
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      alert('Erreur lors de la sauvegarde de la note');
    }
  };

  const confirmDelete = async () => {
    if (selectedGrade && selectedGrade.id) {
      try {
        await GradeService.delete(selectedGrade.id);
        await fetchGrades();
        setIsDeleteModalOpen(false);
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        alert('Erreur lors de la suppression de la note');
      }
    }
  };

  const filteredGrades = grades.filter(grade =>
    (grade.etudiantNom || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
    (grade.etudiantPrenom || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
    (grade.coursCode || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
    (grade.coursTitre || '').toLowerCase().includes(searchTerm.toLowerCase())
  );

  const filteredTableData = filteredGrades.map(g => ({ ...g, id: String(g.id || '') }));

  const columns = [
    { key: 'etudiantNom' as keyof Grade, header: 'Étudiant' },
    { key: 'coursCode' as keyof Grade, header: 'Cours' },
    { key: 'note' as keyof Grade, header: 'Note' },
    { key: 'coefficient' as keyof Grade, header: 'Coeff.' },
    { key: 'semestre' as keyof Grade, header: 'Semestre' },
    { key: 'annee' as keyof Grade, header: 'Année' },
    { key: 'valide' as keyof Grade, header: 'Statut' },
  ];

  // Calculate overall statistics
  const averageGrade = grades.length > 0 
    ? (grades.reduce((sum, g) => sum + g.note, 0) / grades.length).toFixed(2)
    : '0.00';
  
  const passingRate = grades.length > 0
    ? ((grades.filter(g => g.note >= 10).length / grades.length) * 100).toFixed(1)
    : '0.0';

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Gestion des Notes</h1>
          <p className="text-gray-600 mt-1">Saisie et calcul des moyennes des étudiants</p>
        </div>
        <Button variant="primary" leftIcon={<Plus className="h-5 w-5" />} onClick={handleCreate}>
          Saisir une note
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-blue-100 rounded-lg flex items-center justify-center">
                <FileText className="h-5 w-5 text-blue-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Total notes</p>
                <p className="text-2xl font-bold text-gray-900">{grades.length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-green-100 rounded-lg flex items-center justify-center">
                <Calculator className="h-5 w-5 text-green-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Moyenne générale</p>
                <p className="text-2xl font-bold text-gray-900">{averageGrade}/20</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-purple-100 rounded-lg flex items-center justify-center">
                <TrendingUp className="h-5 w-5 text-purple-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">Taux de réussite</p>
                <p className="text-2xl font-bold text-gray-900">{passingRate}%</p>
              </div>
            </div>
          </CardBody>
        </Card>
        <Card>
          <CardBody>
            <div className="flex items-center space-x-3">
              <div className="h-10 w-10 bg-orange-100 rounded-lg flex items-center justify-center">
                <FileText className="h-5 w-5 text-orange-600" />
              </div>
              <div>
                <p className="text-sm text-gray-600">En attente</p>
                <p className="text-2xl font-bold text-gray-900">{grades.filter(g => !g.valide).length}</p>
              </div>
            </div>
          </CardBody>
        </Card>
      </div>

      {/* Student Averages */}
      <Card>
        <CardHeader>
          <h3 className="text-lg font-semibold text-gray-900">Moyennes par étudiant</h3>
        </CardHeader>
        <CardBody>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {Array.from(new Set(grades.map(g => g.etudiantId))).map(studentId => {
              const studentGrades = grades.filter(g => g.etudiantId === studentId);
              const student = studentGrades[0];
              const average = calculateAverage(studentId);
              
              return (
                <div key={studentId} className="p-4 bg-gray-50 rounded-lg">
                  <p className="font-medium text-gray-900">{student.etudiantPrenom} {student.etudiantNom}</p>
                  <p className="text-2xl font-bold text-blue-600 mt-1">{average}/20</p>
                  <p className="text-sm text-gray-500">{studentGrades.length} notes</p>
                </div>
              );
            })}
          </div>
        </CardBody>
      </Card>

      {/* Table Card */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <h3 className="text-lg font-semibold text-gray-900">Liste des notes</h3>
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
                placeholder="Rechercher une note..."
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
            emptyMessage="Aucune note trouvée"
          />

          {/* Actions */}
          <div className="mt-4 flex items-center justify-between">
            <p className="text-sm text-gray-600">
              Affichage de {filteredGrades.length} sur {grades.length} notes
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
        title={isEditMode ? 'Modifier la note' : 'Saisir une note'}
        size="lg"
      >
        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="ID Étudiant"
              value={formData.etudiantId}
              onChange={(e) => setFormData({ ...formData, etudiantId: e.target.value })}
              required
            />
            <Input
              label="ID Cours"
              value={formData.coursId}
              onChange={(e) => setFormData({ ...formData, coursId: e.target.value })}
              required
            />
            <Input
              label="Note (0-20)"
              type="number"
              min="0"
              max="20"
              step="0.5"
              value={formData.note}
              onChange={(e) => setFormData({ ...formData, note: e.target.value })}
              required
            />
            <Input
              label="Coefficient"
              type="number"
              min="1"
              value={formData.coefficient}
              onChange={(e) => setFormData({ ...formData, coefficient: e.target.value })}
              required
            />
            <Input
              label="Semestre"
              value={formData.semestre}
              onChange={(e) => setFormData({ ...formData, semestre: e.target.value })}
              required
            />
            <Input
              label="Année scolaire"
              value={formData.annee}
              onChange={(e) => setFormData({ ...formData, annee: e.target.value })}
              required
              placeholder="Ex: 2025-2026"
            />
          </div>
          <div className="flex justify-end space-x-3 mt-6">
            <Button variant="secondary" onClick={() => setIsModalOpen(false)}>
              Annuler
            </Button>
            <Button type="submit" variant="primary">
              {isEditMode ? 'Modifier' : 'Saisir'}
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
            Êtes-vous sûr de vouloir supprimer la note de{' '}
            <span className="font-semibold">
              {selectedGrade?.etudiantPrenom} {selectedGrade?.etudiantNom}
            </span>
            {' '}pour le cours{' '}
            <span className="font-semibold">{selectedGrade?.coursCode}</span>
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

export default GradesPage;
