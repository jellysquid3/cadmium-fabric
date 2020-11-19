package me.jellysquid.mods.cadmium;

import com.google.common.jimfs.Jimfs;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LibraryMounter {
    private static final Logger LOGGER = LogManager.getLogger(LibraryMounter.class);

    private final FileSystem fs = Jimfs.newFileSystem();

    public URL mount(String path) throws IOException {
        HashedLibrary resource = this.getLibrary(path);
        Path virtualPath = this.createVirtualPath(resource.hash);

        Files.copy(new ByteArrayInputStream(resource.data), virtualPath);

        LOGGER.info("Mounted library '{}' with hash {}", path, resource.hash);

        return virtualPath.toUri().toURL();
    }

    private Path createVirtualPath(String name) {
        return this.fs.getPath(name + ".jar");
    }

    private HashedLibrary getLibrary(String path) throws IOException {
        byte[] data;

        try (InputStream in = getResourceStream(path)) {
            data = IOUtils.toByteArray(in);
        }

        String hash;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = Hex.encodeHexString(digest.digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return new HashedLibrary(hash, data);
    }

    private InputStream getResourceStream(String path) {
        return LibraryMounter.class
                .getResourceAsStream(path);
    }

    private static class HashedLibrary {
        public final String hash;
        public final byte[] data;

        private HashedLibrary(String hash, byte[] data) {
            this.hash = hash;
            this.data = data;
        }
    }
}
