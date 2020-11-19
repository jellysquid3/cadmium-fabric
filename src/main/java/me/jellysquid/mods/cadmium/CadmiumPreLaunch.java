package me.jellysquid.mods.cadmium;

import me.jellysquid.mods.cadmium.inject.ClasspathInjector;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CadmiumPreLaunch implements PreLaunchEntrypoint {
	private static final Logger LOGGER = LogManager.getLogger("Cadmium");

	@Override
	public void onPreLaunch() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			LOGGER.warn("Not injecting libraries into classpath as we're in a development environment");

			return;
		}

		LibraryMounter libraries = new LibraryMounter();

		ClasspathInjector injector;

		try {
			injector = ClasspathInjector.create();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Couldn't create classpath injector", e);
		}

		LOGGER.info("Using classpath injector: {}", injector.toString());

		List<URL> injections = new ArrayList<>();

		for (String path : getLibraryList()) {
			try {
				URL url = libraries.mount(path);
				injections.add(url);

				LOGGER.info("Added {} to classpath injection list", url);
			} catch (IOException e) {
				throw new RuntimeException("Couldn't install virtual library: " + path, e);
			}
		}

		try {
			injector.inject(injections);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Could not inject libraries into Knot classpath", e);
		}

		LOGGER.info("Successfully injected libraries into classpath");
	}

	private String[] getLibraryList() {
		try (InputStream in = CadmiumPreLaunch.class.getResourceAsStream("/META-INF/jar-replacements/files.txt")) {
			if (in == null) {
				throw new IOException("Couldn't find library list in mod");
			}

			return IOUtils.toString(in, StandardCharsets.UTF_8)
					.split("\n");
		} catch (IOException e) {
			throw new RuntimeException("Couldn't read library list", e);
		}
	}
}