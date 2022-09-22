package com.docusign.core.model.manifestModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Forms {
    public String FormName;

    public List<Inputs> Inputs = new ArrayList<>();
}
