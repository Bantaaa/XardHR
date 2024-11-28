package org.banta.xardhr.service.payroll;

import org.banta.xardhr.dto.request.PayslipDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PayrollService {
    PayslipDto generatePayslip(Long userId);
    PayslipDto approvePayslip(Long id);
    List<PayslipDto> getUserPayslips(Long userId);
}