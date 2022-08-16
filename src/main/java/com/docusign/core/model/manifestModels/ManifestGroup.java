package com.docusign.core.model.manifestModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ManifestGroup {
    public List<CodeExampleText> Examples = new ArrayList<>();

    public String Name;
}
