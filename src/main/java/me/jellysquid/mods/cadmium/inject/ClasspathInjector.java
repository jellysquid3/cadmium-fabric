package me.jellysquid.mods.cadmium.inject;

import java.net.URL;
import java.util.List;

public interface ClasspathInjector {
    void inject(List<URL> injections) throws ReflectiveOperationException;

    static ClasspathInjector create() throws ReflectiveOperationException {
        return new KnotClasspathInjector(Thread.currentThread().getContextClassLoader());
    }
}
