package com.wowo.wowo.decorator;

import com.wowo.wowo.data.dto.WithdrawDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingWithdrawDecorator extends WithdrawServiceDecorator {
    private static final Logger logger = LoggerFactory.getLogger(LoggingWithdrawDecorator.class);

    public LoggingWithdrawDecorator(WithdrawService wrappedService) {
        super(wrappedService);
    }

    @Override
    public void withdraw(WithdrawDTO withdrawDTO) {
        logger.info("Bắt đầu rút tiền: sourceId={}, amount={}",
                withdrawDTO.getSourceId(), withdrawDTO.getAmount());
        super.withdraw(withdrawDTO);
        logger.info("Rút tiền thành công: sourceId={}, amount={}",
                withdrawDTO.getSourceId(), withdrawDTO.getAmount());
    }
}