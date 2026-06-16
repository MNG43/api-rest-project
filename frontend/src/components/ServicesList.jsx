const SERVICE_LABELS = {
  auth: 'Authentification',
  etudiants: 'Étudiants',
  professeurs: 'Professeurs',
  modules: 'Modules',
  cours: 'Cours',
  notes: 'Notes',
  paiements: 'Paiements',
  salles: 'Salles',
  'emploi-du-temps': 'Emploi du temps',
  notifications: 'Notifications',
}

function getServiceLabel(name) {
  return SERVICE_LABELS[name] ?? name
}

export default function ServicesList({
  services,
  loading,
  error,
  selectedService,
  providerStatus,
  onSelect,
  onRefresh,
}) {
  return (
    <section className="panel services-panel">
      <div className="panel-header">
        <div>
          <h2>Services universitaires</h2>
          {services && (
            <p className="panel-subtitle">
              {services.total_services} services disponibles
              {providerStatus && (
                <span className={`provider-tag ${providerStatus.toLowerCase()}`}>
                  Fournisseur {providerStatus === 'AVAILABLE' ? 'actif' : 'inactif'}
                </span>
              )}
            </p>
          )}
        </div>
        <button type="button" className="btn btn-ghost" onClick={onRefresh}>
          Actualiser
        </button>
      </div>

      {loading && <p className="muted">Chargement des services…</p>}
      {error && <p className="error-text">{error}</p>}

      {!loading && !error && services?.available_services && (
        <div className="services-grid">
          {services.available_services.map((name) => (
            <button
              key={name}
              type="button"
              className={`service-card ${selectedService === name ? 'selected' : ''}`}
              onClick={() => onSelect(name)}
            >
              <span className="service-name">{getServiceLabel(name)}</span>
              <span className="service-slug">{name}</span>
            </button>
          ))}
        </div>
      )}

      {services?.warning && (
        <div className="warning-banner">{services.warning}</div>
      )}
    </section>
  )
}
