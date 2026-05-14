package com.finsight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "stored_filename", nullable = false)
    private String storedFilename;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "page_count")
    private Integer pageCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "fiscal_year")
    private Integer fiscalYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "minio_key")
    private String minioKey;

    @Column(name = "chunk_count", nullable = false)
    private Integer chunkCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentChunk> chunks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = Status.PENDING;
        if (chunkCount == null) chunkCount = 0;
        if (documentType == null) documentType = DocumentType.OTHER;
    }

    public enum DocumentType {
        ANNUAL_REPORT, EARNINGS, REGULATORY, AUDIT, LOAN, OTHER
    }

    public enum Status {
        PENDING, PROCESSING, PROCESSED, FAILED
    }
}