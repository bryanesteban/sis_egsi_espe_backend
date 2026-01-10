# 📮 Colección de Postman - SIEGSI Autenticación

## 📥 Importar en Postman

1. Abre Postman
2. Click en **Import** (arriba a la izquierda)
3. Selecciona el archivo `SIEGSI-Auth-Postman-Collection.json`
4. ¡Listo! La colección aparecerá en tu workspace

## 🔑 Credenciales de Prueba

**Usuario Administrador (Ya configurado en la colección):**
- **Username**: `cDaroma`
- **Password**: `password`
- **Rol**: `ADMIN`

✅ **Las variables están preconfiguradas**. Solo necesitas hacer clic en **Send** en cada endpoint.

## ⚡ Uso Rápido

1. Importa la colección en Postman
2. Ejecuta el endpoint **"1. Login"** → Click en **Send**
3. El token se guarda automáticamente
4. Ejecuta los demás endpoints sin modificar nada

**No necesitas cambiar ningún valor en los requests**, las credenciales ya están configuradas con las variables `{{username}}` y `{{password}}`.

## ✅ Tests Automáticos

La colección incluye tests automáticos que se ejecutan después de cada request:

### Tests en Login:
- ✅ Validación de status code 200
- ✅ Verificación de token JWT en respuesta
- ✅ Verificación de headers de seguridad HTTP:
  - `X-XSS-Protection`
  - `X-Frame-Options`
  - `Content-Security-Policy`
- ✅ Guardado automático del token

### Tests en Refresh Token:
- ✅ Validación de status code 200
- ✅ Verificación de nuevo token
- ✅ Actualización automática del token
- ✅ Verificación de headers de seguridad

### Revisar Resultados de Tests:
1. Ejecuta cualquier endpoint
2. Ve a la pestaña **Test Results** en Postman
3. Verás todos los tests ejecutados con ✓ o ✗

## 🔍 Cómo Ver los Headers de Seguridad en Postman

### Método 1: Pestaña Headers (Recomendado)
1. Ejecuta cualquier endpoint (por ejemplo: Login)
2. Click en **Send**
3. En la sección de respuesta (abajo), busca la pestaña **Headers**
4. Verás todos los headers HTTP que el servidor envió:
   ```
   X-XSS-Protection: 1; mode=block
   X-Frame-Options: DENY
   Content-Security-Policy: default-src 'self'; script-src...
   Referrer-Policy: strict-origin-when-cross-origin
   Cache-Control: no-cache, no-store, max-age=0, must-revalidate
   ```

### Método 2: Console de Postman
1. Abre la consola de Postman: **View → Show Postman Console** (o `Alt+Ctrl+C`)
2. Ejecuta cualquier endpoint
3. En la consola verás:
   - Request headers enviados
   - Response headers recibidos
   - Logs de los tests ejecutados

### Método 3: Test Results
1. Después de ejecutar un endpoint
2. Ve a la pestaña **Test Results**
3. Los tests de headers te confirman que están presentes:
   ```
   ✓ Response tiene X-XSS-Protection header
   ✓ Response tiene X-Frame-Options header
   ✓ Response tiene Content-Security-Policy header
   ```

### 📸 Ubicación Visual en Postman

```
┌─────────────────────────────────────────────────┐
│  POST http://localhost:9090/login    [Send]    │
├─────────────────────────────────────────────────┤
│  Body   │  Params  │  Auth  │  Headers         │
│  ...request body...                             │
└─────────────────────────────────────────────────┘
              ↓ Después de Send ↓
┌─────────────────────────────────────────────────┐
│  Status: 200 OK   Time: 245ms   Size: 185 B    │
├─────────────────────────────────────────────────┤
│  Body │ Cookies │ Headers │ Test Results       │ ← Click aquí
│  ────────────────────────────                   │
│  X-XSS-Protection: 1; mode=block                │
│  X-Frame-Options: DENY                          │
│  Content-Security-Policy: default-src 'self'... │
│  Referrer-Policy: strict-origin-when-cross...   │
│  Cache-Control: no-cache, no-store...           │
└─────────────────────────────────────────────────┘
```

## 🚀 Endpoints Incluidos

### 1️⃣ **Login**
- **Método**: POST
- **URL**: `{{base_url}}/login`
- **Body**:
  ```json
  {
    "username": "{{username}}",
    "password": "{{password}}"
  }
  ```
  *Usa las variables de colección: cDaroma / password*
  
- **Respuesta exitosa (200)**:
  ```json
  {
    "username": "cDaroma",
    "rolename": "ADMIN",
    "token": "eyJhbGciOiJIUzI1NiIs..."
  }
  ```
- **Script automático**: Guarda el token en la variable `{{jwt_token}}`

### 2️⃣ **Refresh Token**
- **Método**: POST
- **URL**: `{{base_url}}/login/refresh`
- **Body**:
  ```json
  {
    "token": "{{jwt_token}}"
  }
  ```
- **Respuesta exitosa (200)**:
  ```jsoncDaroma
  {
    "username": "admin",
    "token": "nuevo_token_aqui...",
    "message": "Token renovado exitosamente"
  }
  ```
- **Script automático**: Actualiza el token en `{{jwt_token}}`

### 3️⃣ **Cambiar Nombre de Usuario**
- **Método**: POST
- **URL**: `{{base_url}}/login/changeUsername`
- **Auth**: Bearer Token (usa `{{jwt_token}}` automáticamente)
- **Body**:
  ```json
  {
    "usernameOld": "admin",
    "usernameNew": "adminNew",
    "password": "password123"
  }
  ```

### 4️⃣ **Cambiar Contraseña**
- **Método**: POST
- **URL**: `{{base_url}}/login/changePassword`
- **Auth**: Bearer Token (usa `{{jwt_token}}` automáticamente)
- **Body**:
  ```json
  {
    "username": "admin",
    "passwordOld": "password123",
    "passwordNew": "newPassword456"
  }
  ```

### 5️⃣ **Health Check**
- **Método**: GET
- **URL**: `{{base_url}}/actuator/health`
- **Sin autenticación**
- **Respuesta**:
  ```json
  {
    "status": "UP"
  }
  ```

## 🔧 Variables de Colección

La colección incluye estas variables configuradas automáticamente:

| Variable | Valor por defecto | Descripción |
|----------|-------------------|-------------|
| `base_url` | `http://localhost:9090` | URL base del API |
| `username` | `cDaroma` | Usuario administrador |
| `password` | `password` | Contraseña del usuario |
| `jwt_token` | (se guarda automáticamente) | Token JWT después del login |
| `rolename` | (se guarda automáticamente) | Rol del usuario después del login |

### Cómo Editar Variables (Opcional)

Si necesitas usar otras credenciales:

1. Click derecho en la colección "SIEGSI - Autenticación"
2. Click en **"Edit"**
3. Ve a la pestaña **"Variables"**
4. Edita `username` y `password` con tus credenciales
5. **Save** y listo

## 📝 Flujo de Uso Recomendado

1. **Ejecuta Login** primero
   - El token se guarda automáticamente en `{{jwt_token}}`
   - Las variables `username` y `rolename` también se guardan

2. **Prueba los endpoints protegidos**
   - Los endpoints 3 y 4 usan automáticamente el token guardado
   - No necesitas copiar/pegar el token manualmente

3. **Renueva el token cuando sea necesario**
   - Usa el endpoint **Refresh Token**
   - El nuevo token reemplaza automáticamente al anterior

## 🎯 Características Especiales

### ✅ Scripts Automáticos

**Login (Test Script)**:
```javascript
// Guarda el token automáticamente
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("jwt_token", jsonData.token);
    pm.environment.set("username", jsonData.username);
    pm.environment.set("rolename", jsonData.rolename);
    console.log("✅ Token guardado exitosamente");
}
```

**Refresh Token (Test Script)**:
```javascript
// Actualiza el token automáticamente
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("jwt_token", jsonData.token);
    console.log("✅ Token renovado exitosamente");
}
```

### 📋 Ejemplos de Respuesta

Cada endpoint incluye ejemplos de:
- ✅ Respuesta exitosa
- ❌ Respuesta de error

## 🐳 Uso con Docker

Si estás usando Docker, la URL por defecto ya está configurada:
```
http://localhost:9090
```

Si tu backend corre en otro puerto, cambia la variable `base_url` en Postman.

## 🔐 Autenticación JWT

Los endpoints protegidos (3 y 4) están configurados para usar **Bearer Token** automáticamente con la variable `{{jwt_token}}`.

No necesitas configurar nada manualmente - solo ejecuta el Login y los demás endpoints funcionarán.

## 💡 Tips

1. **Ver el token actual**: Mira la consola de Postman después de hacer Login o Refresh
2. **Ver headers de seguridad**: Click en la pestaña **Headers** en la respuesta
3. **Cambiar ambiente**: Puedes crear diferentes ambientes (Dev, Prod) con diferentes URLs
4. **Depuración**: La consola de Postman muestra los scripts ejecutados y los valores guardados
5. **Ejecutar todos los tests**: Click en la colección → Click en **Run** para ejecutar todos los endpoints secuencialmente

## 📺 Guía Paso a Paso

### Primer Uso:
1. **Importar colección** → Archivo JSON
2. **Abrir endpoint "1. Login"**
3. **Click en Send** (sin modificar nada)
4. **Ver respuesta en Body** → Verás el token
5. **Click en Headers** (al lado de Body) → Verás los headers de seguridad
6. **Click en Test Results** → Verás tests pasando ✓

### Ver Headers de Seguridad:
```
Pasos detallados:
1. Send en cualquier endpoint
2. Busca las pestañas debajo del botón Send
3. Click en "Headers" (al lado de "Body")
4. Scroll down para ver todos los headers
5. Busca:
   - X-XSS-Protection
   - X-Frame-Options
   - Content-Security-Policy
   - Referrer-Policy
```

## ⚠️ Notas

- Asegúrate de que el backend esté corriendo antes de probar los endpoints
- Los tokens JWT expiran después de 24 horas
- Si recibes error 401, probablemente necesites hacer Login nuevamente
