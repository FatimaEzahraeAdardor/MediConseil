package org.youcode.mediconseil.web.vm.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryResponseVM {
    private UUID id;
    private String name;
}
