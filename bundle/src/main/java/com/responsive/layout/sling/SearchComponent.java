package com.responsive.layout.sling;


import com.responsive.layout.Image;
import com.responsive.layout.QBSearch;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;


/**
 * Created by daniil.sheidak on 01.02.2016.
 */
@Model(adaptables = Resource.class)
public class SearchComponent {

    @Inject
    @Optional
    private String title;

    @Inject
    @Optional
    private String tagName;

    @Inject
    @Optional
    private String titleStyle;

    @Inject
    @Optional
    private Integer elementsNumber;

    @Inject
    @Default(values = "/content/dam")
    private String searchPath;

    @Inject
    @Optional
    private String sortDirection;

    @Inject
    private QBSearch qbSearch;

    private List<Image> results;

    @Self
    Resource resource;

    @PostConstruct
    public void activate() {
        if(qbSearch != null) {
            if(StringUtils.isBlank(searchPath)) {
               searchPath = resource.getPath();
            }
            results = qbSearch.getResults(searchPath, tagName, elementsNumber, sortDirection);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getTitleStyle() {
        return titleStyle;
    }

    public String getTagName() {
        return tagName;
    }

    public Integer getElementsNumber() {
        return elementsNumber;
    }

    public List<Image> getResults() {
        return results;
    }

    public String getSearchPath() {
        return searchPath;
    }

    public String getSortDirection() {
        return sortDirection;
    }
}
