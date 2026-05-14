CREATE TABLE document_chunks (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    document_id   BIGINT NOT NULL,
    chunk_index   INT NOT NULL,
    chunk_text    TEXT NOT NULL,
    page_number   INT,
    char_offset   INT,
    token_count   INT,
    qdrant_id     VARCHAR(255),

    CONSTRAINT fk_chunks_document
        FOREIGN KEY (document_id) REFERENCES documents(id)
        ON DELETE CASCADE
);