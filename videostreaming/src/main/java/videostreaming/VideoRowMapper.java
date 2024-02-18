package videostreaming;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VideoRowMapper implements RowMapper<Video> {

    @Override
    public Video mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String videoPath = rs.getString("videoPath");
        String description = rs.getString("description");
        String ownerUsername = rs.getString("ownerUsername");
        Video video = new Video();
        video.setId(id);
        video.setTitle(title);
        video.setVideoPath(videoPath);
        video.setDescription(description);
        video.setOwnerUsername(ownerUsername);
        return video;
    }
}