package com.movieapichallenge.app.service;

import com.movieapichallenge.app.util.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public ResponseEntity downloadFileByIdCharacter(String idCharacter, String fileName) {
        FileUtil fileUtil = new FileUtil();
        Resource resource = null;

        Path path = Paths.get(fileUtil.getCharacterImagePath() + idCharacter + "/" + fileName);
        try{
            resource = new UrlResource(path.toUri());
        }catch(MalformedURLException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
