// TODO: 2023-01-18  
package com.example.todo.todoapi.dto.request;

import lombok.*;

import javax.validation.constraints.*;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class TodoModifyRequestDTO {

    @NotBlank
    @Size(min = 2, max = 10)
    private String title;
    private boolean done;
}
