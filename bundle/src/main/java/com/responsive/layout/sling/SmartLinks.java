package com.responsive.layout.sling;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Path;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by daniil.sheidak on 11.02.2016.
 */
@Model(adaptables = Resource.class)
public class SmartLinks {

    @Inject
    @Optional
    private String title;

    @Inject
    @Optional
    private String href;

    @Inject
    @Optional
    private String target;


    public String getTitle() {
        return title;
    }

    public String getHref() {
        return href;
    }

    public String getTarget() {
        return target;
    }

    @Self
    Resource resource;

    @PostConstruct
    public void activate() {
        String s = "";
    }
}
