package com.hozan.univer.web.api;

import com.hozan.univer.exception.InternalException;
import com.hozan.univer.model.File;
import com.hozan.univer.service.FileContentService;
import com.hozan.univer.service.FileService;
import com.hozan.univer.service.MultipartFileSender;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.min;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/files")
public class FileContentController extends BaseController {

    @Autowired
    private FileContentService contentService;
    @Autowired
    private FileService fileService;

    @PostMapping(value = "/{fileId}")
    public ResponseEntity<?> createFileContent(@PathVariable("fileId") Long id, @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("< createFileContent");

        Optional<File> f = fileService.getById(id);
        if (!f.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        f.get().setMimeType(file.getContentType());
        if(f.get().getName().isEmpty()){
            f.get().setName(file.getOriginalFilename());
        }
        contentService.saveFileContent(f.get(), file.getInputStream());

        fileService.update(f.get());

        logger.info("> createFileContent");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/metadata")
    public ResponseEntity<?> createMetadata(@RequestParam("name") String name, @RequestParam("summary") String summary) {
        logger.info("< createMetadata");

        File newFile = new File();
        newFile.setName(name);
        newFile.setSummary(summary);

        Optional<File> savedFile = fileService.create(newFile);
        if(!savedFile.isPresent()){
            throw new InternalException("The file 'metadata' was not created.");
        }

        logger.info("> createMetadata");
        return new ResponseEntity<>(savedFile, HttpStatus.CREATED);
    }

    @GetMapping(value = "/metadata")
    public ResponseEntity<File> getFileMetadata(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "id", required = false) Long id) throws IOException {
        logger.info("< getFileMetadata");

        if (name != null) {
            Optional<File> file = this.fileService.getByName(name);
            return file.map(file1 -> new ResponseEntity<>(file1, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        if (id != null) {
            Optional<File> file = fileService.getById(id);
            return file.map(file1 -> new ResponseEntity<>(file1, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

        logger.info("> getFileMetadata");

        return new ResponseEntity<>(HttpStatus.OK);
    }
   @DeleteMapping(value = "/metadata/{fileId}")
    public ResponseEntity<?> deleteMetadata(@PathVariable("fileId") Long id){
        logger.info("< deleteFile id:{}",id);

        Optional<File> f = fileService.getById(id);
        if (f.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        fileService.remove(id);

        logger.info("> deleteFile");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping(value="/{fileId}")
    public ResponseEntity<?> getFileContent(@PathVariable("fileId") Long id) {
        logger.info("< getContent") ;

        Optional<File> f = fileService.getById(id);

        if (!f.isPresent()) {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<InputStream> inputStream = contentService.getFileContent(f.get());
        if(!inputStream.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

            InputStreamResource inputStreamResource = new InputStreamResource(inputStream.get());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(f.get().getContentLength());
            headers.set("Content-Type", f.get().getMimeType());


        logger.info("> getContent") ;
        return new ResponseEntity<Object>(inputStreamResource, headers, HttpStatus.OK);
    }

    @GetMapping("/videos/{idVideo}")
    public void   getVideo(@PathVariable  Long idVideo, @RequestHeader HttpHeaders headers, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("< getRangeVideo idVideo: {}", idVideo) ;

        Optional<File> f = fileService.getById(idVideo);
        if (!f.isPresent()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Optional<InputStream> inputStream = contentService.getFileContent(f.get());


             MultipartFileSender.fromInputStreamAndFile(inputStream.get(), f.get())
                              .with(request)
                    .with(response)
                    .serveResource();
        logger.info("> getRangeVideo idVideo: {}", idVideo) ;
        }

    @GetMapping("/videoss/{idVideo}")
    public  ResponseEntity<?> getVideo(@PathVariable  Long idVideo, @RequestHeader HttpHeaders headers) throws IOException {

        Optional<File> f = fileService.getById(idVideo);
        if (!f.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<InputStream> inputStream = contentService.getFileContent(f.get());



        byte[] videoContent = getByteContent(inputStream.get(), headers);
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        resHeaders.setContentLength(videoContent.length);

        ByteArrayResource byteArrayResource = new ByteArrayResource(videoContent);
        return new  ResponseEntity<ByteArrayResource> (byteArrayResource,resHeaders, HttpStatus.PARTIAL_CONTENT);
    }

    private byte[] getByteContent(InputStream inputStream, HttpHeaders httpHeaders) throws IOException {
        int contentLength  = (int)inputStream.available();
        byte[]  fullVideo = IOUtils.toByteArray(inputStream);

        List<HttpRange> range = httpHeaders.getRange();
         if (range.get(0) != null) {
            int start = (int)range.get(0).getRangeStart(contentLength);
            int end = (int)range.get(0).getRangeEnd(contentLength);
            int rangeLength = (int)min(1 * 1024 * 1024, end - start + 1);
             byte[] partialVideo = new byte[rangeLength];

             int j = 0;
             for(int i = start; i < rangeLength; i++) {
                 partialVideo[j] = fullVideo[i];
                 j++;
             }

       return  partialVideo;
        } else {
            int rangeLength = (int)min(1 * 1024 * 1024, contentLength);
            byte[] video = new byte[rangeLength];
             ByteBuffer byteBuffer = ByteBuffer.wrap(video);
             byteBuffer.get(video, 0, rangeLength);
             byteBuffer.clear();
            return video;
        }
    }

}

