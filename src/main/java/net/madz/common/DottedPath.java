package net.madz.common;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Represents a hierarchical name in which each level is separated by a dot.
 * This class is loosely modeled after java.io.File
 */
public class DottedPath implements Iterable<DottedPath> {
    private final Optional<DottedPath> parent;
    private final String name, absoluteName;
    private List<String> paths;

    /** Constructor */
    public DottedPath(String name) {
        this(Optional.<DottedPath>absent(), name);
    }

    private DottedPath(Optional<DottedPath> parent, String name) {
        this.parent = parent;
        this.name = name;
        this.absoluteName = this.parent.isPresent() ? this.parent.get().absoluteName + "." + this.name : this.name;

        this.paths = makePaths();
    }

    private List<String> makePaths() {
        if (!this.parent.isPresent()) return newArrayList(this.name);

        List<String> paths = newArrayList(this.parent.get().paths);
        paths.add(this.name);

        return paths;
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
        return new DottedPath(Optional.of(this), segment);
    }

    public DottedPath append(List<String> segments) {
        if (segments.isEmpty()) {
            return this;
        }

        return this.append(segments.get(0)).append(segments.subList(1, segments.size()));
    }

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
        return parent.isPresent() ? parent.get().size() + 1 : 1;
    }

    private List<DottedPath> toList() {
        if (!this.parent.isPresent()) return newArrayList(this);

        ArrayList<DottedPath> dottedPaths = newArrayList(this.parent.get().toList());
        dottedPaths.add(this);

        return dottedPaths;
    }

    @Override
    public Iterator<DottedPath> iterator() {
        return toList().iterator();
    }

    /**
     * Parent element in path
     */
    public Optional<DottedPath> getParent() {
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
     *
     * @param separator
     *            String to append between each path level
     * @return StringBuilder passed in or created
     */
    public StringBuilder toString(StringBuilder sb, final String separator) {
        return sb.append(Joiner.on(separator).join(paths));
    }

    /**
     * @return absolute name
     */
    @Override
    public String toString() {
        return this.absoluteName;
    }

}
