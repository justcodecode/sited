package org.app4j.site.runtime.template;

import com.google.common.base.Charsets;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.app4j.site.runtime.cache.service.DiskCache;
import org.app4j.site.runtime.template.web.Md5Path;
import org.app4j.site.util.Resource;
import org.app4j.site.util.ResourceRepository;
import org.app4j.site.util.SortedList;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Optional;

/**
 * @author chi
 */
public class AssetsConfig {
    private final DiskCache cache;
    private final List<ResourceRepository> assetsRepositories = new SortedList<>((o1, o2) -> o2.hashCode() - o1.hashCode());

    public AssetsConfig(DiskCache cache) {
        this.cache = cache;
    }

    public AssetsConfig add(ResourceRepository resourceRepository) {
        assetsRepositories.add(resourceRepository);
        return this;
    }

    public InputStream minifyJs(Resource resource) {
        try {
            String hashedPath = new Md5Path(resource.path(), resource.md5()).hashedPath();
            Optional<InputStream> cache = this.cache.get(hashedPath);
            if (cache.isPresent()) {
                return cache.get();
            }

            JavaScriptCompressor compressor = new JavaScriptCompressor(new StringReader(new String(resource.bytes(), Charsets.UTF_8)),
                new JsErrorReporter(resource.path()));
            ByteArrayOutputStream minified = new ByteArrayOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(minified);
            compressor.compress(new OutputStreamWriter(minified), -1, true, false, false, false);
            out.flush();

            byte[] content = minified.toByteArray();
            this.cache.put(hashedPath, new ByteArrayInputStream(content));
            return new ByteArrayInputStream(content);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public InputStream minifyCss(Resource resource) {
        try {
            String hashedPath = new Md5Path(resource.path(), resource.md5()).hashedPath();
            Optional<InputStream> cache = this.cache.get(hashedPath);
            if (cache.isPresent()) {
                return cache.get();
            }

            CssCompressor compressor = new CssCompressor(new StringReader(new String(resource.bytes(), Charsets.UTF_8)));
            ByteArrayOutputStream minified = new ByteArrayOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(minified);
            compressor.compress(out, -1);
            out.flush();

            byte[] content = minified.toByteArray();
            this.cache.put(hashedPath, new ByteArrayInputStream(content));
            return new ByteArrayInputStream(content);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public Optional<Resource> get(String path) {
        for (ResourceRepository resourceRepository : assetsRepositories) {
            Optional<Resource> resourceOptional = resourceRepository.resolve(path);
            if (resourceOptional.isPresent()) {
                return Optional.of(resourceOptional.get());
            }
        }
        return Optional.empty();
    }

    class JsErrorReporter implements ErrorReporter {
        private final Logger logger = LoggerFactory.getLogger(JsErrorReporter.class);
        private final String filename;

        public JsErrorReporter(String filename) {
            this.filename = filename;
        }

        @Override
        public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0) {
                logger.warn(message);
            } else {
                logger.warn("[" + filename + ":" + line + "] " + message);
            }
        }

        @Override
        public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
            if (line < 0) {
                logger.error(message);
            } else {
                logger.error("[" + filename + ":" + line + "] " + message);
            }
        }

        @Override
        public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
            error(message, sourceName, line, lineSource, lineOffset);
            return new EvaluatorException(message);
        }
    }
}
