# Autenticação JWT com Spring Boot 3

Este projeto implementa autenticação segura com JWT (JSON Web Token) usando Spring Boot 3 e Spring Security 6.
Ele inclui funcionalidades como login, registro de usuários com papéis (roles), recuperação de senha e proteção de rotas.

## Tecnologias Utilizadas

- Java 17+
- Spring Boot 3
- Spring Security 6
- JWT (JSON Web Token)
- Spring Data JPA
- MySQL
- JUnit 5 e Mockito para testes

## Funcionalidades

### 1. Autenticação com JWT

- Serviço para geração de tokens JWT.
- Filtro de autenticação para validar tokens em requisições protegidas.
- Configuração do Spring Security para proteger rotas.

### 2. Endpoint de Login

- Valida credenciais do usuário.
- Gera e retorna um JWT caso as credenciais sejam válidas.

### 3. Recuperação de Senha

- Endpoint para solicitar a redefinição de senha (envio de e-mail com token de recuperação).
- Endpoint para alterar a senha usando o token recebido.

### 4. Proteção de Rotas

- Apenas usuários autenticados podem acessar determinadas rotas.
- Implementação de diferentes níveis de acesso (admin, usuário comum).

## Instalação e Execução

### 1. Clonar o repositório

```sh
 git clone https://github.com/MurilloNS/login-auth.git
```

### 2. Configurar o banco de dados

Altere o arquivo `application.yml` para configurar seu banco de dados:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/login-auth
    username: root
    password: senha
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

  security:
    jwt:
      secret-key: "my-very-secure-secret-key-EXACTLY-32-CHARS"
      expiration-time: 3600000
      password-reset-expiration: 900000

  mail:
    host: smtp.gmail.com
    port: 587
    username: "username"
    password: "password"
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

```

### 3. Executar o projeto

```sh
mvn spring-boot:run
```

## Testes

Para executar os testes unitários com JUnit 5 e Mockito, use:

```sh
mvn test
```

## Endpoints Principais

### Autenticação

- `POST api/auth/login` - Autentica um usuário e retorna um token JWT.
- `POST api/auth/register` - Registra um novo usuário.

### Recuperação de senha
- `POST /api/password-reset/request` - Solicita a recuperação de senha.
- `POST /api/password-reset/confirm` - Redefine a senha usando o token de recuperação.

### Usuários

- `GET api/user` - Lista todos os usuários (requer permissão de admin).
- `GET api/user/me` - Obtém detalhes do usuário autenticado.

## Contribuição

1. Fork este repositório.
2. Crie uma branch (`git checkout -b minha-feature`).
3. Commit suas modificações (`git commit -m 'Minha nova feature'`).
4. Envie para o repositório (`git push origin minha-feature`).
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

