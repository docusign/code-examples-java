package com.docusign.controller.eSignature.examples;

import com.docusign.DSConfiguration;
import com.docusign.common.WorkArguments;
import com.docusign.core.model.DoneExample;
import com.docusign.core.model.Session;
import com.docusign.core.model.User;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This topic demonstrates how to create an envelope where the workflow
 * is paused before the envelope is sent to a second recipient.
 */
@Controller
@RequestMapping("/eg034")
public class EG034ControllerUseConditionalRecipients extends AbstractEsignatureController {

    private static final String DOCUMENT_FILE_NAME = "Welcome1.txt";
    private static final String DOCUMENT_ID = "1";
    private static final String DOCUMENT_NAME = "Welcome";
    private static final String NOT_ALLOWED_ERROR_MESSAGE = "Update to the workflow with recipient routing is not allowed";

    private final Session session;
    private final User user;


    @Autowired
    public EG034ControllerUseConditionalRecipients(DSConfiguration config, Session session, User user) {
        super(config, "eg034", "Use conditional recipients.");
        this.session = session;
        this.user = user;
    }

    @Override
    // ***DS.snippet.0.start
    protected Object doWork(WorkArguments args, ModelMap model, HttpServletResponse response) throws ApiException, IOException {

        // Step 2: Construct your API headers
        EnvelopesApi envelopesApi = createEnvelopesApi(this.session.getBasePath(), this.user.getAccessToken());

        // Step 3: Construct your envelope JSON body
        EnvelopeDefinition envelope = createEnvelope(args);

        // Step 4: Call the eSignature REST API
        try {
            EnvelopeSummary results = envelopesApi.createEnvelope(this.session.getAccountId(), envelope);
            this.session.setEnvelopeId(results.getEnvelopeId());
            DoneExample.createDefault(this.title)
                    .withJsonObject(results)
                    .withMessage(
                            "An envelope where the workflow is routed to different recipients based on the value of a transaction " +
                                    "has been created and sent!<br/>Envelope ID "
                                    + this.session.getEnvelopeId() + ".")
                    .addToModel(model);
        } catch (ApiException apiException) {
            if (!apiException.getMessage().contains(NOT_ALLOWED_ERROR_MESSAGE)) {
                throw apiException;
            }
            DoneExample.createDefault(this.title)
                    .withMessage(NOT_ALLOWED_ERROR_MESSAGE + " for your account!<br/> " +
                            "Please contact with our <a target='_blank' href='https://developers.docusign.com/support'> support team </a> " +
                            "to resolve this issue.")
                    .addToModel(model);
        }

        return DONE_EXAMPLE_PAGE;
    }

    private static EnvelopeDefinition createEnvelope(WorkArguments args) throws IOException {
        Document document = EnvelopeHelpers.createDocumentFromFile(DOCUMENT_FILE_NAME, DOCUMENT_NAME, DOCUMENT_ID);

        WorkflowStep workflowStep = new WorkflowStep();
        workflowStep.setAction(EnvelopeHelpers.WORKFLOW_STEP_ACTION_PAUSE);
        workflowStep.setTriggerOnItem(EnvelopeHelpers.WORKFLOW_TRIGGER_ROUTING_ORDER);
        workflowStep.setItemId("2");
        workflowStep.setStatus("pending");
        workflowStep.setRecipientRouting(createRecipientRouting(args));

        Workflow workflow = new Workflow();
        workflow.setWorkflowSteps(Collections.singletonList(workflowStep));

        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(
                createApprovalSigner(document.getDocumentId()),
                createPurchaserSigner(args, document.getDocumentId())
        ));

        EnvelopeDefinition envelope = new EnvelopeDefinition();
        envelope.setEmailSubject("ApproveIfChecked");
        envelope.setDocuments(Collections.singletonList(document));
        envelope.setStatus(EnvelopeHelpers.ENVELOPE_STATUS_SENT);
        envelope.setRecipients(recipients);
        envelope.setWorkflow(workflow);

        return envelope;
    }

    private static Signer createPurchaserSigner(WorkArguments args, String documentId) {
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
        signer.setName(args.getSignerName());
        signer.setEmail(args.getSignerEmail());
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

    private static RecipientGroup createRecipientGroup(WorkArguments args) {
        RecipientOption recipientOption1 = new RecipientOption();
        recipientOption1.setRecipientLabel("signer2a");
        recipientOption1.setName(args.getSignerNotCheckedName());
        recipientOption1.setRoleName("Signer when not checked");
        recipientOption1.setEmail(args.getSignerNotCheckedEmail());

        RecipientOption recipientOption2 = new RecipientOption();
        recipientOption2.setRecipientLabel("signer2b");
        recipientOption2.setName(args.getSignerCheckedName());
        recipientOption2.setRoleName("Signer when checked");
        recipientOption2.setEmail(args.getSignerCheckedEmail());

        RecipientGroup recipientGroup = new RecipientGroup();
        recipientGroup.setGroupName("Approver");
        recipientGroup.setGroupMessage("Members of this group approve a workflow");
        recipientGroup.setRecipients(Arrays.asList(recipientOption1, recipientOption2));
        return recipientGroup;
    }

    private static ConditionalRecipientRule createConditionalRecipientRule(WorkArguments args) {
        ConditionalRecipientRule conditionalRecipientRule = new ConditionalRecipientRule();
        conditionalRecipientRule.setRecipientId("2");
        conditionalRecipientRule.setOrder("0");
        conditionalRecipientRule.setRecipientGroup(createRecipientGroup(args));
        conditionalRecipientRule.setConditions(createConditionalRecipientRuleConditions());
        return conditionalRecipientRule;
    }

    private static RecipientRouting createRecipientRouting(WorkArguments args) {
        RecipientRules recipientRules = new RecipientRules();
        recipientRules.setConditionalRecipients(Collections.singletonList(createConditionalRecipientRule(args)));

        RecipientRouting recipientRouting = new RecipientRouting();
        recipientRouting.setRules(recipientRules);
        return recipientRouting;
    }

    // ***DS.snippet.0.end
}