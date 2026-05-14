-- Seed idempotente: INSERT IGNORE evita erro quando o boot reaproveita
-- um banco que já contém esses registros (ddl-auto=update não dropa tabelas).
-- Para resetar tudo do zero: DROP DATABASE biblioteca; CREATE DATABASE biblioteca;

-- ===== Editoras =====
INSERT IGNORE INTO editora (id, nome, cidade, pais) VALUES
(1, 'Companhia das Letras', 'São Paulo', 'Brasil'),
(2, 'Penguin Random House', 'Londres', 'Reino Unido'),
(3, 'HarperCollins', 'Nova York', 'Estados Unidos');

-- ===== Autores =====
INSERT IGNORE INTO autor (id, nome, nacionalidade, data_nascimento) VALUES
(1, 'Machado de Assis', 'Brasileiro', '1839-06-21'),
(2, 'Clarice Lispector', 'Brasileira', '1920-12-10'),
(3, 'George Orwell', 'Britânico', '1903-06-25'),
(4, 'J.K. Rowling', 'Britânica', '1965-07-31'),
(5, 'Gabriel García Márquez', 'Colombiano', '1927-03-06');

-- ===== Categorias =====
INSERT IGNORE INTO categoria (id, nome, descricao) VALUES
(1, 'Ficção', 'Obras de ficção em geral'),
(2, 'Romance', 'Romances literários'),
(3, 'Distopia', 'Ficção distópica'),
(4, 'Fantasia', 'Literatura fantástica'),
(5, 'Realismo Mágico', 'Subgênero literário'),
(6, 'Clássico', 'Obras consideradas clássicas');

-- ===== Livros =====
INSERT IGNORE INTO livro (id, titulo, isbn, ano_publicacao, numero_paginas, preco, editora_id) VALUES
(1, 'Dom Casmurro', '9788535910606', 1899, 256, 39.90, 1),
(2, 'A Hora da Estrela', '9788535914849', 1977, 87, 34.90, 1),
(3, '1984', '9780451524935', 1949, 328, 49.90, 2),
(4, 'A Revolução dos Bichos', '9780141036137', 1945, 112, 29.90, 2),
(5, 'Harry Potter e a Pedra Filosofal', '9780747532699', 1997, 223, 59.90, 2),
(6, 'Cem Anos de Solidão', '9780060883287', 1967, 417, 54.90, 3),
(7, 'O Amor nos Tempos do Cólera', '9780307389732', 1985, 348, 47.90, 3),
(8, 'Memórias Póstumas de Brás Cubas', '9788535910590', 1881, 208, 36.90, 1);

-- ===== Livro x Autor (M:N) =====
INSERT IGNORE INTO livro_autor (livro_id, autor_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 3),
(5, 4),
(6, 5),
(7, 5),
(8, 1);

-- ===== Livro x Categoria (M:N) =====
INSERT IGNORE INTO livro_categoria (livro_id, categoria_id) VALUES
(1, 1), (1, 2), (1, 6),
(2, 1), (2, 2),
(3, 1), (3, 3), (3, 6),
(4, 1), (4, 3),
(5, 1), (5, 4),
(6, 1), (6, 5), (6, 6),
(7, 1), (7, 2), (7, 5),
(8, 1), (8, 6);
