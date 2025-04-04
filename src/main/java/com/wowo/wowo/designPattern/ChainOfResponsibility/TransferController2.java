package com.wowo.wowo.designPattern.ChainOfResponsibility;

import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.TransferDTO;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController2 {
    private static final Logger logger = LoggerFactory.getLogger(TransferController2.class);

    private final TransferService2 transferService2;
    private final TransactionMapper transferMapper;

    @Autowired
    public TransferController2(TransferService2 transferService2, TransactionMapper transferMapper) {
        this.transferService2 = transferService2;
        this.transferMapper = transferMapper;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> transfer(@RequestBody TransferDTO transferDTO) {
        try {

            // Gọi TransferService để xử lý giao dịch
            Transaction transaction = transferService2.processTransaction(transferDTO);

            // Ánh xạ sang TransactionDTO và trả về
            TransactionDTO transactionDTO = transferMapper.toDto(transaction);
            return ResponseEntity.ok(transactionDTO);
        } catch (IllegalArgumentException e) {
            logger.error("Bad request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null); // Trả về lỗi 400 nếu có ngoại lệ
        } catch (Exception e) {
            logger.error("Internal error: ", e);
            return ResponseEntity.internalServerError().body(null); // Trả về lỗi 500 nếu có lỗi không mong muốn
        }
    }
}