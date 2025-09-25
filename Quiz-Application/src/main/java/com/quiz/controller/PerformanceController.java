// package com.quiz.controller;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.ResponseBody;

// import com.quiz.entity.Leaderboard;
// import com.quiz.entity.User;
// import com.quiz.entity.dto.PerformanceDTO;
// import com.quiz.repo.LeaderboardRepository;
// import com.quiz.repo.UserRepository;

// @Controller
// public class PerformanceController {

//     @Autowired
//     private LeaderboardRepository leaderboardRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping("/my-performance/{userId}")
//     public String userPerformance(@PathVariable Long userId, Model model) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         // fetch leaderboard records for this user
//         List<Leaderboard> performances = leaderboardRepository.findByUser(user);

//         model.addAttribute("performances", performances);
//         model.addAttribute("username", user.getUsername());
//         return "performance"; // maps to performance.html
//     }

//     @GetMapping("/performance")
//     public String userPerformance(Model model, @AuthenticationPrincipal User user) {
//         var performances = leaderboardRepository.findByUser(user);
//         model.addAttribute("performances", performances);
//         return "performance";
//     }

//     // @GetMapping("/api/performance/{userId}")
//     // @ResponseBody
//     // public List<Leaderboard> getUserPerformance(@PathVariable Long userId) {
//     //     User user = userRepository.findById(userId)
//     //             .orElseThrow(() -> new RuntimeException("User not found"));
//     //     return leaderboardRepository.findByUser(user);
//     // }
// //    @GetMapping("/api/performance/{userId}")
// //     @ResponseBody
// //     public List<Leaderboard> getPerformanceByUser(@PathVariable Long userId) {
// //         User user = userRepository.findById(userId)
// //             .orElseThrow(() -> new RuntimeException("User not found"));
// //         return leaderboardRepository.findByUser(user);
// //     }
//     @GetMapping("/api/performance/{userId}")
//     @ResponseBody
//     public List<PerformanceDTO> getPerformanceByUser(@PathVariable Long userId) {
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         return leaderboardRepository.findByUser(user).stream()
//                 .map(lb -> new PerformanceDTO(
//                 lb.getQuiz().getTitle(),
//                 lb.getScore(),
//                 lb.getTotalMarks()
//         ))
//                 .toList();
//     }

// }
package com.quiz.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.entity.User;
import com.quiz.entity.dto.PerformanceDTO;
import com.quiz.repo.LeaderboardRepository;
import com.quiz.repo.UserRepository;

@RestController
@RequestMapping("/api/performance")
public class PerformanceController {

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public List<PerformanceDTO> getPerformanceByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return leaderboardRepository.findByUser(user).stream()
            .map(lb -> new PerformanceDTO(
                lb.getQuiz().getTitle(),
                lb.getScore(),
                lb.getTotalMarks()
            ))
            .collect(Collectors.toList());
    }
}
