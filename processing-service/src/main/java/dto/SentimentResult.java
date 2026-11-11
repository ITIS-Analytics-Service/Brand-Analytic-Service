package dto;

import entity.Sentiment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SentimentResult {
    private Sentiment label;
    private Float score;
    private String modelVersion;
}
