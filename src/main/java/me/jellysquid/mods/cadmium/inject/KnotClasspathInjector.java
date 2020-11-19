package me.jellysquid.mods.cadmium.inject;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class KnotClasspathInjector implements ClasspathInjector {
    private final Field urlLoaderField;
    private final Constructor<?> knotDelegateConstructor;
    private final ClassLoader classLoader;

    public KnotClasspathInjector(ClassLoader classLoader) throws ReflectiveOperationException {
        this.urlLoaderField = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader")
                .getDeclaredField("urlLoader");
        this.urlLoaderField.setAccessible(true);

        this.knotDelegateConstructor = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader$DynamicURLClassLoader")
                .getDeclaredConstructor(URL[].class);
        this.knotDelegateConstructor.setAccessible(true);

        this.classLoader = classLoader;
    }

    @Override
    public void inject(List<URL> injections) throws ReflectiveOperationException {
        URLClassLoader knotDelegateClassLoader = (URLClassLoader) this.urlLoaderField.get(this.classLoader);

        URL[] urls = ArrayUtils.addAll(injections.toArray(new URL[0]), knotDelegateClassLoader.getURLs());
        URLClassLoader classLoader = (URLClassLoader) this.knotDelegateConstructor.newInstance(new Object[]{ urls });

        this.urlLoaderField.set(this.classLoader, classLoader);
    }
}
