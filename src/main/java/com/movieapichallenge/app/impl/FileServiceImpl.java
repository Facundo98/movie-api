package com.movieapichallenge.app.impl;

import com.movieapichallenge.app.service.FileService;
import com.movieapichallenge.app.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public ResponseEntity downloadFile(Long id, String fileName, HttpServletRequest request,String type) {
        FileUtil fileUtil = new FileUtil();
        Resource resource = null;
        String contentType;
        Path path = null;

        switch (type){
            case "character": path = fileUtil.getImagePath(id,fileName);
            break;

            case "genre": path = fileUtil.getGenrePath(id,fileName);
            break;

            case "movieOrSerie": path = fileUtil.getMovieOrSeriePath(id,fileName);
            break;
        }

        try{
            resource = new UrlResource(path.toUri());
        }catch(MalformedURLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
