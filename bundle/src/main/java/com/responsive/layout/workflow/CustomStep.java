package com.responsive.layout.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.tagging.InvalidTagFormatException;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        log.info("Starting CustomStep...");
        ResourceResolver resolver = null;
        try {
            resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        } catch (LoginException e) {
            log.error(e.getMessage());
            return;
        }
        String payloadPath = workItem.getWorkflowData().getPayload().toString();
        Resource resource = resolver.getResource(payloadPath);
        Asset asset = DamUtil.resolveToAsset(resource);
        String assetName = asset.getName();
        String[] keyWords = (String[]) metaDataMap.get("keyWords");
        for(String keyWord : keyWords) {
            if(assetName.contains(keyWord)){
                String newTagTitle = (String) metaDataMap.get("tag");
                addTag(newTagTitle, asset);
            }
        }
        resolver.close();
    }

    private void addTag(String newTagTitle, Asset asset){
        ResourceResolver resolver = null;
        try {
            resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        } catch (LoginException e) {
            log.error(e.getMessage());
            return;
        }
        String path = asset.getPath();
        Resource metadataResource = resolver.getResource(path + "/jcr:content/metadata");
        TagManager tagManager = resolver.adaptTo(TagManager.class);
        try {
            Tag newTag = tagManager.createTagByTitle(newTagTitle);
            Tag[] tags = tagManager.getTags(metadataResource);
            List<Tag> tagsList = new ArrayList<>(Arrays.asList(tags));
            if(!tagsList.contains(newTag)){
                tagsList.add(newTag);
            }
            Tag[] updatedTags = new Tag[tagsList.size()];
            tagsList.toArray(updatedTags);
            tagManager.setTags(metadataResource, updatedTags);
        } catch (InvalidTagFormatException e) {
            log.error(e.getMessage());
        } finally {
            resolver.close();
        }
    }
}
