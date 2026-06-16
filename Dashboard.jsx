import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Dashboard = () => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Appel au Service Consommateur (Port 8083)
        axios.get('http://localhost:8083/api/v1/consumer/data/students')
            .then(res => {
                setData(res.data);
                setLoading(false);
            })
            .catch(err => {
                setError("Impossible de joindre le service consommateur");
                setLoading(false);
            });
    }, []);

    if (loading) return <div className="flex justify-center p-10">Chargement...</div>;

    return (
        <div className="min-h-screen bg-gray-50 p-8">
            <header className="mb-8 flex justify-between items-center bg-white p-6 rounded-lg shadow-sm">
                <h1 className="text-2xl font-bold text-blue-900">🎓 Portail Université</h1>
                <div className="flex items-center space-x-2">
                    <span className={`h-3 w-3 rounded-full ${data?.metadata?.status === 'SUCCESS' ? 'bg-green-500' : 'bg-red-500'}`}></span>
                    <span className="text-sm text-gray-600">Statut Service: {data?.metadata?.status}</span>
                </div>
            </header>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* Liste des étudiants via le consommateur */}
                <div className="md:col-span-2 bg-white rounded-xl shadow-md overflow-hidden">
                    <div className="bg-blue-900 p-4">
                        <h2 className="text-white font-semibold text-lg">Liste des Étudiants</h2>
                    </div>
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-gray-100">
                                <th className="p-4 border-b">Nom & Prénom</th>
                                <th className="p-4 border-b">Filière</th>
                                <th className="p-4 border-b">Niveau</th>
                            </tr>
                        </thead>
                        <tbody>
                            {data?.content?.map((student) => (
                                <tr key={student.id} className="hover:bg-blue-50 transition-colors">
                                    <td className="p-4 border-b">{student.nom} {student.prenom}</td>
                                    <td className="p-4 border-b"><span className="px-2 py-1 bg-blue-100 text-blue-800 rounded text-xs">{student.filiere}</span></td>
                                    <td className="p-4 border-b text-gray-600">{student.niveau}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                {/* Métadonnées enrichies par le consommateur */}
                <div className="bg-white p-6 rounded-xl shadow-md border-t-4 border-amber-400">
                    <h2 className="text-lg font-bold mb-4">Infos Service</h2>
                    <ul className="space-y-3 text-sm">
                        <li className="flex justify-between"><span>Version:</span> <span className="font-mono">{data?.metadata?.version}</span></li>
                        <li className="flex justify-between"><span>Source:</span> <span className="text-blue-600">{data?.metadata?.source}</span></li>
                        <li className="flex justify-between"><span>Traitement:</span> <span className="text-gray-500">{new Date(data?.metadata?.timestamp).toLocaleTimeString()}</span></li>
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;