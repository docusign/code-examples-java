package com.docusign.core.model.manifestModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class APIs {
    public String Name;

    public List<ManifestGroup> Groups = new ArrayList<ManifestGroup>();
}
