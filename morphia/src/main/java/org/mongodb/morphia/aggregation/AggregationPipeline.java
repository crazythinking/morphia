package org.mongodb.morphia.aggregation;


import org.mongodb.morphia.query.MorphiaIterator;

import java.util.List;

public interface AggregationPipeline<T, U> {
    AggregationPipeline<T, U> project(Projection... projections);
    
    AggregationPipeline<T, U> group(String id, Group... groupings);
    
    AggregationPipeline<T, U> group(List<Group> id, Group... groupings);

    MorphiaIterator<U, U> aggregate();

    AggregationPipeline<T, U> match(Matcher... criterion);
}