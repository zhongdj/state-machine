package net.madz.core.common;

import net.madz.core.util.StringPrintWriter;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

public final class Dumper {
    private static final String INDENT = "   ";

    private StringBuilder sb;
    private final String indent;
    private boolean beginningOfLine = true;

    private Dumper child;

    public Dumper() {
        this("", new StringBuilder());
    }

    private Dumper(String indent, StringBuilder sb) {
        this.sb = sb;
        this.indent = indent;
    }

    public Dumper indent() {
        return push();
    }

    public Dumper push() {
        if (child == null) {
            child = new Dumper(indent + INDENT, sb);
        }
        return child;
    }

    public Dumper print(Object o) {
        if (beginningOfLine) {
            sb.append(indent);
        }
        if (null == o) {
            sb.append("null");
        } else {
            sb.append(o);
        }
        beginningOfLine = false;
        return this;
    }

    public Dumper println() {
        sb.append("\n");
        beginningOfLine = true;
        return this;
    }

    public Dumper println(Object o) {
        print(o);
        return println();
    }

    public Dumper dump(Map<?, ?> map) {
        println("{");
        for (Entry<?, ?> entry : map.entrySet()) {
            indent().print(entry.getKey()).print(" = ").dump(entry.getValue());
        }
        println("}");
        return this;
    }

    public Dumper dump(Iterable<?> iterable) {
        println("{");
        for (Object element : iterable) {
            indent().dump(element);
        }
        println("}");
        return this;
    }

    public Dumper dump(Object[] array) {
        return dump(Arrays.asList(array));
    }

    public Dumper dump(Object o) {
        if (null == o) {
            println("null");
        } else if (o instanceof Dumpable) {
            ((Dumpable) o).dump(this);
        } else if (o instanceof Iterable) {
            return dump((Iterable<?>) o);
        } else if (o instanceof Map) {
            return dump((Map<?, ?>) o);
        } else if (o.getClass().isArray()) {
            return dump((Object[]) o);
        } else if (o instanceof Throwable) {
            StringPrintWriter spw = new StringPrintWriter();
            ((Throwable) o).printStackTrace(spw);
            for (String line : spw.toString().split("\n")) {
                println(line);
            }
        } else {
            println(o);
        }
        return this;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    /**
     * Convert a dumpable object to a string.
     */
    public final static String toString(Object obj) {
        if (null != obj) {
            return new Dumper().dump(obj).toString();
        }
        return null;
    }
}
