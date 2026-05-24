# 🧪 Test Strategy

## 1. Approach
Layered hybrid testing — favouring fast API checks, narrow UI coverage on critical paths, and integration tests to validate cross-layer workflows.

Integration (E2E) ← API → UI orchestration
▲
UI ← Critical user journeys only
▲
API ← Broad: contract, auth, CRUD, SLA


---

## 2. Coverage

### UI (SauceDemo)
- Login (valid, locked, empty)
- Cart (add multiple, badge count)
- Checkout (data-driven E2E, empty-form validation)
- Cross-browser (Chrome + Firefox via CI matrix)

### API (DummyJSON)
- Auth: login, invalid login, refresh, auto re-auth
- Secured endpoints: `/auth/me`, `/auth/products`, `/auth/carts`
- CRUD: GET, POST, PUT, DELETE, 404
- Search & filter (data-driven)
- Response-time SLA assertions

### Integration
- API → UI: create user via API, validate via UI login
- User lifecycle: CRUD in a single ordered flow

---

## 3. Why These Scenarios

| Choice | Reason |
|---|---|
| Login & Checkout | Revenue-critical paths |
| Token refresh | Mirrors real OAuth/JWT |
| Data-driven checkout | Scales 1 test → N datasets |
| Negative auth (401) | Catches regression in auth layer |
| API → UI flow | Proves cross-layer orchestration |

---

## 4. Top Risks

| Risk | Mitigation |
|---|---|
| DummyJSON mocks PUT/DELETE | Validate shape, not persistence |
| SauceDemo locator drift | Only `id` / `data-test`, centralised in POM |
| CI flakiness (React + headless Chrome) | RetryAnalyzer + per-button-flip verification |

---

## 5. Out of Scope (Documented)
- Performance / load
- Visual regression
- Mobile / Appium
- DB validation
- Security (ZAP)

---

## 6. Improvement Plan

**Short-term**
- JSON schema validation on all responses
- Mask Bearer tokens in logs
- Secrets via env vars

**Medium-term**
- Contract testing (Pact)
- Visual regression (Percy)
- Dockerised execution

**Long-term**
- Performance baselines (Gatling)
- Self-healing locators
- AI-assisted flake quarantine

---

## 7. Quality Gates
PR is mergeable when:
- ✅ All API tests pass
- ✅ UI passes on Chrome & Firefox
- ✅ Integration tests pass
- ✅ Allure report generated