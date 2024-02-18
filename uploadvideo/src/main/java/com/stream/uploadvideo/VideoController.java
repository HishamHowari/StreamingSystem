package com.stream.uploadvideo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VideoController {

    VideoDAO videoDAO = new VideoDAO();

    @GetMapping("/video")
    public String video(Model model, HttpSession session, HttpServletResponse response) {

        boolean doesExist=session.getAttribute("username") != null;
        if(!doesExist)
            return "redirect:login";

        return "video";
    }

    @ModelAttribute("videos")
    public List<Video> getVideos(HttpSession session) {
        if(session.getAttribute("username")==null)
            return null;
        String userName = session.getAttribute("username").toString();
        if(userName==null)
            return null;
        return videoDAO.getUserVideos(userName);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("videoTitle") String videoTitle,
                                   @RequestParam("videoDesc") String videoDesc,
                                   @RequestParam("videoFile") MultipartFile videoFile, Model model,HttpSession session) {
        System.out.println("Video Title: " + videoTitle);
        System.out.println("Video Description: " + videoDesc);
        System.out.println("Video File: " + videoFile.getOriginalFilename());

        if (session.getAttribute("username") == null || videoFile.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload.");
            return "redirect:video";
        }

        try {
            String serverUrl = "http://filesystemservice:8084/fileSystem/upload";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);


            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("ownerUsername", session.getAttribute("username"));
            body.add("file", new ByteArrayResource(videoFile.getBytes()) {
                @Override
                public String getFilename() {
                    return videoFile.getOriginalFilename();
                }
            });
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                    serverUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ObjectMapper responseObjectMapper = new ObjectMapper();
                JsonNode jsonNode = responseObjectMapper.readTree(responseEntity.getBody());
                String videoPath = jsonNode.get("path").asText();
                String videoOwner = "hisham";

                Video video = new Video();
                video.setTitle(videoTitle);
                video.setDescription(videoDesc);
                video.setVideoPath(videoPath);
                video.setOwnerUsername(videoOwner);

                boolean isUploaded = videoDAO.saveVideo(video);

                if (isUploaded) model.addAttribute("message", "Video uploaded successfully");
            } else {
                model.addAttribute("message", "Error uploading video to the server.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Error uploading video.");
        }

        return "redirect:video";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteVideo(@PathVariable("id") int id, Model model, HttpSession session) {
        if(session.getAttribute("username")==null)
            return "redirect:login";

        System.out.println("Deleting Video ID: " + id);
        Video video = videoDAO.getVideo(id);
        String serverUrl = "http://filesystemservice:8084/fileSystem/delete";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("path", video.getVideoPath());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<String> responseEntity = new RestTemplate().exchange(
                serverUrl,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );
        boolean isDeleted = videoDAO.removeVideo(video);
        if (isDeleted) model.addAttribute("message", "Video deleted successfully");
        return "video";
    }

}
