package com.wowo.wowo.exceptions;

import com.wowo.wowo.data.dto.ErrorVm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVm> handleException(Exception ex) {
        return ResponseEntity.status(500).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(500).build());
    }

    /**
     * Handle {@link TransactionNotFoundException} to response with 404 status code
     *
     * @param ex TransactionNotFoundException
     *
     * @return ResponseEntity<ErrorVm>
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorVm> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        return ResponseEntity.status(404).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(404).build());
    }

    /**
     * Handle {@link InsufficientBalanceException} to response with 400 status code
     *
     * @param ex InsufficientBalanceException
     *
     * @return ResponseEntity<ErrorVm>
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorVm> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        return ResponseEntity.status(400).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(400).build());
    }

    /**
     * Handle {@link TransactionFailedException} to response with 500 status code
     *
     * @param ex TransactionFailedException
     *
     * @return ResponseEntity<ErrorVm>
     */
    @ExceptionHandler(TransactionFailedException.class)
    public ResponseEntity<ErrorVm> handleTransactionFailedException(TransactionFailedException ex) {
        return ResponseEntity.status(400).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(400).build());
    }

    /**
     * Handle {@link UserNotFoundException} to response with 404 status code
     * when not found any user in database
     *
     * @param ex UserNotFoundException
     *
     * @return ResponseEntity<ErrorVm>
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorVm> handeUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(404).build());
    }

    /**
     * Handle {@link NegativeMoneyException} to response with 400 status code
     *
     * @param ex NegativeMoneyException
     *
     * @return ResponseEntity<ErrorVm>
     */
    @ExceptionHandler(NegativeMoneyException.class)
    public ResponseEntity<ErrorVm> handleNegativeMoney(NegativeMoneyException ex) {
        return ResponseEntity.status(400).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(400).build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorVm> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(404).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(404).build());
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorVm> handleBadRequest(BadRequest ex) {
        return ResponseEntity.status(400).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(400).build());
    }
}
