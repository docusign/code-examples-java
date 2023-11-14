package com.docusign.controller.eSignature.services;

import com.docusign.controller.eSignature.examples.EnvelopeHelpers;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UseConditionalRecipientsService {
    private static final String DOCUMENT_FILE_NAME = "Welcome1.txt";

    private static final String DOCUMENT_ID = "1";

    private static final String DOCUMENT_NAME = "Welcome";

    //ds-snippet-start:eSign34Step4
    public static EnvelopeSummary useConditionalRecipients(
            EnvelopesApi envelopesApi,
            String accountId,
            EnvelopeDefinition envelope
    ) throws ApiException {
        return envelopesApi.createEnvelope(accountId, envelope);
    }
    //ds-snippet-end:eSign34Step4

    //ds-snippet-start:eSign34Step3
    public static EnvelopeDefinition createEnvelope(
            String signerNotCheckedName,
            String signerNotCheckedEmail,
            String signerCheckedName,
            String signerCheckedEmail,
            String signerName,
            String signerEmail
    ) throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, DOCUMENT_ID);

        WorkflowStep workflowStep = new WorkflowStep();
        workflowStep.setAction(EnvelopeHelpers.WORKFLOW_STEP_ACTION_PAUSE);
        workflowStep.setTriggerOnItem(EnvelopeHelpers.WORKFLOW_TRIGGER_ROUTING_ORDER);
        workflowStep.setItemId("2");
        workflowStep.setStatus("pending");
        workflowStep.setRecipientRouting(createRecipientRouting(
                signerNotCheckedName,
                signerNotCheckedEmail,
                signerCheckedName,
                signerCheckedEmail
        ));

        Workflow workflow = new Workflow();
        workflow.setWorkflowSteps(Collections.singletonList(workflowStep));

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(
                createApprovalSigner(document.getDocumentId()),
                createPurchaserSigner(signerName, signerEmail, document.getDocumentId())
        ));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("ApproveIfChecked");
        envelope.setDocuments(Collections.singletonList(document));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);
        envelope.setWorkflow(workflow);

        return envelope;
    }

    private static Signer createPurchaserSigner(
            String signerName,
            String signerEmail,
            String documentId
    ) {
        SignHere signHere = new SignHere();
        signHere.setName("SignHereTabs");
        signHere.setXPosition("200");
        signHere.setYPosition("200");
        signHere.setTabLabel("PurchaserSignature");
        signHere.setPageNumber("1");
        signHere.setDocumentId(documentId);

        Checkbox checkbox = new Checkbox();
        checkbox.setName("ClickToApprove");
        checkbox.setSelected("false");
        checkbox.setXPosition("50");
        checkbox.setYPosition("50");
        checkbox.setTabLabel("ApproveWhenChecked");
        checkbox.setPageNumber("1");
        checkbox.setDocumentId(documentId);

        Tabs tabs = EnvelopeHelpers.createSignerTabs(signHere);
        tabs.setCheckboxTabs(Collections.singletonList(checkbox));

        Signer signer = new Signer();
        signer.setName(signerName);
        signer.setEmail(signerEmail);
        signer.setRoutingOrder("1");
        signer.setRecipientId("1");
        signer.setRoleName("Purchaser");
        signer.setTabs(tabs);

        return signer;
    }

    private static Signer createApprovalSigner(String documentId) {
        SignHere signHere = new SignHere();
        signHere.setName("SignHere");
        signHere.setRecipientId("2");
        signHere.setXPosition("200");
        signHere.setYPosition("300");
        signHere.setTabLabel("ApproverSignature");
        signHere.setPageNumber("1");
        signHere.setDocumentId(documentId);

        Signer signer = new Signer();
        signer.setName("Approver");
        signer.setEmail("placeholder@example.com");
        signer.setRoutingOrder("2");
        signer.setRecipientId("2");
        signer.setRoleName("Approver");
        signer.setTabs(EnvelopeHelpers.createSignerTabs(signHere));
        return signer;
    }

    private static List<ConditionalRecipientRuleCondition> createConditionalRecipientRuleConditions() {

        ConditionalRecipientRuleFilter filter1 = new ConditionalRecipientRuleFilter();
        filter1.setRecipientId("1");
        filter1.setScope("tabs");
        filter1.setTabId("ApprovalTab");
        filter1.setOperator("equals");
        filter1.setValue("false");
        filter1.setTabType("checkbox");
        filter1.setTabLabel("ApproveWhenChecked");

        ConditionalRecipientRuleFilter filter2 = new ConditionalRecipientRuleFilter();
        filter2.setRecipientId("1");
        filter2.setScope("tabs");
        filter2.setTabId("ApprovalTab");
        filter2.setOperator("equals");
        filter2.setValue("true");
        filter2.setTabType("checkbox");
        filter2.setTabLabel("ApproveWhenChecked");

        ConditionalRecipientRuleCondition condition1 = new ConditionalRecipientRuleCondition();
        condition1.setRecipientLabel("signer2a");
        condition1.setOrder("1");
        condition1.setFilters(Collections.singletonList(filter1));

        ConditionalRecipientRuleCondition condition2 = new ConditionalRecipientRuleCondition();
        condition2.setRecipientLabel("signer2b");
        condition2.setOrder("2");
        condition2.setFilters(Collections.singletonList(filter2));
        return Arrays.asList(condition1, condition2);
    }

    private static RecipientGroup createRecipientGroup(
            String signerNotCheckedName,
            String signerNotCheckedEmail,
            String signerCheckedName,
            String signerCheckedEmail
    ) {
        RecipientOption recipientOption1 = new RecipientOption();
        recipientOption1.setRecipientLabel("signer2a");
        recipientOption1.setName(signerNotCheckedName);
        recipientOption1.setRoleName("Signer when not checked");
        recipientOption1.setEmail(signerNotCheckedEmail);

        RecipientOption recipientOption2 = new RecipientOption();
        recipientOption2.setRecipientLabel("signer2b");
        recipientOption2.setName(signerCheckedName);
        recipientOption2.setRoleName("Signer when checked");
        recipientOption2.setEmail(signerCheckedEmail);

        RecipientGroup recipientGroup = new RecipientGroup();
        recipientGroup.setGroupName("Approver");
        recipientGroup.setGroupMessage("Members of this group approve a workflow");
        recipientGroup.setRecipients(Arrays.asList(recipientOption1, recipientOption2));
        return recipientGroup;
    }

    private static ConditionalRecipientRule createConditionalRecipientRule(
            String signerNotCheckedName,
            String signerNotCheckedEmail,
            String signerCheckedName,
            String signerCheckedEmail
    ) {
        ConditionalRecipientRule conditionalRecipientRule = new ConditionalRecipientRule();
        conditionalRecipientRule.setRecipientId("2");
        conditionalRecipientRule.setOrder("0");
        conditionalRecipientRule.setRecipientGroup(createRecipientGroup(
                signerNotCheckedName,
                signerNotCheckedEmail,
                signerCheckedName,
                signerCheckedEmail
        ));
        conditionalRecipientRule.setConditions(createConditionalRecipientRuleConditions());
        return conditionalRecipientRule;
    }

    private static RecipientRouting createRecipientRouting(
            String signerNotCheckedName,
            String signerNotCheckedEmail,
            String signerCheckedName,
            String signerCheckedEmail
    ) {
        RecipientRules recipientRules = new RecipientRules();
        recipientRules.setConditionalRecipients(Collections.singletonList(createConditionalRecipientRule(
                signerNotCheckedName,
                signerNotCheckedEmail,
                signerCheckedName,
                signerCheckedEmail
        )));

        RecipientRouting recipientRouting = new RecipientRouting();
        recipientRouting.setRules(recipientRules);
        return recipientRouting;
    }
    //ds-snippet-end:eSign34Step3
}
