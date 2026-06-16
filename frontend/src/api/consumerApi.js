const API_BASE = '/api/v1/consumer'

async function fetchJson(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    },
    ...options,
  })
  const text = await response.text()
  const data = text ? JSON.parse(text) : null
  return { data, status: response.status, ok: response.ok }
}

export function getHealth() {
  return fetchJson('/health')
}

export function getServices() {
  return fetchJson('/services')
}

export function getServiceInfo(serviceName) {
  return fetchJson(`/service/${encodeURIComponent(serviceName)}`)
}

export function getEnrichedData(resourceName) {
  return fetchJson(`/data/${encodeURIComponent(resourceName)}`)
}

export function getUniversitySummary() {
  return fetchJson('/resume')
}

export function createResource(resourceName, payload) {
  return fetchJson(`/data/${encodeURIComponent(resourceName)}`, {
    method: 'POST',
    body: JSON.stringify(payload),
  })
}

export function updateResource(resourceName, id, payload) {
  return fetchJson(`/data/${encodeURIComponent(resourceName)}/${encodeURIComponent(id)}`, {
    method: 'PUT',
    body: JSON.stringify(payload),
  })
}

export function deleteResource(resourceName, id) {
  return fetchJson(`/data/${encodeURIComponent(resourceName)}/${encodeURIComponent(id)}`, {
    method: 'DELETE',
  })
}

export function loadTestData() {
  return fetchJson('/test-data', {
    method: 'POST',
  })
}
