```pgsql
security/
├── JwtTokenProvider.java
├── JwtAuthenticationFilter.java
├── CustomUserDetails.java
├── CustomUserDetailsService.java
└── SecurityConfig.java
```

## The Big Picture (Flow)

```plaintext
User -> /api/auth/login -------------------→ [ AuthController ]
                                       ↓
                                 Authenticate user
                                       ↓
                                Generate JWT Token
                                       ↓
                            Return Token to client

User -> [Any protected API] with token in header
→ SecurityFilterChain → JwtAuthenticationFilter → JwtTokenProvider → CustomUserDetailsService
→ If valid: allow access | else: 403
```

## Example Flow: Logging In

1. User hits `/api/auth/login`:

```http request
POST /api/auth/login
Content-Type: application/json

{
  "username": "supplier1",
  "password": "secure123"
}
```

Backend:

- Uses `CustomUserDetailsService` to find user
- Verifies password using `PasswordEncoder`
- Generates JWT via `JwtTokenProvider`

## Example: Accessing a Protected Route

```http request
GET /api/invoices
Authorization: Bearer eyJhbGciOiJIUz...
```

Backend

- `JwtAuthenticationFilter` intercepts the request
- Pulls username from token
- Validates token
- Loads `UserDetails`
- Sets the user in `SecurityContextHolder`
- Request continues if valid