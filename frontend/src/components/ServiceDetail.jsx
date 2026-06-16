function JsonViewer({ data }) {
  return (
    <pre className="json-viewer">{JSON.stringify(data, null, 2)}</pre>
  )
}

function StatusPill({ status, httpStatus }) {
  const isSuccess = status === 'SUCCESS'
  const isFallback = status === 'FALLBACK'

  let className = 'status-pill'
  if (isSuccess) className += ' success'
  else if (isFallback) className += ' fallback'
  else className += ' error'

  return (
    <span className={className}>
      {status ?? `HTTP ${httpStatus}`}
    </span>
  )
}

export default function ServiceDetail({ serviceName, result, loading, error }) {
  if (!serviceName) {
    return (
      <section className="panel detail-panel empty">
        <h2>Détails du service</h2>
        <p className="muted">Sélectionnez un service pour consulter sa réponse enrichie.</p>
      </section>
    )
  }

  return (
    <section className="panel detail-panel">
      <div className="panel-header">
        <div>
          <h2>Détails — {serviceName}</h2>
          <p className="panel-subtitle mono">GET /api/v1/consumer/service/{serviceName}</p>
        </div>
        {result && (
          <StatusPill
            status={result.data?.processing_status}
            httpStatus={result.status}
          />
        )}
      </div>

      {loading && <p className="muted">Appel au fournisseur en cours…</p>}
      {error && <p className="error-text">{error}</p>}

      {!loading && result && (
        <>
          {result.data?.processing_status === 'FALLBACK' && (
            <div className="fallback-banner">
              <strong>Dégradation gracieuse</strong>
              <span>{result.data.message}</span>
              {result.data.suggestion && <span>{result.data.suggestion}</span>}
            </div>
          )}

          <div className="meta-tags">
            {result.data?.consumer_service && (
              <span className="meta-tag">{result.data.consumer_service}</span>
            )}
            {result.data?.consumer_version && (
              <span className="meta-tag">v{result.data.consumer_version}</span>
            )}
            {result.data?.data_source && (
              <span className="meta-tag">{result.data.data_source}</span>
            )}
          </div>

          <JsonViewer data={result.data} />
        </>
      )}
    </section>
  )
}
