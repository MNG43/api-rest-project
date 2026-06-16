function StatusBadge({ available, label }) {
  return (
    <span className={`badge ${available ? 'badge-up' : 'badge-down'}`}>
      <span className="badge-dot" />
      {label}
    </span>
  )
}

export default function HealthPanel({ health, loading, error, onRefresh }) {
  if (loading) {
    return (
      <section className="panel health-panel">
        <div className="panel-header">
          <h2>État du système</h2>
        </div>
        <p className="muted">Vérification en cours…</p>
      </section>
    )
  }

  if (error) {
    return (
      <section className="panel health-panel">
        <div className="panel-header">
          <h2>État du système</h2>
          <button type="button" className="btn btn-ghost" onClick={onRefresh}>
            Actualiser
          </button>
        </div>
        <p className="error-text">{error}</p>
      </section>
    )
  }

  return (
    <section className="panel health-panel">
      <div className="panel-header">
        <h2>État du système</h2>
        <button type="button" className="btn btn-ghost" onClick={onRefresh}>
          Actualiser
        </button>
      </div>

      <div className="health-grid">
        <div className="health-item">
          <span className="health-label">Consommateur</span>
          <StatusBadge available={health?.status === 'UP'} label={health?.status ?? 'INCONNU'} />
        </div>
        <div className="health-item">
          <span className="health-label">Fournisseur</span>
          <StatusBadge
            available={health?.provider_available}
            label={health?.provider_available ? 'DISPONIBLE' : 'INDISPONIBLE'}
          />
        </div>
        <div className="health-item">
          <span className="health-label">Port consommateur</span>
          <span className="health-value">{health?.port ?? '—'}</span>
        </div>
        <div className="health-item">
          <span className="health-label">URL fournisseur</span>
          <span className="health-value mono">{health?.provider_url ?? '—'}</span>
        </div>
        <div className="health-item health-item-wide">
          <span className="health-label">Dernière vérification</span>
          <span className="health-value mono">{health?.timestamp ?? '—'}</span>
        </div>
      </div>
    </section>
  )
}
