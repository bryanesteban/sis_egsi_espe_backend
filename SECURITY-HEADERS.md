# 🔒 Headers de Seguridad HTTP - SIEGSI

## Headers Implementados

El backend de SIEGSI incluye los siguientes headers de seguridad HTTP para proteger la aplicación contra vulnerabilidades comunes:

### 1. **X-XSS-Protection**
```
X-XSS-Protection: 1; mode=block
```
- **Función**: Protege contra ataques de Cross-Site Scripting (XSS)
- **Configuración**: Activa el filtro XSS del navegador en modo block
- **Impacto**: Bloquea la ejecución de scripts maliciosos

### 2. **X-Frame-Options**
```
X-Frame-Options: DENY
```
- **Función**: Previene ataques de Clickjacking
- **Configuración**: Impide que la página sea cargada en iframes
- **Impacto**: Protege contra engaños visuales en marcos externos

### 3. **Content-Security-Policy (CSP)**
```
Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline'; 
style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:;
```
- **Función**: Define políticas de seguridad de contenido
- **Configuración**:
  - `default-src 'self'`: Solo permite recursos del mismo origen
  - `script-src 'self' 'unsafe-inline'`: Scripts propios e inline (para Swagger)
  - `style-src 'self' 'unsafe-inline'`: Estilos propios e inline
  - `img-src 'self' data:`: Imágenes propias y data URIs
  - `font-src 'self' data:`: Fuentes propias y data URIs
- **Impacto**: Previene inyección de código malicioso

### 4. **Referrer-Policy**
```
Referrer-Policy: strict-origin-when-cross-origin
```
- **Función**: Controla la información del referrer en peticiones HTTP
- **Configuración**: Envía solo el origen en peticiones cross-origin
- **Impacto**: Protege información sensible en URLs

### 5. **Cache-Control / Pragma / Expires**
```
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
```
- **Función**: Controla el almacenamiento en caché
- **Configuración**: Desactiva caché para respuestas sensibles
- **Impacto**: Previene exposición de datos en caché compartido

## Verificación

### Usando curl
```bash
curl -I http://localhost:9090/actuator/health
```

### Usando navegador
1. Abre las DevTools (F12)
2. Ve a la pestaña Network
3. Recarga la página
4. Selecciona cualquier request
5. Revisa la sección "Response Headers"

### Resultado esperado
```http
HTTP/1.1 200 
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
Content-Security-Policy: default-src 'self'; script-src 'self' 'unsafe-inline'; ...
Referrer-Policy: strict-origin-when-cross-origin
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
```

## Configuración

Los headers están configurados en:
```
src/main/java/com/espe/ListoEgsi/auth/SpringSecurityConfig.java
```

```java
.headers(headers -> headers
    .contentSecurityPolicy(csp -> csp
        .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; ...")
    )
    .frameOptions(frame -> frame.deny())
    .xssProtection(xss -> xss
        .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
    )
    .referrerPolicy(referrer -> referrer
        .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
    )
)
```

## Compatibilidad

| Header | Chrome | Firefox | Safari | Edge |
|--------|--------|---------|--------|------|
| X-XSS-Protection | ✅ | ⚠️ (Obsoleto) | ✅ | ✅ |
| X-Frame-Options | ✅ | ✅ | ✅ | ✅ |
| CSP | ✅ | ✅ | ✅ | ✅ |
| Referrer-Policy | ✅ | ✅ | ✅ | ✅ |

## Notas de Seguridad

### ⚠️ Para Producción

1. **CSP más estricto**: Considera eliminar `'unsafe-inline'` cuando sea posible
2. **HSTS**: Agregar Strict-Transport-Security en servidor web (Nginx/Apache)
3. **Monitoreo**: Implementar reportes de violaciones CSP
4. **Actualizar**: Revisar periódicamente mejores prácticas de seguridad

### ✅ Buenas Prácticas Implementadas

- ✅ Deshabilitación de CSRF (API REST stateless con JWT)
- ✅ Sesiones stateless (SessionCreationPolicy.STATELESS)
- ✅ CORS configurado con orígenes específicos
- ✅ JWT con expiración (24 horas)
- ✅ Passwords hasheados con BCrypt
- ✅ Headers de seguridad en todas las respuestas

## Testing

### Test de Headers con curl
```bash
# Login
curl -I -X POST http://localhost:9090/login \
  -H "Content-Type: application/json" \
  -d '{"username":"cDaroma","password":"password"}'

# Health Check
curl -I http://localhost:9090/actuator/health

# Swagger UI
curl -I http://localhost:9090/swagger-ui.html
```

### Herramientas de Análisis
- [SecurityHeaders.com](https://securityheaders.com/)
- [Mozilla Observatory](https://observatory.mozilla.org/)
- [OWASP ZAP](https://www.zaproxy.org/)

## Referencias

- [OWASP Secure Headers Project](https://owasp.org/www-project-secure-headers/)
- [Spring Security Headers](https://docs.spring.io/spring-security/reference/servlet/exploits/headers.html)
- [MDN HTTP Headers](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers)
