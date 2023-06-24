package ch.nostromo.tiffanys.dragonborn.engine.search;

/** Search listener for async search */
public interface SearchListener {

    /** Update for every depth searched */
    void onDepthCompleted(SearchResult searchResult);

    /** final result */
    void onSearchFinished(SearchResult searchResult);
}
