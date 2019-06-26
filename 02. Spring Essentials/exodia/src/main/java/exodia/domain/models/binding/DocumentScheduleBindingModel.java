package exodia.domain.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class DocumentScheduleBindingModel {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
