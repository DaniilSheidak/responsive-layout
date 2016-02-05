package com.responsive.layout.sling;



import com.responsive.layout.Page;
import com.responsive.layout.QBSearch;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;


/**
 * Created by daniil.sheidak on 01.02.2016.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy= DefaultInjectionStrategy.OPTIONAL)
public class SearchComponent {

    @Inject
    private String title;

    @Inject
    private String tagName;

    @Inject
    private String titleStyle;

    @Inject
    private Integer elementsNumber;

    @Inject
    private String searchPath;

    @Inject
    private String sortDirection;

    @Inject
    private QBSearch qbSearch;

    private List<Page> results;


    @PostConstruct
    public void activate() {
        if(qbSearch != null) {
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

    public List<Page> getResults() {
        return results;
    }

    public String getSearchPath() {
        return searchPath;
    }

    public String getSortDirection() {
        return sortDirection;
    }
}
