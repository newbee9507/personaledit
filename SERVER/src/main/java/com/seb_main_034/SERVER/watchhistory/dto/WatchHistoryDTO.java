package com.seb_main_034.SERVER.watchhistory.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class WatchHistoryDTO {
    private Long userId;
    private Long movieId;
    private LocalDateTime watchTime;
    // getters and setters
}
