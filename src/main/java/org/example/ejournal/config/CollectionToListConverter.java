package org.example.ejournal.config;

import org.modelmapper.AbstractConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionToListConverter<T> extends AbstractConverter<Collection<T>, List<T>> {
    @Override
    protected List<T> convert(Collection<T> source) {
        if (source == null) {
            return null;
        }

        return new ArrayList<>(source);
    }
}
