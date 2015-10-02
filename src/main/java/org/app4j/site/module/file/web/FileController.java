package org.app4j.site.module.file.web;

import com.google.common.io.ByteStreams;
import org.app4j.site.module.file.service.FileRepository;
import org.app4j.site.web.Request;
import org.app4j.site.web.Response;
import org.app4j.site.web.exception.NotFoundException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class FileController {
    private final Pattern pattern = Pattern.compile("^/i/(\\d+)x(\\d+)/f/(.+)$");

    private final FileRepository fileRepository;

    public FileController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Response file(Request request) throws IOException {
        String path = request.path().substring("/f/".length());
        return Response.bytes(ByteStreams.toByteArray(fileRepository.get(path)));
    }

    public Response image(Request request) throws IOException {
        Matcher matcher = pattern.matcher(request.path());
        if (matcher.matches()) {
//            int width = Integer.parseInt(matcher.group(1));
//            int height = Integer.parseInt(matcher.group(2));
            String file = matcher.group(3);
            byte[] image = ByteStreams.toByteArray(fileRepository.get(file));
            return Response.bytes(image);
        } else {
            throw new NotFoundException(request.path());
        }
    }
}
