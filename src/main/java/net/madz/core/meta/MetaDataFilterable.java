package net.madz.core.meta;

public interface MetaDataFilterable {
    public MetaDataFilterable filter(MetaData parent, MetaDataFilter filter, boolean lazyFilter);

    public KeySet getKeySet();
}
