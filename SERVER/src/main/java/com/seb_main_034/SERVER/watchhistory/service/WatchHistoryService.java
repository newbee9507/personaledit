package com.seb_main_034.SERVER.watchhistory.service;

import com.seb_main_034.SERVER.watchhistory.dto.WatchHistoryDTO;
import com.seb_main_034.SERVER.watchhistory.entity.WatchHistory;
import com.seb_main_034.SERVER.watchhistory.repository.WatchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchHistoryService {
    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    public WatchHistory saveWatchHistory(WatchHistoryDTO watchHistoryDTO) {
        WatchHistory watchHistory = new WatchHistory();
        watchHistory.setUserId(watchHistoryDTO.getUserId());
        watchHistory.setMovieId(watchHistoryDTO.getMovieId());
        watchHistory.setWatchTime(LocalDateTime.now());
        return watchHistoryRepository.save(watchHistory);
    }

    public List<WatchHistoryDTO> getWatchHistoryByUserId(Long userId) {
        List<WatchHistory> histories = watchHistoryRepository.findByUserId(userId);
        return histories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private WatchHistoryDTO convertToDTO(WatchHistory watchHistory) {
        WatchHistoryDTO dto = new WatchHistoryDTO();
        dto.setUserId(watchHistory.getUserId());
        dto.setMovieId(watchHistory.getMovieId());
        dto.setWatchTime(watchHistory.getWatchTime());
        return dto;
    }
}
