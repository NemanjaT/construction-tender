package com.construction.tender.service.unit;

import com.construction.tender.exception.LockExecutionException;
import com.construction.tender.service.IssuerService;
import com.construction.tender.service.LockService;
import com.construction.tender.service.impl.LockServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LockServiceImplTest {
    @Mock
    private IssuerService issuerService;

    @InjectMocks
    private LockService lockService = new LockServiceImpl();

    @Test
    public void exampleMethodTriggeredOnCall() {
        lockService.lockTenderAndCall(1234L,
                () -> issuerService.isTenderFromIssuer(1234L, "issuer"));

        verify(issuerService).isTenderFromIssuer(eq(1234L), eq("issuer"));
    }

    @Test
    public void exampleMethodTriggeredOnDo() {
        lockService.lockTenderAndDo(1234L,
                () -> issuerService.isTenderFromIssuer(1234L, "issuer"));

        verify(issuerService).isTenderFromIssuer(eq(1234L), eq("issuer"));
    }

    @Test
    public void runtimeExceptionThrownOnCatch() {
        assertThrows(NumberFormatException.class, () -> {
            lockService.lockTenderAndCall(1234L, () -> {
                throw new NumberFormatException("error");
            });
        });
    }

    @Test
    public void wrappedExceptionThrownOnCatch() {
        assertThrows(LockExecutionException.class, () -> {
            lockService.lockTenderAndCall(1234L, () -> {
                throw new Exception("error");
            });
        });
    }
}
