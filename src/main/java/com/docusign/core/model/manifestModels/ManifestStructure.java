package com.docusign.core.model.manifestModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ManifestStructure {
    public SupportingTexts SupportingTexts;

    public List<APIs> APIs = new ArrayList<APIs>();
}
