package com.docusign.core.model.manifestModels;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CodeExampleText {
    public int ExampleNumber;

    public String ExampleName;

    public String CFREnabled;

    public String ExampleDescription;

    public String Notes;

    public String ResultsPageText;

    public String SkipForLanguages;

    public List<LinkToAPIMethods> LinksToAPIMethod = new ArrayList<>();

    public List<CustomErrorTexts> CustomErrorTexts = new ArrayList<>();

    public List<RedirectsToOtherCodeExamples> RedirectsToOtherCodeExamples = new ArrayList<>();

    public List<Forms> Forms = new ArrayList<>();

    public List<AdditionalPage> AdditionalPage = new ArrayList<>();
}
