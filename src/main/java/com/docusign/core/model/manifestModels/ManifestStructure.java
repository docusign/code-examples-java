package com.docusign.core.model.manifestModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ManifestStructure {
    public List<ManifestGroup> Groups = new ArrayList<ManifestGroup>();
}
