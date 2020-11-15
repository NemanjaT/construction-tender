package com.construction.tender.service;

import java.util.concurrent.Callable;

/**
 * Lock service is used to lock tender during manipulation to the same. The reason for locking a tender is so that, for
 * instance, a bidder can not create an offer for a tender which is accepting another offer at the same time.
 */
public interface LockService {
    /**
     * Locks the tender based on given argument ID.
     * @param tenderId tender ID to lock
     * @param callable what to execute after locking tender ID
     */
    <T> T lockTenderAndCall(Long tenderId, Callable<T> callable);

    /**
     * Locks the tender based on given argument ID.
     * @param tenderId tender ID to lock
     * @param runnable what to execute after locking tender ID
     */
    void lockTenderAndDo(Long tenderId, Runnable runnable);
}
