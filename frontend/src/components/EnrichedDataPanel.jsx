import { useCallback, useEffect, useState } from 'react'
import { getEnrichedData, getUniversitySummary } from '../api/consumerApi'

const RESOURCES = [
  'etudiants',
  'professeurs',
  'modules',
  'cours',
  'notes',
  'paiements',
  'salles',
  'emploi-du-temps',
  'notifications',
]

function EnrichedDataPanel() {
  const [selectedResource, setSelectedResource] = useState('etudiants')
  const [summary, setSummary] = useState(null)
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const loadSummary = useCallback(async () => {
    try {
      const { data: summaryData } = await getUniversitySummary()
      setSummary(summaryData)
    } catch {
      setSummary(null)
    }
  }, [])

  const loadData = useCallback(async (resourceName) => {
    setLoading(true)
    setError(null)
    setData(null)

    try {
      const result = await getEnrichedData(resourceName)
      setData(result)
    } catch {
      setError('Impossible de charger les données enrichies.')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    loadSummary()
  }, [loadSummary])

  useEffect(() => {
    loadData(selectedResource)
  }, [loadData, selectedResource])

  return (
    <section className="panel enriched-panel">
      <div className="panel-header">
        <div>
          <h2>Données enrichies</h2>
          <p>Appel HTTP au fournisseur, traitement côté consommateur, exposition du résultat</p>
        </div>
      </div>

      {summary && (
        <div className="summary-grid">
          <div className="summary-card">
            <span>Ressources</span>
            <strong>{summary.total_ressources}</strong>
          </div>
          <div className="summary-card">
            <span>Enregistrements</span>
            <strong>{summary.total_enregistrements}</strong>
          </div>
          <div className="summary-card">
            <span>Statut</span>
            <strong>{summary.processing_status}</strong>
          </div>
        </div>
      )}

      <div className="resource-tabs">
        {RESOURCES.map((resource) => (
          <button
            key={resource}
            type="button"
            className={resource === selectedResource ? 'tab active' : 'tab'}
            onClick={() => setSelectedResource(resource)}
          >
            {resource}
          </button>
        ))}
      </div>

      {loading && <p className="panel-message">Chargement des données enrichies...</p>}
      {error && <p className="panel-error">{error}</p>}

      {data?.data && (
        <pre className="json-view">{JSON.stringify(data.data, null, 2)}</pre>
      )}
    </section>
  )
}

export default EnrichedDataPanel
