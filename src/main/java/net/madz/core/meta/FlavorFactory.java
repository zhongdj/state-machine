package net.madz.core.meta;

public interface FlavorFactory {
    /**
     * Flavor factory.
     * 
     * @throws FlavorNotSupportedException
     */
    public <T> T getFlavor(Class<T> flavorInterface, Object container)
	    throws FlavorNotSupportedException;
}
