# 📮 Colección de Postman - SIEGSI Autenticación

## 📥 Importar en Postman

1. Abre Postman
2. Click en **Import** (arriba a la izquierda)
3. Selecciona el archivo `SIEGSI-Auth-Postman-Collection.json`
4. ¡Listo! La colección aparecerá en tu workspace

## 🔑 Credenciales de Prueba

**Usuario Administrador (Incluido en la colección):**
- **Username**: `cDaroma`
- **Password**: `password`
- **Rol**: `ADMIN`

Las variables de la colección ya incluyen estas credenciales.

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
| `jwt_token` | (vacío) | Token JWT - se guarda automáticamente |
| `username` | (vacío) | Usuario autenticado |
| `rolename` | (vacío) | Rol del usuario |

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
2. **Cambiar ambiente**: Puedes crear diferentes ambientes (Dev, Prod) con diferentes URLs
3. **Depuración**: La consola de Postman muestra los scripts ejecutados y los valores guardados

## ⚠️ Notas

- Asegúrate de que el backend esté corriendo antes de probar los endpoints
- Los tokens JWT expiran después de 24 horas
- Si recibes error 401, probablemente necesites hacer Login nuevamente
