import api from './ApiClient';

export interface Teacher {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  telephone?: string;
  specialite?: string;
  departement?: string;
  dateEmbauche?: string;
  statut?: string;
}

export const TeacherService = {
  // Récupérer tous les professeurs
  getAll: async (): Promise<Teacher[]> => {
    const response = await api.get('/data/professeurs');
    return response.data.data || response.data;
  },

  // Récupérer un professeur par ID
  getById: async (id: number): Promise<Teacher> => {
    const response = await api.get(`/data/professeurs/${id}`);
    return response.data;
  },

  // Créer un nouveau professeur
  create: async (teacher: Teacher): Promise<Teacher> => {
    const response = await api.post('/data/professeurs', teacher);
    return response.data;
  },

  // Mettre à jour un professeur
  update: async (id: number, teacher: Teacher): Promise<Teacher> => {
    const response = await api.put(`/data/professeurs/${id}`, teacher);
    return response.data;
  },

  // Supprimer un professeur
  delete: async (id: number): Promise<void> => {
    await api.delete(`/data/professeurs/${id}`);
  },
};
