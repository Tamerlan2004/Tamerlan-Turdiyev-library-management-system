-- V1__Create_library_tables.sql

-- 1. Users (для JWT авторизации)
CREATE TABLE users (
                       id              BIGSERIAL PRIMARY KEY,
                       username        VARCHAR(50)  NOT NULL UNIQUE,
                       email           VARCHAR(100) NOT NULL UNIQUE,
                       password        VARCHAR(255) NOT NULL,
                       role            VARCHAR(20)  NOT NULL DEFAULT 'USER',
                       created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Authors
CREATE TABLE authors (
                         id              BIGSERIAL PRIMARY KEY,
                         name            VARCHAR(100) NOT NULL,
                         biography       TEXT,
                         created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Books
CREATE TABLE books (
                       id                  BIGSERIAL PRIMARY KEY,
                       title               VARCHAR(200) NOT NULL,
                       isbn                VARCHAR(20) UNIQUE,
                       publication_year    INTEGER,
                       available           BOOLEAN DEFAULT TRUE,
                       author_id           BIGINT,
                       created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_book_author FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);

-- 4. Members
CREATE TABLE members (
                         id          BIGSERIAL PRIMARY KEY,
                         full_name   VARCHAR(100) NOT NULL,
                         email       VARCHAR(100) UNIQUE NOT NULL,
                         phone       VARCHAR(20),
                         created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Member Profiles
CREATE TABLE member_profiles (
                                 id              BIGSERIAL PRIMARY KEY,
                                 member_id       BIGINT UNIQUE NOT NULL,
                                 faculty         VARCHAR(100),
                                 group_name      VARCHAR(50),
                                 course          INTEGER,
                                 created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT fk_profile_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- 6. Loans
CREATE TABLE loans (
                       id              BIGSERIAL PRIMARY KEY,
                       member_id       BIGINT NOT NULL,
                       book_id         BIGINT NOT NULL,
                       loan_date       DATE DEFAULT CURRENT_DATE,
                       return_date     DATE,
                       returned        BOOLEAN DEFAULT FALSE,
                       created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_loan_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
                       CONSTRAINT fk_loan_book   FOREIGN KEY (book_id)   REFERENCES books(id)   ON DELETE CASCADE
);

-- Индексы
CREATE INDEX idx_books_author ON books(author_id);
CREATE INDEX idx_loans_member ON loans(member_id);
CREATE INDEX idx_loans_book   ON loans(book_id);
CREATE INDEX idx_users_email  ON users(email);
CREATE INDEX idx_users_username ON users(username);