package com.stream.uploadvideo;

import java.util.List;

public class VideoDAO extends DAO{
    public boolean saveVideo(Video video) {
        // Save the video to the database
        String query = "INSERT INTO Video (title, videoPath, description, ownerUsername) VALUES (?, ?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(query, video.getTitle(), video.getVideoPath(), video.getDescription(), video.getOwnerUsername());
        return rowsAffected > 0;
    }
    public boolean removeVideo(Video video) {
        String query = "DELETE FROM Video WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(query, video.getId());
        return rowsAffected > 0;
    }
    public List<Video> getUserVideos(String ownerUsername) {
        String query = "SELECT * FROM Video WHERE ownerUsername = ?";
        return jdbcTemplate.query(query, new Object[]{ownerUsername}, new VideoRowMapper());
    }
    public Video getVideo(int id) {
        String query = "SELECT * FROM Video WHERE id = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new VideoRowMapper());
    }

}
