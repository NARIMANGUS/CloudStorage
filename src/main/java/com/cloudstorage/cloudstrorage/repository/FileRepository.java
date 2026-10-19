package com.cloudstorage.cloudstrorage.repository;

import com.cloudstorage.cloudstrorage.dto.FileDownload;
import com.cloudstorage.cloudstrorage.dto.FileView;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FileRepository {
    private final JdbcTemplate jdbcTemplate;
    public FileRepository (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addFile(
            long userId,
            String originalName,
            String storageKey,
            long sizeBytes) {
        return jdbcTemplate.update("INSERT INTO files (user_id, original_name, storage_key, size_bytes) VALUES (?,?,?,?)",
                userId,
                originalName,
                storageKey,
                sizeBytes
        );
    }

    public List<FileView> findByUserId(long userId) {
        return jdbcTemplate.query(

                """
                SELECT
                id,
                original_name,
                pg_size_pretty(size_bytes) AS size_label,
                to_char(uploaded_at, 'DD.MM.YYYY HH24:MI') AS uploaded_at_label
                FROM files
                WHERE user_id = ?
                ORDER BY uploaded_at DESC, id DESC;
                """,
                (rs, rowNum) -> new FileView(
                        rs.getLong("id"),
                        rs.getString("original_name"),
                        rs.getString("size_label"),
                        rs.getString("uploaded_at_label")
                ),
                userId
        );
    }

    public Optional<FileDownload> findDownloadByIdAndUserId(
            long fileId,
            long userId
    ) {
        return jdbcTemplate.query(
                """
                SELECT original_name, storage_key
                FROM files
                WHERE id = ? AND user_id = ?
                """,
                (rs, rowNum) -> new FileDownload(
                        rs.getString("original_name"),
                        rs.getString("storage_key")
                ),
                fileId,
                userId
        ).stream().findFirst();
    }
}

