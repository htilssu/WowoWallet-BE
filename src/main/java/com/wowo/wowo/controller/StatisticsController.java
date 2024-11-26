package com.wowo.wowo.controller;

import com.wowo.wowo.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // API thống kê tổng hợp
    @GetMapping("/all")
    public String getAllStatistics() {
        // Lấy tất cả các thống kê
        long totalWallets = statisticsService.getTotalWallets();
        long totalGroupFunds = statisticsService.getTotalGroupFunds();
        long totalUsers = statisticsService.getTotalUsers();
        long totalApplications = statisticsService.getTotalApplication();

        // Trả về thông tin thống kê dưới dạng JSON
        return String.format(
                "{ \"total_wallets\": %d, \"total_group_funds\": %d, \"total_users\": %d, \"total_partners\": %d }",
                totalWallets, totalGroupFunds, totalUsers, totalApplications
        );
    }
}
