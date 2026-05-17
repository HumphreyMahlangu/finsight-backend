package com.finsight.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class EmbeddingService {

    private final QdrantClient qdrantClient;
    private final String collectionName;
    private final OpenAiEmbeddingModel embeddingModel;

    private static final int VECTOR_SIZE = 1536;

    public EmbeddingService(
            QdrantClient qdrantClient,
            @Value("${qdrant.collection-name}") String collectionName,
            @Value("${langchain4j.open-ai.api-key}") String openAiApiKey
    ) {
        this.qdrantClient = qdrantClient;
        this.collectionName = collectionName;
        this.embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(openAiApiKey)
                .modelName("text-embedding-3-small")
                .httpClientBuilder(new dev.langchain4j.http.client.jdk.JdkHttpClientBuilder())
                .build();
    }

    @PostConstruct
    public void initialiseCollection() {
        try {
            var collections = qdrantClient.listCollectionsAsync().get();
            boolean exists = collections.stream()
                    .anyMatch(c -> c.equals(collectionName));

            if (!exists) {
                qdrantClient.createCollectionAsync(
                        io.qdrant.client.grpc.Collections.CreateCollection.newBuilder()
                                .setCollectionName(collectionName)
                                .setVectorsConfig(
                                        Collections.VectorsConfig.newBuilder()
                                                .setParams(Collections.VectorParams.newBuilder()
                                                        .setSize(VECTOR_SIZE)
                                                        .setDistance(Collections.Distance.Cosine)
                                                        .build())
                                                .build()
                                )
                                .build()
                ).get();
                log.info("Created Qdrant collection: {}", collectionName);
            } else {
                log.info("Qdrant collection already exists: {}", collectionName);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to initialise Qdrant collection: " + e.getMessage(), e);
        }
    }

    public List<Float> embedText(String text) {
        Embedding embedding = embeddingModel.embed(text).content();
        return embedding.vectorAsList();
    }

    public List<List<Float>> embedBatch(List<String> texts) {
        return texts.stream()
                .map(this::embedText)
                .toList();
    }
}