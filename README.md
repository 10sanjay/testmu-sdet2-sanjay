# 🧪 Enterprise Hybrid Automation Framework

A production-grade hybrid test automation framework demonstrating **UI**, **API**, and **Integration** testing using enterprise-level patterns — clean architecture, dynamic test data, token-based authentication, retry handling, and rich Allure reporting.

---

## 📌 About The Project

This framework automates end-to-end testing of two real, publicly accessible applications:

| Layer           | Application                                | Purpose                                                  |
|-----------------|--------------------------------------------|----------------------------------------------------------|
| **UI**          | [SauceDemo](https://www.saucedemo.com)     | E-commerce flow: login, inventory, cart, checkout        |
| **API**         | [DummyJSON](https://dummyjson.com)         | Authentication (JWT), CRUD users, search, secured APIs   |
| **Integration** | API + UI combined                          | Create data via API → validate via UI                    |
---
## ✨ Key Features
- ✅ Hybrid framework — UI + API + Integration in one cohesive structure
- ✅ Page Object Model with stable `data-test` selectors
- ✅ Token-based authentication with auto-refresh on `401 Unauthorized`
- ✅ Dynamic test data (Faker, UUID, timestamps) — zero hardcoding
- ✅ Data-driven tests via external JSON
- ✅ Retry handling at both test and action level
- ✅ Allure reporting with screenshots & API request/response logs
- ✅ Externalized configuration — no hardcoded URLs or credentials
---
## 🛠 Tech Stack
| Category             | Technology                  |
|----------------------|-----------------------------|
| Language             | Java 11                     |
| Build Tool           | Maven 3.8+                  |
| UI Automation        | Selenium WebDriver 4.21     |
| API Automation       | RestAssured 5.4             |
| Test Runner          | TestNG 7.10                 |
| Reporting            | Allure 2.27                 |
| Driver Management    | WebDriverManager 5.8        |
| Logging              | SLF4J + Logback             |
| Test Data Generation | JavaFaker                   |
| JSON Handling        | Jackson Databind            |
| Schema Validation    | JSON Schema Validator       |
---
## ✅ Prerequisites (Install Before Cloning)
Make sure the following are installed on your machine:
| Tool                       | Version       | Verify Installation       |
|-----------------------------|---------------|----------------------------|
| **Java JDK**                | 11 or higher  | `java -version`           |
| **Maven**                   | 3.8+          | `mvn -version`            |
| **Git**                     | Latest        | `git --version`           |
| **Google Chrome**           | Latest        | Required for UI tests     |
| **Mozilla Firefox**         | Latest        | Required for UI tests     |
| **Allure CLI** *(optional)* | 2.27+         | `allure --version`        |
### 🔧 Installation Links
- **Java 11+** → https://adoptium.net/
- **Maven** → https://maven.apache.org/download.cgi
- **Git** → https://git-scm.com/downloads
- **Chrome** → https://www.google.com/chrome
- **Firefox** → https://www.mozilla.org/firefox
- **Allure CLI** (Windows) → `scoop install allure` or `choco install allure-commandline`
- **Allure CLI** (macOS) → `brew install allure`

> ℹ️ WebDriverManager auto-downloads browser drivers — no manual driver setup is required.
---
## 🚀 Getting Started
### 1️⃣ Clone the Repository
```bash
# Clone the repository
git clone https://github.com/<your-username>/<your-repo-name>.git
cd <your-repo-name>

# Install dependencies and compile project
mvn clean install -DskipTests

# Execute all tests
mvn clean test

# Generate and open Allure report
mvn allure:serve

