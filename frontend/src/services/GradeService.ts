import api from './ApiClient';

export interface Grade {
  id?: number;
  etudiantId: number;
  etudiantNom?: string;
  etudiantPrenom?: string;
  coursId: number;
  coursCode?: string;
  coursTitre?: string;
  note: number;
  coefficient: number;
  semestre: string;
  annee: string;
  dateSaisie?: string;
  valide?: boolean;
}

export const GradeService = {
  // Récupérer toutes les notes
  getAll: async (): Promise<Grade[]> => {
    const response = await api.get('/data/notes');
    return response.data.data || response.data;
  },

  // Récupérer une note par ID
  getById: async (id: number): Promise<Grade> => {
    const response = await api.get(`/data/notes/${id}`);
    return response.data;
  },

  // Créer une nouvelle note
  create: async (grade: Grade): Promise<Grade> => {
    const response = await api.post('/data/notes', grade);
    return response.data;
  },

  // Mettre à jour une note
  update: async (id: number, grade: Grade): Promise<Grade> => {
    const response = await api.put(`/data/notes/${id}`, grade);
    return response.data;
  },

  // Supprimer une note
  delete: async (id: number): Promise<void> => {
    await api.delete(`/data/notes/${id}`);
  },

  // Calculer la moyenne d'un étudiant
  calculateAverage: async (studentId: number): Promise<number> => {
    const grades = await GradeService.getAll();
    const studentGrades = grades.filter(g => g.etudiantId === studentId);
    
    if (studentGrades.length === 0) return 0;
    
    const totalWeighted = studentGrades.reduce((sum, g) => sum + (g.note * g.coefficient), 0);
    const totalCoefficient = studentGrades.reduce((sum, g) => sum + g.coefficient, 0);
    
    return totalCoefficient > 0 ? totalWeighted / totalCoefficient : 0;
  },
};
