# Secure Student Portal — Java Web App, NetBeans, Security & CI/CD

## Stack
Java 17, Maven, Jakarta Servlet 6, JSP, H2 in-memory DB, BCrypt, Apache Tomcat 10.1.

## Features / security controls
- Registration and login; BCrypt password hashing (cost 12)
- Prepared statements (SQL injection defense)
- Username/password validation
- CSRF token on forms
- Session authentication; 15-minute timeout
- Security response headers (CSP, X-Frame-Options, nosniff, Referrer-Policy)
- Logout
- GitHub Actions CI: build/test, OWASP Dependency Check, WAR artifact

> Demo note: H2 is in-memory and resets on application restart. Secure cookie `Secure` is disabled for local HTTP. Enable it when deployed behind HTTPS. Add rate limiting, account lockout, email verification, production DB migrations, and robust logging before production.

## Requirements
- JDK 17
- Apache NetBeans 18+ (or current)
- Maven 3.9+
- Tomcat 10.1

## Open in NetBeans
1. Extract ZIP.
2. NetBeans: File → Open Project → select the extracted folder (the one containing `pom.xml`).
3. Ensure JDK 17 is selected: Tools → Java Platforms.
4. Right-click project → Clean and Build.
5. Add Tomcat 10.1: Tools → Servers → Add Server, choose Apache Tomcat, set installation folder.
6. Right-click project → Run (or deploy the WAR to Tomcat).
7. Open `http://localhost:8080/secure-student-portal/`.
8. Register an account; then log in.

## Build from terminal
```bash
mvn clean verify
```
WAR output: `target/secure-student-portal.war`.

## CI/CD pipeline (GitHub Actions)
1. Create a GitHub repository (e.g. `secure-student-portal`).
2. Push this project to branch `main`.
3. The workflow `.github/workflows/ci.yml` runs on push, pull request, or manual dispatch.
4. GitHub → Actions → Java Web App CI → open the run.
5. Verify Build/Test and OWASP Dependency Check steps. Download artifacts: `application-war` and `dependency-check-report`.
6. For a deployment stage, configure a Tomcat server or cloud hosting. Store deployment credentials as GitHub Actions Secrets—not in source. Add a deployment job only after selecting a target server.

## Security testing checklist
- Try SQL injection strings in username (e.g. `' OR '1'='1`) — should not authenticate.
- Register with short password — rejected.
- Submit form without CSRF token — should return 403.
- Inspect response headers in browser DevTools.
- Attempt `/dashboard` while logged out — redirects to login.
- Confirm password is not stored as plaintext (BCrypt hash in DB).

## CI stages
Checkout → Java setup → Maven clean verify → OWASP Dependency Check (CVSS >= 7 fails) → upload report → upload WAR.

## Notes
This starter is for classroom/lab demonstrations, not a production-ready identity system.
