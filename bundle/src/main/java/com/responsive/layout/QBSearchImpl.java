package com.responsive.layout;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.day.cq.dam.commons.util.DamUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;


/**
 * Created by daniil.sheidak on 02.02.2016.
 */
@Component (immediate = true)
@Service
public class QBSearchImpl implements QBSearch {
    private static final Logger LOG = LoggerFactory.getLogger(QBSearchImpl.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    public List<Image> getResults(String searchPath, String tagName, Integer elementsNumber, String sortDirection) {
        String logMessage = StringUtils.isNotBlank(tagName) ? "Searching pages by tag " + tagName + " started" : "Searching pages started";
        LOG.info(logMessage);
        List<Image> assetList = new ArrayList<>();
        ResourceResolver resolver = null;
        try {
            resolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
            QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);

            Map<String, String> predicates = new HashMap<String, String>();
            predicates.put("type", "dam:Asset");
            if(StringUtils.isNotBlank(searchPath)) {
                predicates.put("path", searchPath);
            }
            if(StringUtils.isNotBlank(tagName)) {
                predicates.put("fulltext", tagName);
                predicates.put("fulltext.relPath", "jcr:content/cq:tags");
            }

            if (StringUtils.isNotBlank(sortDirection)) {
                predicates.put("orderby", "@jcr:content/cq:lastModified");
                predicates.put("orderby.sort", sortDirection);
            }

            int start = 0;
            int limit = 5;
            if (elementsNumber != null) {
                limit = elementsNumber.intValue() > 0 ? elementsNumber : 5;
            }
            predicates.put("p.offset", Integer.toString(start));
            predicates.put("p.limit", Integer.toString(limit));

            Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), resolver.adaptTo(Session.class));
            SearchResult result = query.getResult();
            long totalMatches = result.getTotalMatches();
            LOG.info("total matches: " + totalMatches);

            List<Hit> hits = result.getHits();
            hits.forEach(hit -> {
                try {
                    Resource resource = hit.getResource();
                    Asset asset = DamUtil.resolveToAsset(resource);
                    Image image = new Image(asset.getRendition(DamConstants.PREFIX_ASSET_THUMBNAIL + ".100.100.png").getPath());
                    assetList.add(image);
                } catch (RepositoryException e) {
                    LOG.error(e.getMessage());
                }
            });
        } catch (LoginException e) {
            LOG.error(e.getMessage());
        } finally {
            if(resolver != null) {
                resolver.close();
            }
        }
        return assetList;
    }
}
