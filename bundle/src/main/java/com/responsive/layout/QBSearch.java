package com.responsive.layout;

import java.util.List;

/**
 * Created by daniil.sheidak on 02.02.2016.
 */
public interface QBSearch {
    List<Page> getResults(String searchPath, String tagName, Integer elementsNumber, String sortDirection);
}
