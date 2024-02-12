package com.seb_main_034.SERVER.watchhistory.controller;

import com.seb_main_034.SERVER.watchhistory.dto.WatchHistoryDTO;
import com.seb_main_034.SERVER.watchhistory.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watch-history")
public class WatchHistoryController {
    @Autowired
    private WatchHistoryService watchHistoryService;

    @PostMapping
    public void saveWatchHistory(@RequestBody WatchHistoryDTO watchHistoryDTO) {
        watchHistoryService.saveWatchHistory(watchHistoryDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchHistoryDTO>> getUserWatchHistory(@PathVariable Long userId) {
        List<WatchHistoryDTO> watchHistory = watchHistoryService.getWatchHistoryByUserId(userId);
        return new ResponseEntity<>(watchHistory, HttpStatus.OK);
    }
}
