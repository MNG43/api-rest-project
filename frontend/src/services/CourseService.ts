import api from './ApiClient';

export interface Course {
  id?: number;
  code: string;
  titre: string;
  description?: string;
  professeurId?: number;
  professeurNom?: string;
  module?: string;
  credits?: number;
  heures?: number;
  semestre?: string;
  statut?: string;
}

export const CourseService = {
  // Récupérer tous les cours
  getAll: async (): Promise<Course[]> => {
    const response = await api.get('/data/cours');
    return response.data.data || response.data;
  },

  // Récupérer un cours par ID
  getById: async (id: number): Promise<Course> => {
    const response = await api.get(`/data/cours/${id}`);
    return response.data;
  },

  // Créer un nouveau cours
  create: async (course: Course): Promise<Course> => {
    const response = await api.post('/data/cours', course);
    return response.data;
  },

  // Mettre à jour un cours
  update: async (id: number, course: Course): Promise<Course> => {
    const response = await api.put(`/data/cours/${id}`, course);
    return response.data;
  },

  // Supprimer un cours
  delete: async (id: number): Promise<void> => {
    await api.delete(`/data/cours/${id}`);
  },
};
