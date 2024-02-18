package videostreaming;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
@Controller
public class VideoStreamingController {
    VideoDAO videoDAO = new VideoDAO();

    private final RestTemplate restTemplate;

    public VideoStreamingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @GetMapping("/videostreaming")
    public String video(Model model, HttpSession session, HttpServletResponse response) {

        boolean doesExist=session.getAttribute("username") != null;
        if(!doesExist)
            return "redirect:login";

        return "videostreaming";
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

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<byte[]> streamVideo(@PathVariable int videoId) {

        //send the request to the video server containing the video path
        //get the video path from the videoDAO
        Video video = videoDAO.getVideo(videoId);
        String videoPath = video.getVideoPath();
        //send the request to the video server containing the video path in the request body
        String serverUrl = "http://filesystemservice:8084/fileSystem/stream";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("videoPath", videoPath);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<byte[]> response = restTemplate.postForEntity(serverUrl, requestEntity, byte[].class);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(response.getBody());

    }
}
