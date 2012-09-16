package net.madz.core.common;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import net.madz.core.util.StringPrintWriter;

public final class Dumper {
    private static final String INDENT = "   ";

    private final PrintWriter pw;
    private final String indent;
    private boolean beginningOfLine = true;

    private Dumper parent;
    private Dumper child;

    public Dumper() {
	this(new StringPrintWriter());
    }

    public Dumper(OutputStream out) {
	this(new PrintWriter(out, true));
    }

    public Dumper(PrintWriter pw) {
	this(pw, "", null);
    }

    private Dumper(PrintWriter pw, String indent, Dumper parent) {
	this.pw = pw;
	this.indent = indent;
	this.parent = parent;
    }

    public Dumper indent() {
	return push();
    }

    public Dumper undent() {
	return pop();
    }

    public Dumper push() {
	if (child == null) {
	    child = new Dumper(pw, indent + INDENT, this);
	}
	return child;
    }

    public Dumper pop() {
	return parent != null ? parent : this;
    }

    public Dumper print(Object o) {
	if (beginningOfLine) {
	    pw.print(indent);
	}
	if (null == o) {
	    pw.print("null");
	} else {
	    pw.print(o);
	}
	beginningOfLine = false;
	return this;
    }

    public Dumper println() {
	pw.println();
	beginningOfLine = true;
	return this;
    }

    public Dumper println(Object o) {
	print(o);
	return println();
    }

    public Dumper printf(String format, Object... values) {
	return print(String.format(format, values));
    }

    public Dumper printfln(String format, Object... values) {
	return println(String.format(format, values));
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

    public String toString() {
	return pw.toString();
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

    public String getIndentString() {
	return indent;
    }
}
