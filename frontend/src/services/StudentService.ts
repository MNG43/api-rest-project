import api from './ApiClient';

export interface Student {
  id?: number;
  nom: string;
  prenom: string;
  email: string;
  telephone?: string;
  dateNaissance?: string;
  filiere?: string;
  annee?: string;
  statut?: string;
}

export const StudentService = {
  // Récupérer tous les étudiants
  getAll: async (): Promise<Student[]> => {
    const response = await api.get('/data/etudiants');
    return response.data.data || response.data;
  },

  // Récupérer un étudiant par ID
  getById: async (id: number): Promise<Student> => {
    const response = await api.get(`/data/etudiants/${id}`);
    return response.data;
  },

  // Créer un nouvel étudiant
  create: async (student: Student): Promise<Student> => {
    const response = await api.post('/data/etudiants', student);
    return response.data;
  },

  // Mettre à jour un étudiant
  update: async (id: number, student: Student): Promise<Student> => {
    const response = await api.put(`/data/etudiants/${id}`, student);
    return response.data;
  },

  // Supprimer un étudiant
  delete: async (id: number): Promise<void> => {
    await api.delete(`/data/etudiants/${id}`);
  },
};
