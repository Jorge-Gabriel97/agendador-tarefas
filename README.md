# 📅 Agendador de Tarefas — API RESTful

API RESTful desenvolvida em **Java 17** e **Spring Boot** para gerenciamento e agendamento de tarefas em uma arquitetura baseada em microsserviços, com persistência NoSQL em **MongoDB**, autenticação stateless via **JWT** e comunicação síncrona entre serviços com **Spring Cloud OpenFeign**.

---

## 🎯 Visão Geral

Em arquiteturas distribuídas, desacoplar a gestão de identidade e autenticação do domínio de negócios garante maior escalabilidade, segurança e facilidade de manutenção. O **Agendador de Tarefas** resolve a necessidade de gerenciar compromissos e eventos por usuário sem duplicar regras de autenticação: a aplicação valida tokens **JWT** em cada requisição, identifica automaticamente o usuário proprietário do recurso e persiste os dados de forma isolada no **MongoDB**.

---

## ⚙️ Funcionalidades

- **CRUD Completo de Tarefas:** Criação (`POST`), consulta (`GET`), atualização completa (`PUT`), atualização parcial (`PATCH`) e exclusão (`DELETE`).
- **Isolamento e Segurança por Usuário:** Extração automática do e-mail autenticado a partir do cabeçalho `Authorization: Bearer <token>` via `JwtUtil`, garantindo que cada usuário visualize e modifique apenas suas próprias tarefas.
- **Filtro Dinâmico por Período:** Consulta de eventos agendados dentro de um intervalo de data e hora (`LocalDateTime` no padrão ISO-8601).
- **Atualização Inteligente com MapStruct:**
  - `PUT`: Substituição completa dos campos mutáveis (`SET_TO_NULL`), protegendo metadados imutáveis (`id`, `dataCriacao`, `emailUsuario`).
  - `PATCH`: Atualização seletiva apenas dos atributos enviados no payload (`NullValuePropertyMappingStrategy.IGNORE`), eliminando condicionais repetitivas.
- **Tratamento de Exceções Customizadas:** Exceções de domínio (`ResourceNotFoundException` e `UnauthorizedException`) para respostas HTTP semânticas.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17
- **Framework Principal:** Spring Boot (Web, Validation)
- **Persistência NoSQL:** Spring Data MongoDB
- **Segurança:** Spring Security & JWT (JSON Web Token)
- **Comunicação entre Microsserviços:** Spring Cloud OpenFeign
- **Mapeamento DTO ↔ Entity:** MapStruct
- **Produtividade:** Lombok
- **Build & Dependências:** Gradle

---

## 🏗️ Arquitetura e Fluxo de Dados

O projeto segue uma arquitetura em camadas bem definidas (`controller`, `business`, `infrastructure`), aplicando o princípio **Follow the Data Flow** para rastreabilidade ponta a ponta:

1. **Entrada e Segurança (`Security Filter` + `TarefasController`):** A requisição chega na porta `8081` com o token JWT. O filtro valida a assinatura do token e libera o acesso ao endpoint `/tarefas`.
2. **Integração entre Serviços (`OpenFeign`):** Caso necessário validar dados cadastrais do usuário, o cliente Feign consulta o microsserviço `usuario` (executado na porta `8080`).
3. **Regra de Negócio e Mapeamento (`TarefaService` + `TarefaConverter`):** O serviço extrai o e-mail do token, valida a propriedade do documento no banco e utiliza o **MapStruct** para converter `TarefasDto` em `TarefasEntity` (e vice-versa).
4. **Persistência (`TarefasRepository` + `MongoDB`):** Os documentos são armazenados e consultados na instância MongoDB (`localhost:27017`).

### 🧩 Desafios Técnicos Resolvidos

- **Atualizações Parciais sem Boilerplate:** Substituição de múltiplos blocos `if (campo != null)` por `@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)` com `@MappingTarget` no `TarefaConverter`, mantendo o `Service` limpo e focado nas regras de negócio.
- **Proteção de Integridade em Operações `PUT` e `PATCH`:** Uso explícito de `@Mapping(target = "...", ignore = true)` para impedir a alteração indevida de `id`, `dataCriacao` e `emailUsuario` durante edições.
- **Coexistência de Microsserviços:** Isolamento de portas (`8080` para o serviço de usuários e `8081` para o agendador de tarefas) e configuração stateless no Spring Security para evitar bloqueios de CSRF em requisições REST.

---

## 🔌 Endpoints da API

Base URL: `http://localhost:8081/tarefas` *(Todos os endpoints exigem o header `Authorization: Bearer <seu_token_jwt>`)*

| Método | Rota | Descrição | Status de Sucesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/tarefas` | Cria uma nova tarefa vinculada ao usuário do token | `200 OK` |
| `GET` | `/tarefas` | Lista todas as tarefas do usuário autenticado | `200 OK` |
| `GET` | `/tarefas/eventos` | Filtra tarefas por período (`?dataInicial=...&dataFinal=...`) | `200 OK` |
| `PUT` | `/tarefas/{id}` | Atualiza todos os dados mutáveis de uma tarefa | `200 OK` |
| `PATCH` | `/tarefas/{id}` | Atualiza parcialmente apenas os campos enviados | `200 OK` |
| `DELETE` | `/tarefas/{id}` | Remove uma tarefa pelo seu ID | `204 No Content` |

### Exemplo de Payload (`POST` / `PUT`)

```json
{
  "nomeTarefa": "Reunião de Alinhamento Arquitetural",
  "descricao": "Revisar contratos de API e integração OpenFeign",
  "dataEvento": "2026-12-05T14:30:00"
}
