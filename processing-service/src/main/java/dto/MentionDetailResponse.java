package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentionDetailResponse {
    private UUID mentionId;
    private UUID brandId;
    private String sourceId;
    private String externalId;
    private UUID jobId;
    private String url;
    private String title;
    private String text;
    private String author;
    private Instant publishedAt;
    private Instant collectedAt;
    private Instant processedAt;
    private SentimentResult sentiment;
    private List<String> themes;
}
