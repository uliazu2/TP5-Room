# 📱 UserApp — CRUD com Room Database

Aplicativo Android completo com autenticação e gerenciamento de perfil de usuário usando **Room Database**, **ViewModel**, **LiveData** e **Navigation Component**.

---

## 🗂️ Estrutura do Projeto

```
app/src/main/java/com/example/userapp/
├── data/
│   ├── entity/        → User.kt              (tabela Room)
│   ├── dao/           → UserDao.kt           (queries SQL)
│   ├── database/      → AppDatabase.kt       (instância Room)
│   └── repository/    → UserRepository.kt    (camada de dados)
├── viewmodel/         → UserViewModel.kt     (lógica de negócio)
├── ui/
│   ├── login/         → LoginFragment.kt
│   ├── register/      → RegisterFragment.kt
│   ├── home/          → HomeFragment.kt
│   └── profile/       → ProfileFragment.kt
├── SessionManager.kt                         (gerencia sessão)
└── MainActivity.kt
```

---

## ✨ Funcionalidades

| Tela        | O que faz                                               |
|-------------|--------------------------------------------------------|
| **Login**   | Autentica com email + senha, persiste sessão            |
| **Cadastro**| Cria novo usuário (nome, email, telefone, data nasc.)   |
| **Home**    | Exibe dados do usuário logado em tempo real (LiveData)  |
| **Perfil**  | Editar todos os campos + trocar senha + excluir conta   |

---

## 🏗️ Arquitetura

```
Fragment → ViewModel → Repository → DAO → Room (SQLite)
```

- **Room** → persistência local com SQLite
- **ViewModel** → sobrevive a rotações de tela
- **LiveData** → atualiza UI automaticamente
- **Navigation Component** → gerencia pilha de telas
- **ViewBinding** → acesso seguro às views
- **Coroutines** → operações assíncronas sem bloquear a UI

---

## 🚀 Como Abrir no Android Studio

### Passo 1 — Importar o Projeto
1. Abra o **Android Studio**
2. Clique em **File → Open**
3. Navegue até a pasta `UserApp` e clique em **OK**
4. Aguarde o Gradle sincronizar (pode demorar alguns minutos na 1ª vez)

### Passo 2 — Verificar SDK
- **minSdk**: 24 (Android 7.0)
- **targetSdk**: 34 (Android 14)
- **compileSdk**: 34
- Instale o SDK necessário via **SDK Manager** se solicitado

### Passo 3 — Executar
- Conecte um dispositivo ou crie um **AVD (Emulador)**
- Clique no botão ▶️ **Run**

---

## 📦 Dependências Principais

```gradle
// Room Database
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'

// Navigation
implementation 'androidx.navigation:navigation-fragment-ktx:2.7.6'
implementation 'androidx.navigation:navigation-ui-ktx:2.7.6'

// ViewModel & LiveData
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

// Material Design
implementation 'com.google.android.material:material:1.11.0'
```

---

## 🗄️ Esquema do Banco de Dados

```sql
CREATE TABLE users (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT    NOT NULL,
    email       TEXT    NOT NULL,
    phone       TEXT    NOT NULL,
    password    TEXT    NOT NULL,
    birth_date  TEXT    DEFAULT '',
    created_at  INTEGER DEFAULT (current_timestamp)
);
```

---

## 📝 Observações

- As senhas são armazenadas como texto simples para fins didáticos. Em produção, use hash (ex: BCrypt)
- A sessão do usuário é mantida via `SharedPreferences`
- Todos os ícones são **Vector Drawables** do Material Design — sem necessidade de imagens externas
# TP5-Room
