package net.madz.util;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Static utility functions used internally by the met-data infrastructure.
 */
public abstract class MetaDataUtil {
    public final static Charset UTF8 = Charset.forName("utf-8");

    /**
     * Null-safe equals
     * 
     * @param left
     * @param right
     * @return true if left value and right values equate
     */
    public final static boolean equals(Object left, Object right) {
        return (left == right) || (left != null && left.equals(right));
    }

    public final static int hashCode(int mul, Object obj) {
        if (null != obj) {
            return obj.hashCode() * mul;
        }
        return 0;
    }

    private static final char[] HEX_DIGITS = "01234567890abcdef".toCharArray();

    public final static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_DIGITS[(b >>> 4) & 0x0f]);
            sb.append(HEX_DIGITS[b & 0x0f]);
        }
        return sb.toString();
    }

    public final static String toHexString(int[] sets) {
        if (null != sets) {
            StringBuilder sb = new StringBuilder(sets.length * 8);
            for (int idx = sets.length - 1; idx >= 0; idx--) {
                final int set = sets[idx];
                sb.append(String.format("%08x", ((long) set) & 0x0ffffffffL));
            }
            return sb.toString();
        }
        return "";
    }

    public final static String[] nullSafeSplit(String str, String splitCriteria) {
        if (null != str) {
            return str.split(splitCriteria);
        }
        return null;
    }

    /**
     * Null-safe trim that will convert a null string to null. TODO This method
     * already exists elsewhere, I just can't find it
     * 
     * @param str
     *            String to trim
     * @return Trimmed string, or null if the string is null or a null string
     */
    public final static String trimToNull(String str) {
        if (null != str) {
            str = str.trim();
            if (str.length() == 0) {
                str = null;
            }
        }
        return str;
    }

    /**
     * @param value
     *            Object to convert to a string
     * @return Passed object + (passed object type)
     */
    public final static String toClassAndValue(Object value) {
        if (null == value) {
            return null;
        }
        return value + "(" + value.getClass().getName() + ")";
    }

    private final static <O> O minmax(final int signum, final Comparator<O> comparator, final O... values) {
        O returnValue = null;

        if (null != values) {
            for (O value : values) {
                if (null == returnValue) {
                    returnValue = value;
                } else if (null != value && Integer.signum(comparator.compare(value, returnValue)) == signum) {
                    returnValue = value;
                }
            }
        }
        return returnValue;

    }

    private final static <O extends Comparable<O>> O minmax(final int signum, final O... values) {
        O returnValue = null;

        if (null != values) {
            for (O value : values) {
                if (null == returnValue) {
                    returnValue = value;
                } else if (null != value && Integer.signum(value.compareTo(returnValue)) == signum) {
                    returnValue = value;
                }
            }
        }
        return returnValue;

    }

    public final static <O extends Comparable<O>> O min(final O... values) {
        return minmax(-1, values);
    }

    public final static <O extends Comparable<O>> O max(final O... values) {
        return minmax(1, values);
    }

    public final static <O> O min(final Comparator<O> comparator, final O... values) {
        return minmax(-1, comparator, values);
    }

    public final static <O> O max(final Comparator<O> comparator, final O... values) {
        return minmax(1, comparator, values);
    }

    /**
     * @param <T>
     * @param type
     * @param values
     * @return
     */
    public final static <T> T singleValue(Class<T> type, List<?> values) {
        if (null == values || values.size() != 1) {
            throw new IllegalStateException("Expecting a single value");
        }
        return type.cast(values.get(0));
    }

    /**
     * @param <T>
     * @param type
     * @param values
     * @return
     */
    public final static <T> T singleValue(Class<T> type, Collection<?> values) {
        if (null == values || values.size() != 1) {
            throw new IllegalStateException("Expecting a single value of type " + type.getSimpleName());
        }
        return type.cast(values.iterator().next());
    }

    /**
     * @param <T>
     * @param type
     * @param values
     * @return
     */
    public final static <T> T firstValue(Class<T> type, Collection<?> values) {
        final T value = findFirstValue(type, values);
        if (null != value) {
            return value;
        }
        throw new IllegalStateException("Expecting at least one value");
    }

    /**
     * @param <T>
     * @param type
     * @param values
     * @return
     */
    public final static <T> T findFirstValue(Class<T> type, Collection<?> values) {
        if (null != values) {
            for (Object value : values) {
                if (type.isInstance(value)) {
                    return type.cast(value);
                }
            }
        }
        return null;
    }

    /**
     * Returns the first non-null value in the set
     */
    public final static <O> O coalesce(final O... values) {
        if (null != values) {
            for (O value : values) {
                if (null != value) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * Add all elements to the collection
     */
    public final static <T, S extends Collection<T>> S addAll(S collection, T... values) {
        collection.addAll(Arrays.asList(values));
        return collection;
    }

    /**
     * Returns the last ordinal element in the last
     * 
     * @param list
     * @param ordinal
     *            0 for the last element in the list, 1 for second-to-last, and
     *            so on
     * @return Element from list or null if the element does not exist
     */
    public final static <T> T last(List<T> list, int ordinal) {
        final int size = list.size() - 1;
        if (size >= ordinal) {
            return list.get(size - ordinal);
        }
        return null;
    }

    /**
     * Returns the first ordinal element in the list.
     * 
     * @param list
     * @param oridinal
     *            0 for the first element in list, 1 for second, and so on
     * @return Element from list or null if the element does not exist
     */
    public final static <T> T first(final List<T> list, final int oridinal) {
        final int size = list.size() - 1;
        if (size >= oridinal) {
            return list.get(oridinal);
        }
        return null;
    }

    public final static String getAccessorName(Method method, String... prefixes) {
        final String methodName = method.getName();
        for (String prefix : prefixes) {
            if (methodName.startsWith(prefix) && methodName.length() > prefix.length()) {
                String firstNameCharacter = Character.toString(Character.toLowerCase(methodName.charAt(prefix.length())));
                String remainder = methodName.substring(prefix.length() + 1);
                return firstNameCharacter + remainder;
            }
        }
        return null;
    }

}
