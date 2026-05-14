CREATE TABLE documents (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    original_name   VARCHAR(500) NOT NULL,
    stored_filename VARCHAR(500) NOT NULL,
    file_size_bytes BIGINT,
    page_count      INT,
    document_type   ENUM(
                        'ANNUAL_REPORT',
                        'EARNINGS',
                        'REGULATORY',
                        'AUDIT',
                        'LOAN',
                        'OTHER'
                    ) NOT NULL DEFAULT 'OTHER',
    company_name    VARCHAR(255),
    fiscal_year     INT,
    status          ENUM('PENDING', 'PROCESSING', 'PROCESSED', 'FAILED') NOT NULL DEFAULT 'PENDING',
    minio_key       VARCHAR(1000),
    chunk_count     INT NOT NULL DEFAULT 0,
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_documents_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
);