-- ===== Editoras =====
INSERT INTO editora (nome, cidade, pais) VALUES
('Companhia das Letras', 'São Paulo', 'Brasil'),
('Penguin Random House', 'Londres', 'Reino Unido'),
('HarperCollins', 'Nova York', 'Estados Unidos');

-- ===== Autores =====
INSERT INTO autor (nome, nacionalidade, data_nascimento) VALUES
('Machado de Assis', 'Brasileiro', '1839-06-21'),
('Clarice Lispector', 'Brasileira', '1920-12-10'),
('George Orwell', 'Britânico', '1903-06-25'),
('J.K. Rowling', 'Britânica', '1965-07-31'),
('Gabriel García Márquez', 'Colombiano', '1927-03-06');

-- ===== Categorias =====
INSERT INTO categoria (nome, descricao) VALUES
('Ficção', 'Obras de ficção em geral'),
('Romance', 'Romances literários'),
('Distopia', 'Ficção distópica'),
('Fantasia', 'Literatura fantástica'),
('Realismo Mágico', 'Subgênero literário'),
('Clássico', 'Obras consideradas clássicas');

-- ===== Livros =====
INSERT INTO livro (titulo, isbn, ano_publicacao, numero_paginas, preco, editora_id) VALUES
('Dom Casmurro', '9788535910606', 1899, 256, 39.90, 1),
('A Hora da Estrela', '9788535914849', 1977, 87, 34.90, 1),
('1984', '9780451524935', 1949, 328, 49.90, 2),
('A Revolução dos Bichos', '9780141036137', 1945, 112, 29.90, 2),
('Harry Potter e a Pedra Filosofal', '9780747532699', 1997, 223, 59.90, 2),
('Cem Anos de Solidão', '9780060883287', 1967, 417, 54.90, 3),
('O Amor nos Tempos do Cólera', '9780307389732', 1985, 348, 47.90, 3),
('Memórias Póstumas de Brás Cubas', '9788535910590', 1881, 208, 36.90, 1);

-- ===== Livro x Autor (M:N) =====
INSERT INTO livro_autor (livro_id, autor_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 3),
(5, 4),
(6, 5),
(7, 5),
(8, 1);

-- ===== Livro x Categoria (M:N) =====
INSERT INTO livro_categoria (livro_id, categoria_id) VALUES
(1, 1), (1, 2), (1, 6),
(2, 1), (2, 2),
(3, 1), (3, 3), (3, 6),
(4, 1), (4, 3),
(5, 1), (5, 4),
(6, 1), (6, 5), (6, 6),
(7, 1), (7, 2), (7, 5),
(8, 1), (8, 6);
