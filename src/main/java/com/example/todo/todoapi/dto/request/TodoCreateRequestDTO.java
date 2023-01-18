// TODO: 2023-01-18  
package com.example.todo.todoapi.dto.request;

import com.example.todo.todoapi.entity.TodoEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
//@Data  // -> 위의 4줄을 한번에 사용할 수 있지만 유지보수(custom)에 대비해서 사용 지양
@Builder
public class TodoCreateRequestDTO {

    @NotBlank
    @Size(min = 2, max = 10)
    private String title;

    // 이 dto 를 엔터티로 변환
    public TodoEntity toEntity() {
        return TodoEntity.builder()
                .title(this.title)
                .build();
    }
}
