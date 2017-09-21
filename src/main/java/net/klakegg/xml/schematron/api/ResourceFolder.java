package net.klakegg.xml.schematron.api;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author erlend
 */
public interface ResourceFolder extends URIResolver {

    InputStream load(String path) throws IOException;

    @Override
    default Source resolve(String href, String base) throws TransformerException {
        try {
            return !"".equals(base) ? null : new StreamSource(load(href));
        } catch (IOException e) {
            throw new TransformerException(e.getMessage(), e);
        }
    }

    static ResourceFolder of(String prefix) {
        return new ClassPathResourceFolder(prefix);
    }

    static ResourceFolder of(Path folder) {
        return new PathResourceFolder(folder);
    }

    class ClassPathResourceFolder implements ResourceFolder {

        private String prefix;

        public ClassPathResourceFolder(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public InputStream load(String path) throws IOException {
            String file = prefix + path;

            InputStream inputStream = getClass().getResourceAsStream(file);

            if (inputStream == null)
                throw new IOException(String.format("Unable to locate resource '%s' in classpath.", file));

            return inputStream;

        }
    }

    class PathResourceFolder implements ResourceFolder {

        private Path folder;

        public PathResourceFolder(Path folder) {
            this.folder = folder;
        }

        @Override
        public InputStream load(String path) throws IOException {
            return Files.newInputStream(folder.resolve(path));
        }
    }
}
