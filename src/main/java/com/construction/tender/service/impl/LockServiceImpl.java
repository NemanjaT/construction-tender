package com.construction.tender.service.impl;

import com.construction.tender.exception.LockExecutionException;
import com.construction.tender.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockServiceImpl is implemented so that a thread is locked while there's a tender ID lock in place.
 * This solution will work in a single-instance application, but won't do much good if there are a couple of instances running.
 * <p>
 * To achieve the same effect in multi-instance environment, instead of locking a thread, a table should be created
 * where a "locked" tender ID should be recorded (with timestamp for timeouts) and the same record should be deleted
 * once the tender ID should be unlocked.
 * Something very similar to Shedlocking, but instead of locking whole operations for every data manipulation, operation
 * should only be locked for a specific tender ID.
 * <p>
 * To avoid a lot of boilerplate code for tender specific shedlocking, only this simple single-instance locking is implemented.
 */
@Service
@Slf4j
public class LockServiceImpl implements LockService {
    private final ReentrantLock mainReentrantLock = new ReentrantLock();
    private final Map<Long, ReentrantLock> locks = new HashMap<>();

    @Override
    public <T> T lockTenderAndCall(Long tenderId, Callable<T> callable) {
        final var lock = getLock(tenderId);
        try {
            lock.lock();
            return callable.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new LockExecutionException(e);
        } finally {
            lock.unlock();
            locks.remove(tenderId);
        }
    }

    @Override
    public void lockTenderAndDo(Long tenderId, Runnable callable) {
        final var lock = getLock(tenderId);
        try {
            lock.lock();
            callable.run();
        } finally {
            lock.unlock();
            locks.remove(tenderId);
        }
    }

    private Lock getLock(Long tenderId) {
        try {
            mainReentrantLock.lock();
            final var tenderLock = locks.getOrDefault(tenderId, new ReentrantLock());
            locks.putIfAbsent(tenderId, tenderLock);
            return tenderLock;
        } finally {
            mainReentrantLock.unlock();
        }
    }
}
