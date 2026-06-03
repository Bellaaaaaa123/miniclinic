package tw.edu.fju.miniclinic.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;
import tw.edu.fju.miniclinic.model.StatsResponse;

@Controller
public class StatsController {

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @GetMapping("/stats")
    public String statsPage(Model model) {
        long doctorCount = doctorRepo.count();
        long patientCount = patientRepo.count();
        long appointmentCount = appointmentRepo.count();

        Map<String, Long> appointmentsByDepartment = appointmentRepo.findAll().stream()
                .collect(Collectors.groupingBy(appt -> appt.getDoctor().getDepartment(), Collectors.counting()));

        model.addAttribute("doctorCount", doctorCount);
        model.addAttribute("patientCount", patientCount);
        model.addAttribute("appointmentCount", appointmentCount);
        model.addAttribute("appointmentsByDepartment", appointmentsByDepartment);
        return "stats";
    }
}

/**
 * 統計摘要 API Controller (REST)
 * 提供系統統計資訊的 JSON 端點（無需認證）
 */
@RestController
@RequestMapping("/api/stats")
class StatsApiController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * GET /api/stats
     * 回傳系統統計摘要
     * 不需要認證（不受 Interceptor 保護）
     */
    @GetMapping
    public StatsResponse getStats() {
        // 計算各項統計數據
        long totalDoctors = doctorRepository.count();
        long totalPatients = patientRepository.count();
        long totalAppointments = appointmentRepository.count();

        // 依狀態分類計算掛號數量
        long bookedCount = appointmentRepository.countByStatus("BOOKED");
        long completedCount = appointmentRepository.countByStatus("COMPLETED");
        long cancelledCount = appointmentRepository.countByStatus("CANCELLED");

        // 構建 byStatus 字典
        Map<String, Integer> byStatus = new HashMap<>();
        byStatus.put("BOOKED", (int) bookedCount);
        byStatus.put("COMPLETED", (int) completedCount);
        byStatus.put("CANCELLED", (int) cancelledCount);

        // 回傳統計物件
        return new StatsResponse(
            (int) totalDoctors,
            (int) totalPatients,
            (int) totalAppointments,
            byStatus
        );
    }
}
