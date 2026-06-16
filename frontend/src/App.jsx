import { useCallback, useEffect, useMemo, useState } from 'react'
import {
  Activity,
  AlertTriangle,
  Bell,
  BookOpen,
  Building2,
  CalendarDays,
  CheckCircle2,
  ClipboardList,
  CreditCard,
  Database,
  Eye,
  GraduationCap,
  Loader2,
  Plus,
  RefreshCw,
  Save,
  Search,
  Server,
  Trash2,
  Upload,
  Users,
  Wifi,
  WifiOff,
} from 'lucide-react'
import {
  createResource,
  deleteResource,
  getEnrichedData,
  getHealth,
  getServices,
  getUniversitySummary,
  loadTestData,
  updateResource,
} from './api/consumerApi'

const RESOURCE_CONFIG = [
  {
    key: 'etudiants',
    label: 'Etudiants',
    icon: Users,
    tone: 'emerald',
    columns: ['prenom', 'nom', 'filiere', 'niveau', 'email'],
  },
  {
    key: 'professeurs',
    label: 'Professeurs',
    icon: GraduationCap,
    tone: 'indigo',
    columns: ['prenom', 'nom', 'specialite', 'grade', 'email'],
  },
  {
    key: 'modules',
    label: 'Modules',
    icon: BookOpen,
    tone: 'amber',
    columns: ['code', 'nom', 'credit', 'volumeHoraire'],
  },
  {
    key: 'cours',
    label: 'Cours',
    icon: ClipboardList,
    tone: 'sky',
    columns: ['titre', 'module.nom', 'professeur.nom', 'filiere', 'niveau', 'semestre'],
  },
  {
    key: 'notes',
    label: 'Notes',
    icon: Activity,
    tone: 'rose',
    columns: ['etudiant.nom', 'cours.titre', 'valeur', 'typeNote', 'semestre'],
  },
  {
    key: 'paiements',
    label: 'Paiements',
    icon: CreditCard,
    tone: 'teal',
    columns: ['etudiant.nom', 'type', 'montant', 'statut', 'methodePaiement'],
  },
  {
    key: 'salles',
    label: 'Salles',
    icon: Building2,
    tone: 'cyan',
    columns: ['code', 'nom', 'batiment', 'capacite', 'type', 'equipementVideo'],
  },
  {
    key: 'emploi-du-temps',
    label: 'Planning',
    icon: CalendarDays,
    tone: 'violet',
    columns: ['jour', 'heureDebut', 'heureFin', 'cours.titre', 'salle.code', 'semestre'],
  },
  {
    key: 'notifications',
    label: 'Notifications',
    icon: Bell,
    tone: 'orange',
    columns: ['titre', 'etudiant.nom', 'type', 'canal', 'lu', 'statut'],
  },
]

const RESOURCE_TEMPLATES = {
  etudiants: {
    nom: 'Sarr',
    prenom: 'Awa',
    email: 'awa.sarr@uadb.sn',
    sexe: 'F',
    dateNaissance: '2001-04-12',
    lieuNaissance: 'Dakar',
    nationalite: 'Senegalaise',
    telephone: '776000001',
    adresse: 'Dakar, Senegal',
    filiere: 'D2A',
    niveau: 'Licence1',
  },
  professeurs: {
    nom: 'Gueye',
    prenom: 'Mamadou',
    email: 'mamadou.gueye@uadb.sn',
    telephone: '776000002',
    specialite: 'Genie logiciel',
    grade: 'Assistant',
  },
  modules: {
    code: 'SOA401',
    nom: 'Architecture SOA',
    description: 'Services REST, consommateurs HTTP et integration JSON',
    credit: 6,
    volumeHoraire: 45,
  },
  cours: {
    titre: 'Services Web REST',
    description: 'Consommation de services avec Spring Boot',
    module: { id: 1 },
    professeur: { id: 1 },
    filiere: 'D2A',
    niveau: 'Licence3',
    semestre: 2,
  },
  notes: {
    etudiant: { id: 1 },
    cours: { id: 1 },
    valeur: 15.5,
    typeNote: 'Examen',
    semestre: 1,
  },
  paiements: {
    etudiant: { id: 1 },
    type: 'SCOLARITE',
    montant: 150000,
    methodePaiement: 'WAVE',
    statut: 'EN_ATTENTE',
    anneeAcademique: 2024,
  },
  salles: {
    code: 'E210',
    nom: 'Salle E210',
    batiment: 'E',
    capacite: 60,
    type: 'TD',
    equipementVideo: true,
  },
  'emploi-du-temps': {
    cours: { id: 1 },
    salle: { id: 1 },
    jour: 'MONDAY',
    heureDebut: '08:00',
    heureFin: '10:00',
    semestre: 1,
    anneeAcademique: 2024,
  },
  notifications: {
    etudiant: { id: 1 },
    titre: 'Information importante',
    message: 'Votre espace etudiant a ete mis a jour',
    type: 'GENERAL',
    canal: 'EMAIL',
    lu: false,
    statut: 'EN_ATTENTE',
  },
}

const METADATA_KEYS = new Set([
  'consumer_timestamp',
  'consumer_service',
  'consumer_version',
  'processing_status',
  'data_source',
  'traite_par',
  'nom_complet',
  'domaine_email',
  'resource',
])

const toneClasses = {
  emerald: 'bg-emerald-50 text-emerald-700 border-emerald-200',
  indigo: 'bg-indigo-50 text-indigo-700 border-indigo-200',
  amber: 'bg-amber-50 text-amber-700 border-amber-200',
  sky: 'bg-sky-50 text-sky-700 border-sky-200',
  rose: 'bg-rose-50 text-rose-700 border-rose-200',
  teal: 'bg-teal-50 text-teal-700 border-teal-200',
  cyan: 'bg-cyan-50 text-cyan-700 border-cyan-200',
  violet: 'bg-violet-50 text-violet-700 border-violet-200',
  orange: 'bg-orange-50 text-orange-700 border-orange-200',
}

function readPath(source, path) {
  return path.split('.').reduce((value, segment) => {
    if (value === null || value === undefined) return undefined
    return value[segment]
  }, source)
}

function formatValue(value) {
  if (value === null || value === undefined || value === '') return '...'
  if (typeof value === 'boolean') return value ? 'Oui' : 'Non'
  if (Array.isArray(value)) return `${value.length} element(s)`
  if (typeof value === 'object') {
    if (value.prenom || value.nom) return [value.prenom, value.nom].filter(Boolean).join(' ')
    if (value.titre) return value.titre
    if (value.code) return value.code
    if (value.id) return `#${value.id}`
    return JSON.stringify(value)
  }
  return String(value)
}

function cleanPayload(value) {
  if (Array.isArray(value)) {
    return value.map(cleanPayload)
  }

  if (value && typeof value === 'object') {
    return Object.entries(value).reduce((result, [key, entry]) => {
      if (METADATA_KEYS.has(key) || key === 'createdAt') return result
      result[key] = cleanPayload(entry)
      return result
    }, {})
  }

  return value
}

function getErrorMessage(data, fallback) {
  if (!data) return fallback
  if (typeof data === 'string') return data
  return data.message || data.error || fallback
}

function App() {
  const [health, setHealth] = useState(null)
  const [services, setServices] = useState(null)
  const [summary, setSummary] = useState(null)
  const [overviewLoading, setOverviewLoading] = useState(false)
  const [overviewError, setOverviewError] = useState('')

  const [selectedResource, setSelectedResource] = useState('etudiants')
  const [resourceData, setResourceData] = useState(null)
  const [resourceLoading, setResourceLoading] = useState(false)
  const [resourceError, setResourceError] = useState('')
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedRow, setSelectedRow] = useState(null)

  const [editorValue, setEditorValue] = useState(JSON.stringify(RESOURCE_TEMPLATES.etudiants, null, 2))
  const [mutationLoading, setMutationLoading] = useState(false)
  const [mutationResult, setMutationResult] = useState(null)
  const [mutationError, setMutationError] = useState('')

  const selectedConfig = useMemo(
    () => RESOURCE_CONFIG.find((resource) => resource.key === selectedResource),
    [selectedResource],
  )

  const items = useMemo(() => {
    if (!Array.isArray(resourceData?.items)) return []
    return resourceData.items
  }, [resourceData])

  const filteredItems = useMemo(() => {
    const needle = searchTerm.trim().toLowerCase()
    if (!needle) return items
    return items.filter((item) => JSON.stringify(item).toLowerCase().includes(needle))
  }, [items, searchTerm])

  const counts = summary?.comptes_par_ressource || {}
  const providerAvailable = health?.provider_available === true || services?.provider_status === 'AVAILABLE'
  const selectedCount = counts[selectedResource] ?? resourceData?.total ?? items.length

  const loadOverview = useCallback(async () => {
    setOverviewLoading(true)
    setOverviewError('')

    try {
      const [healthResult, servicesResult, summaryResult] = await Promise.all([
        getHealth(),
        getServices(),
        getUniversitySummary(),
      ])

      setHealth(healthResult.data)
      setServices(servicesResult.data)
      setSummary(summaryResult.ok ? summaryResult.data : null)

      if (!healthResult.ok || !servicesResult.ok) {
        setOverviewError('Etat partiel du consommateur')
      }
    } catch {
      setOverviewError('Consommateur indisponible sur le port 8083')
    } finally {
      setOverviewLoading(false)
    }
  }, [])

  const loadResource = useCallback(async (resourceName) => {
    setResourceLoading(true)
    setResourceError('')

    try {
      const result = await getEnrichedData(resourceName)
      setResourceData(result.data)
      if (!result.ok) {
        setResourceError(getErrorMessage(result.data, 'Ressource indisponible'))
      }
    } catch {
      setResourceData(null)
      setResourceError('Impossible de charger la ressource')
    } finally {
      setResourceLoading(false)
    }
  }, [])

  const refreshAll = useCallback(() => {
    loadOverview()
    loadResource(selectedResource)
  }, [loadOverview, loadResource, selectedResource])

  useEffect(() => {
    loadOverview()
  }, [loadOverview])

  useEffect(() => {
    setSelectedRow(null)
    setMutationResult(null)
    setMutationError('')
    setEditorValue(JSON.stringify(RESOURCE_TEMPLATES[selectedResource] || {}, null, 2))
    loadResource(selectedResource)
  }, [loadResource, selectedResource])

  function handleResourceSelect(resourceName) {
    setSelectedResource(resourceName)
    setSearchTerm('')
  }

  function handleRowSelect(row) {
    setSelectedRow(row)
    setMutationResult(null)
    setMutationError('')
    setEditorValue(JSON.stringify(cleanPayload(row), null, 2))
  }

  function resetEditor() {
    setSelectedRow(null)
    setMutationResult(null)
    setMutationError('')
    setEditorValue(JSON.stringify(RESOURCE_TEMPLATES[selectedResource] || {}, null, 2))
  }

  async function handleMutation(mode) {
    setMutationLoading(true)
    setMutationError('')
    setMutationResult(null)

    try {
      let result

      if (mode === 'delete') {
        if (!selectedRow?.id) {
          setMutationError('Selection requise')
          return
        }
        result = await deleteResource(selectedResource, selectedRow.id)
      } else {
        let payload
        try {
          payload = JSON.parse(editorValue)
        } catch {
          setMutationError('JSON invalide')
          return
        }

        if (mode === 'update') {
          if (!selectedRow?.id) {
            setMutationError('Selection requise')
            return
          }
          result = await updateResource(selectedResource, selectedRow.id, payload)
        } else {
          result = await createResource(selectedResource, payload)
        }
      }

      setMutationResult(result.data)
      if (!result.ok) {
        setMutationError(getErrorMessage(result.data, 'Operation refusee par le fournisseur'))
        return
      }

      await loadResource(selectedResource)
      await loadOverview()
    } catch {
      setMutationError('Operation impossible')
    } finally {
      setMutationLoading(false)
    }
  }

  async function handleSeedData() {
    setMutationLoading(true)
    setMutationError('')
    setMutationResult(null)

    try {
      const result = await loadTestData()
      setMutationResult(result.data)
      if (!result.ok) {
        setMutationError(getErrorMessage(result.data, 'Chargement refuse par le fournisseur'))
        return
      }
      await loadResource(selectedResource)
      await loadOverview()
    } catch {
      setMutationError('Chargement impossible')
    } finally {
      setMutationLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#f4f6f8] text-slate-900">
      <div className="flex min-h-screen">
        <aside className="hidden w-72 shrink-0 border-r border-slate-200 bg-white lg:block">
          <div className="flex h-16 items-center gap-3 border-b border-slate-200 px-5">
            <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-emerald-600 text-sm font-bold text-white">
              UC
            </div>
            <div>
              <p className="text-sm font-semibold text-slate-950">Universite Consumer</p>
              <p className="text-xs text-slate-500">Spring 8083 - React 5173</p>
            </div>
          </div>

          <nav className="space-y-1 px-3 py-4">
            {RESOURCE_CONFIG.map((resource) => {
              const Icon = resource.icon
              const selected = selectedResource === resource.key
              return (
                <button
                  key={resource.key}
                  type="button"
                  onClick={() => handleResourceSelect(resource.key)}
                  className={`flex w-full items-center justify-between rounded-lg px-3 py-2 text-left text-sm transition ${
                    selected
                      ? 'bg-slate-900 text-white'
                      : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950'
                  }`}
                >
                  <span className="flex min-w-0 items-center gap-3">
                    <Icon className="h-4 w-4 shrink-0" aria-hidden="true" />
                    <span className="truncate font-medium">{resource.label}</span>
                  </span>
                  <span className={`rounded-md px-2 py-0.5 text-xs ${selected ? 'bg-white/15' : 'bg-slate-100'}`}>
                    {counts[resource.key] ?? '-'}
                  </span>
                </button>
              )
            })}
          </nav>
        </aside>

        <main className="min-w-0 flex-1">
          <header className="sticky top-0 z-20 border-b border-slate-200 bg-white/95 backdrop-blur">
            <div className="flex min-h-16 flex-col gap-3 px-4 py-3 lg:flex-row lg:items-center lg:justify-between lg:px-6">
              <div className="min-w-0">
                <div className="flex items-center gap-2 text-xs font-medium uppercase tracking-wide text-slate-500">
                  <Server className="h-4 w-4" aria-hidden="true" />
                  Fournisseur api_rest
                </div>
                <h1 className="mt-1 truncate text-xl font-semibold text-slate-950">
                  Consommateur de services universitaires
                </h1>
              </div>

              <div className="flex flex-wrap items-center gap-2">
                <StatusBadge available={providerAvailable} />
                <button
                  type="button"
                  onClick={handleSeedData}
                  disabled={mutationLoading}
                  className="inline-flex h-9 items-center gap-2 rounded-lg border border-slate-300 bg-white px-3 text-sm font-medium text-slate-700 hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-60"
                >
                  <Upload className="h-4 w-4" aria-hidden="true" />
                  Donnees test
                </button>
                <button
                  type="button"
                  onClick={refreshAll}
                  disabled={overviewLoading || resourceLoading}
                  className="inline-flex h-9 items-center gap-2 rounded-lg bg-slate-900 px-3 text-sm font-medium text-white hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
                >
                  <RefreshCw
                    className={`h-4 w-4 ${overviewLoading || resourceLoading ? 'animate-spin' : ''}`}
                    aria-hidden="true"
                  />
                  Actualiser
                </button>
              </div>
            </div>
          </header>

          <div className="space-y-5 p-4 lg:p-6">
            {overviewError ? (
              <AlertBanner message={overviewError} />
            ) : null}

            <section className="grid gap-3 sm:grid-cols-2 xl:grid-cols-4">
              <MetricCard
                icon={Database}
                label="Enregistrements"
                value={summary?.total_enregistrements ?? '...'}
                accent="text-emerald-700"
              />
              <MetricCard
                icon={ClipboardList}
                label="Ressources"
                value={summary?.total_ressources ?? RESOURCE_CONFIG.length}
                accent="text-indigo-700"
              />
              <MetricCard
                icon={selectedConfig?.icon || Database}
                label={selectedConfig?.label || 'Selection'}
                value={selectedCount}
                accent="text-amber-700"
              />
              <MetricCard
                icon={providerAvailable ? Wifi : WifiOff}
                label="Fournisseur"
                value={providerAvailable ? 'Disponible' : 'Indisponible'}
                accent={providerAvailable ? 'text-teal-700' : 'text-rose-700'}
              />
            </section>

            <section className="grid gap-5 xl:grid-cols-[minmax(0,1fr)_410px]">
              <div className="min-w-0 rounded-lg border border-slate-200 bg-white">
                <div className="flex flex-col gap-3 border-b border-slate-200 p-4 lg:flex-row lg:items-center lg:justify-between">
                  <div className="min-w-0">
                    <div className="flex items-center gap-2">
                      {selectedConfig ? (
                        <span className={`inline-flex h-8 w-8 items-center justify-center rounded-lg border ${toneClasses[selectedConfig.tone]}`}>
                          <selectedConfig.icon className="h-4 w-4" aria-hidden="true" />
                        </span>
                      ) : null}
                      <div>
                        <h2 className="text-base font-semibold text-slate-950">{selectedConfig?.label}</h2>
                        <p className="text-xs text-slate-500">/api/v1/consumer/data/{selectedResource}</p>
                      </div>
                    </div>
                  </div>

                  <div className="flex min-w-0 flex-col gap-2 sm:flex-row sm:items-center">
                    <label className="relative block min-w-0">
                      <Search className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
                      <input
                        value={searchTerm}
                        onChange={(event) => setSearchTerm(event.target.value)}
                        className="h-9 w-full rounded-lg border border-slate-300 bg-white pl-9 pr-3 text-sm outline-none ring-emerald-500 focus:ring-2 sm:w-64"
                        placeholder="Recherche"
                      />
                    </label>
                    <button
                      type="button"
                      onClick={resetEditor}
                      className="inline-flex h-9 items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-3 text-sm font-medium text-slate-700 hover:bg-slate-50"
                    >
                      <Plus className="h-4 w-4" aria-hidden="true" />
                      Nouveau
                    </button>
                  </div>
                </div>

                <ResourceTable
                  config={selectedConfig}
                  items={filteredItems}
                  loading={resourceLoading}
                  error={resourceError}
                  selectedRow={selectedRow}
                  onSelect={handleRowSelect}
                />
              </div>

              <ActionPanel
                selectedResource={selectedResource}
                selectedRow={selectedRow}
                editorValue={editorValue}
                mutationLoading={mutationLoading}
                mutationResult={mutationResult}
                mutationError={mutationError}
                onEditorChange={setEditorValue}
                onCreate={() => handleMutation('create')}
                onUpdate={() => handleMutation('update')}
                onDelete={() => handleMutation('delete')}
              />
            </section>

            <section className="grid gap-5 xl:grid-cols-[410px_minmax(0,1fr)]">
              <ServicePanel services={services} />
              <JsonPanel title="Derniere reponse enrichie" data={resourceData || mutationResult} />
            </section>
          </div>
        </main>
      </div>
    </div>
  )
}

function StatusBadge({ available }) {
  const Icon = available ? CheckCircle2 : AlertTriangle

  return (
    <span
      className={`inline-flex h-9 items-center gap-2 rounded-lg border px-3 text-sm font-medium ${
        available
          ? 'border-emerald-200 bg-emerald-50 text-emerald-700'
          : 'border-rose-200 bg-rose-50 text-rose-700'
      }`}
    >
      <Icon className="h-4 w-4" aria-hidden="true" />
      {available ? 'Fournisseur OK' : 'Fournisseur KO'}
    </span>
  )
}

function AlertBanner({ message }) {
  return (
    <div className="flex items-center gap-3 rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-sm text-amber-800">
      <AlertTriangle className="h-4 w-4 shrink-0" aria-hidden="true" />
      <span>{message}</span>
    </div>
  )
}

function MetricCard({ icon: Icon, label, value, accent }) {
  return (
    <article className="rounded-lg border border-slate-200 bg-white p-4">
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-xs font-medium uppercase tracking-wide text-slate-500">{label}</p>
          <p className="mt-2 text-2xl font-semibold text-slate-950">{value}</p>
        </div>
        <span className={`flex h-10 w-10 items-center justify-center rounded-lg bg-slate-100 ${accent}`}>
          <Icon className="h-5 w-5" aria-hidden="true" />
        </span>
      </div>
    </article>
  )
}

function ResourceTable({ config, items, loading, error, selectedRow, onSelect }) {
  if (loading) {
    return (
      <div className="flex min-h-80 items-center justify-center">
        <Loader2 className="h-7 w-7 animate-spin text-slate-400" aria-hidden="true" />
      </div>
    )
  }

  if (error) {
    return (
      <div className="flex min-h-80 flex-col items-center justify-center gap-3 p-6 text-center">
        <AlertTriangle className="h-8 w-8 text-rose-500" aria-hidden="true" />
        <p className="max-w-md text-sm text-slate-600">{error}</p>
      </div>
    )
  }

  if (!items.length) {
    return (
      <div className="flex min-h-80 flex-col items-center justify-center gap-3 p-6 text-center">
        <Database className="h-8 w-8 text-slate-400" aria-hidden="true" />
        <p className="text-sm text-slate-500">Aucune donnee</p>
      </div>
    )
  }

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-slate-200 text-sm">
        <thead className="bg-slate-50">
          <tr>
            <th className="w-16 px-4 py-3 text-left font-semibold text-slate-500">ID</th>
            {config.columns.map((column) => (
              <th key={column} className="px-4 py-3 text-left font-semibold text-slate-500">
                {column.replace('.', ' ')}
              </th>
            ))}
            <th className="w-16 px-4 py-3 text-right font-semibold text-slate-500">Vue</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-100 bg-white">
          {items.map((item, index) => {
            const selected = selectedRow?.id === item.id && item.id !== undefined
            return (
              <tr
                key={item.id || index}
                onClick={() => onSelect(item)}
                className={`cursor-pointer transition ${
                  selected ? 'bg-emerald-50' : 'hover:bg-slate-50'
                }`}
              >
                <td className="whitespace-nowrap px-4 py-3 font-mono text-xs text-slate-500">
                  {item.id ? `#${item.id}` : '-'}
                </td>
                {config.columns.map((column) => (
                  <td key={column} className="max-w-64 truncate px-4 py-3 text-slate-700">
                    {formatValue(readPath(item, column))}
                  </td>
                ))}
                <td className="px-4 py-3 text-right text-slate-400">
                  <Eye className="ml-auto h-4 w-4" aria-hidden="true" />
                </td>
              </tr>
            )
          })}
        </tbody>
      </table>
    </div>
  )
}

function ActionPanel({
  selectedResource,
  selectedRow,
  editorValue,
  mutationLoading,
  mutationResult,
  mutationError,
  onEditorChange,
  onCreate,
  onUpdate,
  onDelete,
}) {
  return (
    <aside className="rounded-lg border border-slate-200 bg-white">
      <div className="border-b border-slate-200 p-4">
        <h2 className="text-base font-semibold text-slate-950">Actions</h2>
        <p className="mt-1 text-xs text-slate-500">
          {selectedResource}
          {selectedRow?.id ? ` #${selectedRow.id}` : ''}
        </p>
      </div>

      <div className="space-y-4 p-4">
        <textarea
          value={editorValue}
          onChange={(event) => onEditorChange(event.target.value)}
          spellCheck={false}
          className="h-72 w-full resize-none rounded-lg border border-slate-300 bg-slate-950 p-3 font-mono text-xs leading-5 text-slate-100 outline-none ring-emerald-500 focus:ring-2"
        />

        <div className="grid gap-2 sm:grid-cols-3 xl:grid-cols-1 2xl:grid-cols-3">
          <button
            type="button"
            onClick={onCreate}
            disabled={mutationLoading}
            className="inline-flex h-10 items-center justify-center gap-2 rounded-lg bg-emerald-600 px-3 text-sm font-medium text-white hover:bg-emerald-700 disabled:cursor-not-allowed disabled:opacity-60"
          >
            <Plus className="h-4 w-4" aria-hidden="true" />
            Creer
          </button>
          <button
            type="button"
            onClick={onUpdate}
            disabled={mutationLoading || !selectedRow?.id}
            className="inline-flex h-10 items-center justify-center gap-2 rounded-lg bg-indigo-600 px-3 text-sm font-medium text-white hover:bg-indigo-700 disabled:cursor-not-allowed disabled:opacity-60"
          >
            <Save className="h-4 w-4" aria-hidden="true" />
            Modifier
          </button>
          <button
            type="button"
            onClick={onDelete}
            disabled={mutationLoading || !selectedRow?.id}
            className="inline-flex h-10 items-center justify-center gap-2 rounded-lg bg-rose-600 px-3 text-sm font-medium text-white hover:bg-rose-700 disabled:cursor-not-allowed disabled:opacity-60"
          >
            <Trash2 className="h-4 w-4" aria-hidden="true" />
            Supprimer
          </button>
        </div>

        {mutationLoading ? (
          <div className="flex items-center gap-2 rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-sm text-slate-600">
            <Loader2 className="h-4 w-4 animate-spin" aria-hidden="true" />
            Traitement
          </div>
        ) : null}

        {mutationError ? <AlertBanner message={mutationError} /> : null}
        {mutationResult ? <JsonPanel title="Reponse action" data={mutationResult} compact /> : null}
      </div>
    </aside>
  )
}

function ServicePanel({ services }) {
  const list = services?.available_services || []

  return (
    <section className="rounded-lg border border-slate-200 bg-white">
      <div className="border-b border-slate-200 p-4">
        <h2 className="text-base font-semibold text-slate-950">Services</h2>
        <p className="mt-1 text-xs text-slate-500">{services?.provider_status || 'UNKNOWN'}</p>
      </div>
      <div className="grid grid-cols-2 gap-2 p-4 sm:grid-cols-3 lg:grid-cols-5 xl:grid-cols-2">
        {list.map((service) => (
          <span
            key={service}
            className="truncate rounded-lg border border-slate-200 bg-slate-50 px-3 py-2 text-sm font-medium text-slate-700"
          >
            {service}
          </span>
        ))}
      </div>
    </section>
  )
}

function JsonPanel({ title, data, compact = false }) {
  return (
    <section className="min-w-0 rounded-lg border border-slate-200 bg-white">
      <div className="flex items-center justify-between border-b border-slate-200 p-4">
        <h2 className="text-base font-semibold text-slate-950">{title}</h2>
        <span className="rounded-md bg-slate-100 px-2 py-1 font-mono text-xs text-slate-500">JSON</span>
      </div>
      <pre
        className={`overflow-auto bg-slate-950 p-4 font-mono text-xs leading-5 text-slate-100 ${
          compact ? 'max-h-60 rounded-b-lg' : 'max-h-96 rounded-b-lg'
        }`}
      >
        {JSON.stringify(data || {}, null, 2)}
      </pre>
    </section>
  )
}

export default App
