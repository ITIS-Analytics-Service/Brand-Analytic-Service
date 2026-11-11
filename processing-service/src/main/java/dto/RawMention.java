package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RawMention {
    @NotNull
    private UUID brandId;

    @NotBlank
    private String sourceId;

    private String externalId;
    private UUID jobId;

    @NotBlank
    private String url;

    private String title;

    @NotBlank
    private String text;
    private String author;
    private Instant publishedAt;
    @NotNull Instant collectedAt;
}
