OAuth2 / OIDC integration (Keycloak example)

1. Quickstart with Keycloak (local):
   - Run Keycloak (or use `quay.io/keycloak/keycloak:latest`) and create a realm + client.
   - Set client: Access Type=confidential, Valid Redirect URI: `http://localhost:8080/login/oauth2/code/keycloak`.
   - Create roles (e.g., `ROLE_ADMIN`) and a test user.

2. Environment variables (recommended for production):
   - `OAUTH_CLIENT_ID`, `OAUTH_CLIENT_SECRET`, `OAUTH_ISSUER_URI`.

3. Notes:
   - This repo uses Spring Security's `oauth2Login()` and requires provider registration in `application-*.yml`.
   - For advanced claim -> authorities mapping implement a custom `OAuth2UserService`.
