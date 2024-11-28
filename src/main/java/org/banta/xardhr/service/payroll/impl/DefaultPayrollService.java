package org.banta.xardhr.service.payroll.impl;

import org.banta.xardhr.domain.entity.Payslip;
import org.banta.xardhr.dto.request.PayslipDto;
import org.banta.xardhr.service.payroll.PayrollService;

import java.util.List;

public class DefaultPayrollService implements PayrollService {

    @Override
    public PayslipDto generatePayslip(Long userId) {
        return null;
    }

    @Override
    public PayslipDto approvePayslip(Long id) {
        return null;
    }

    @Override
    public List<PayslipDto> getUserPayslips(Long userId) {
        return List.of();
    }
}
