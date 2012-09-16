package net.madz.core.meta;

import net.madz.core.common.DottedPath;

public interface MetaData {
    DottedPath getDottedPath();

    MetaData getParent();
}
