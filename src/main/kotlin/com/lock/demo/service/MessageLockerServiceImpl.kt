package com.lock.demo.service

import com.lock.demo.domain.Message
import org.springframework.stereotype.Service

@Service
class MessageLockerServiceImpl(
    private val messages: HashMap<Any, Message>,
    private val mutex: Any
) : MessageLockerService {
    constructor() : this(
        messages = HashMap(),
        mutex = Any()
    )

    override fun tryLock(message: Message): Boolean {
        return if (existsMessage(message.number)) {
            false
        } else {
            addMessage(message)
            true
        }
    }

    override fun tryUnlock(message: Message): Message? {
        return if(!existsMessage(message.number)) {
            null
        }else {
            val message = getMessage(message.number)
            message?.let {
                unlockMessage(it)
            }
            message
        }
    }

    private fun existsMessage(key: String): Boolean {
        synchronized(mutex) {
            return messages[key] != null
        }
    }

    private fun getMessage(key: String): Message? {
        synchronized(mutex) {
            return messages[key]
        }
    }

    private fun addMessage(message: Message): Message {
        synchronized(mutex) {
            val messageMap = messages[message.number]
            if (messageMap == null) {
                messages[message.number] = message
            }
            return message
        }
    }

    private fun unlockMessage(message: Message) {
        synchronized(mutex) {
            messages.remove(message.number)
        }
    }
}