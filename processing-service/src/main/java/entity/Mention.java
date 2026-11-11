package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mentions")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "brand_id", nullable = false)
    private UUID brandId;

    @Column(name = "source_id", nullable = false, length = 100)
    private String sourceId;

    @Column(name = "external_id", length = 500)
    private String externalId;

    @Column(name = "job_id")
    private UUID jobId;

    @Column(nullable = false, columnDefinition = "text")
    private String url;

    @Column(columnDefinition = "text")
    private String title;

    @Column(columnDefinition = "text")
    private String text;

    @Column(length = 255)
    private String author;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "collected_at", nullable = false)
    private Instant collectedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sentiment sentiment = Sentiment.NEUTRAL;

    @Column(name = "sentiment_score")
    private Float sentimentScore;

    @Column(name = "model_version", length = 50)
    private String modelVersion;

    @Column(length = 255)
    private String topic;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt = Instant.now();

}
