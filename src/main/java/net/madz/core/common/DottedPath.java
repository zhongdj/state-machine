package net.madz.core.common;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a hierarchical name in which each level is separated by a dot.
 * This class is loosely modeled after java.io.File
 */
public class DottedPath implements Iterable<DottedPath> {
    private final DottedPath parent;
    private final String name, absoluteName;

    private DottedPath(DottedPath parent, String name) {
        this.parent = parent;
        this.name = name;
        this.absoluteName = this.parent != null ? this.parent.absoluteName + "." + this.name : this.name;
    }

    /** Constructor */
    public DottedPath(String name) {
        this(null, name);
    }

    public static DottedPath parse(String path) {
        List<String> segments = Arrays.asList(path.split("\\."));
        DottedPath head = new DottedPath(segments.get(0));
        List<String> tail = segments.subList(1, segments.size());
        return head.append(tail);
    }

    public static DottedPath append(DottedPath parent, String segment) {
        return parent != null ? parent.append(segment) : new DottedPath(segment);
    }

    public DottedPath append(String segment) {
        return new DottedPath(this, segment);
    }

    public DottedPath append(List<String> segments) {
        if (segments.isEmpty()) {
            return this;
        }

        return this.append(segments.get(0)).append(segments.subList(1, segments.size()));
    }

    // /** Recursive constructor used to build chain */
    // DottedPath(DottedPath chainParent, String seperator, String name) {
    // if (null == seperator) {
    // this.parent = chainParent;
    // this.name = name;
    // }
    // else {
    // int sep = name.lastIndexOf(seperator);
    // if (sep == -1) {
    // this.parent = chainParent;
    // this.name = name;
    // }
    // else {
    // this.name = name.substring(sep + seperator.length());
    // this.parent = new DottedPath(chainParent, seperator, name.substring(0,
    // sep));
    // }
    // }
    //
    // if (null != this.parent) this.absoluteName =
    // this.parent.getAbsoluteName() + seperator + this.name;
    // else this.absoluteName = this.name;
    // }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DottedPath) && ((DottedPath) obj).absoluteName.equals(this.absoluteName);
    }

    @Override
    public int hashCode() {
        return this.absoluteName.hashCode();
    }

    /**
     * Number of elements from the head element to this element
     */
    public int size() {
        return parent != null ? parent.size() + 1 : 1;
    }

    public List<String> toSegmentList() {
        List<String> segmentList = new LinkedList<String>();
        for (DottedPath current = this; current != null; current = current.parent) {
            segmentList.add(0, current.name);
        }
        return segmentList;
    }

    public List<DottedPath> toList() {
        List<DottedPath> list = new LinkedList<DottedPath>();
        for (DottedPath current = this; current != null; current = current.parent) {
            list.add(0, current);
        }
        return list;
    }

    @Override
    public Iterator<DottedPath> iterator() {
        return toList().iterator();
    }

    /**
     * Parent element in path
     */
    public DottedPath getParent() {
        return this.parent;
    }

    /**
     * Fully qualified name of this element
     * 
     * @return getParent().getAbsoluteName() + "." + getName();
     */
    public String getAbsoluteName() {
        return this.absoluteName;
    }

    /**
     * Local name of this element
     */
    public String getName() {
        return this.name;
    }

    /**
     * Absolute name of this path using the specified separator.
     * 
     * @param sb
     *            StringBuilder to append to, or null to create a newly and
     *            properly sized StringBuilder
     * @param separator
     *            String to append between each path level
     * @return StringBuilder passed in or created
     */
    public StringBuilder toString(StringBuilder sb, final String separator) {
        if (null == sb) {
            int size = 0;
            for (DottedPath path = this; path != null; path = path.parent) {
                if (path != this) {
                    size += separator.length();
                }
                size += path.name.length();
            }

            sb = new StringBuilder(size);
        }

        if (null != this.parent) {
            this.parent.toString(sb, separator);
            sb.append(separator);
        }
        sb.append(this.name);
        return sb;
    }

    /**
     * @return absolute name
     */
    @Override
    public String toString() {
        return this.absoluteName;
    }

}
