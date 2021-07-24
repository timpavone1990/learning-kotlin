package de.timpavone1990.learningkotlin.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Basics {

    @Test
    fun `by default, runBlocking runs a coroutine in the current thread`() {
        val testFunctionThreadId = Thread.currentThread().id
        val coroutineThreadId = runBlocking {
            Thread.currentThread().id
        }
        assertEquals(
            testFunctionThreadId,
            coroutineThreadId,
            "Expected coroutine to be run in the current thread."
        )
    }

    @Test
    fun `runBlocking can run a coroutine in another thread`() {
        val testFunctionThreadId = Thread.currentThread().id
        val coroutineThreadId = runBlocking(Dispatchers.Default) {
            Thread.currentThread().id
        }
        assertNotEquals(
            testFunctionThreadId,
            coroutineThreadId,
            "Expected coroutine to be run in another thread."
        )
    }

    @Test
    fun `launch runs a non-blocking coroutine`() {
        val list = Collections.synchronizedList(mutableListOf<String>())
        runBlocking {
            list.add("Before launch")
            launch {
                list.add("In launch")
            }
            list.add("After launch")
        }
        assertEquals(
            listOf("Before launch", "After launch", "In launch"),
            list,
            "Expected launched coroutine to be non-blocking."
        )
    }

    @Test
    fun `by default, launch runs a coroutine in the current thread`() {
        val threadIds = Collections.synchronizedSet(mutableSetOf<Long>())
        threadIds.add(Thread.currentThread().id)
        runBlocking {
            launch {
                threadIds.add(Thread.currentThread().id)
            }
        }
        assertEquals(1, threadIds.size, "Expected coroutine to be run in the current thread.")
    }

    @Test
    fun `launch can run a coroutine in another thread`() {
        val threadIds = Collections.synchronizedSet(mutableSetOf<Long>())
        threadIds.add(Thread.currentThread().id)
        runBlocking {
            launch(Dispatchers.Default) {
                threadIds.add(Thread.currentThread().id)
            }
        }
        assertEquals(
            2,
            threadIds.size,
            "Expected coroutine to be run in another thread."
        )
    }
}
