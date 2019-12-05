package my.projects.historyaroundkotlin.utils

import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class MockitoUtil {
    companion object {
        fun <T> any(): T {
            Mockito.any<T>()
            return uninitialized()
        }

        fun <T> argThat(matcher: ArgumentMatcher<T>): T {
            ArgumentMatchers.argThat(matcher)
            return uninitialized()
        }

        fun <T> argThat(matcher: (T) -> Boolean): T {
            ArgumentMatchers.argThat(matcher)
            return uninitialized()
        }

        private fun <T> uninitialized(): T = null as T
    }
}

