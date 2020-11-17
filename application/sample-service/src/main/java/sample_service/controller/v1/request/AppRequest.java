package sample_service.controller.v1.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AppRequest {

    @NotNull
    private Integer id;

    @NotNull
    private String name;
}
