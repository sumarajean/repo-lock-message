package com.lock.demo.service

import com.lock.demo.domain.Message

interface MessageLockerService {

    fun tryLock(message: Message): Boolean

    fun tryUnlock(message: Message): Message?
}