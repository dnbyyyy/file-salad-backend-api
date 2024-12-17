CREATE TABLE key_word (
                          id SERIAL PRIMARY KEY,
                          key_word VARCHAR(255) NOT NULL
);

CREATE TABLE saved_file (
                            id UUID PRIMARY KEY,
                            file_path TEXT NOT NULL,
                            key VARCHAR(255) NOT NULL,
                            file_name VARCHAR(255) NOT NULL,
                            created_at TIMESTAMP NOT NULL
);
