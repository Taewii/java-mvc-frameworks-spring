package exodia.domain.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDetailsViewModel {

    private UUID id;
    private String title;
    private String content;
}
