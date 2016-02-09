package com.responsive.layout.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.commons.util.DamUtil;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

@Component(immediate = true, metatype = true)
@Service
public class CustomStep implements WorkflowProcess {

    @Property(value = "My Custom Workflow.")
    static final String DESCRIPTION = Constants.SERVICE_DESCRIPTION;

    @Property(value = "Daniil Sheidak")
    static final String VENDOR = Constants.SERVICE_VENDOR;

    @Property(value = "My Sample Workflow Process")
    static final String LABEL="process.label";

    private static final Logger log = LoggerFactory.getLogger(CustomStep.class);

    @Self
    Resource resource;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        log.info("Starting CustomStep...");
//        DamUtil.resolveToAsset(resource);
        WorkflowData workflowData = workItem.getWorkflowData();
//        if (workflowData.getPayloadType().equals(TYPE_JCR_PATH)) {
            final String payloadPath = workItem.getWorkflowData().getPayload().toString();
            try {
                Session jcrSession = workflowSession.adaptTo(Session.class);
                Node node = (Node) jcrSession.getItem(payloadPath + "/" + JcrConstants.JCR_CONTENT);
//                if (node != null) {
//                    node.setProperty(PROPERTY_OLM_APPROVED, true);
//                    jcrSession.save();
//                }
            } catch (RepositoryException e) {
                log.error(e.getMessage(), e);
            }
//        }
    }
}
