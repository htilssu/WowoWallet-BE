package com.ewallet.ewallet.exceptions;

import com.ewallet.ewallet.viewmodels.ErrorVm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

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
        return ResponseEntity.status(500).body(ErrorVm.builder().message(ex.getMessage())
                .errorCode(500).build());
    }
}
