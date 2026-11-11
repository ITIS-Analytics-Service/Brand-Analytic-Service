package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MentionResponse {
    private UUID mentionId;
    private UUID brandId;
    private String sourceId;
    private String url;
    private String title;
    private String snippet;
    private String author;
    private Instant publishedAt;
    private Instant collectedAt;
    private SentimentResult sentiment;
}
