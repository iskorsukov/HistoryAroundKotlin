package my.projects.historyaroundkotlin.presentation.view.start.splash

import androidx.navigation.NavDirections
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.presentation.view.base.BaseNavControllerFragmentTest
import my.projects.historyaroundkotlin.presentation.view.start.splash.content.SplashFragment
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.timeout
import org.mockito.Mockito.verify
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class SplashFragmentTest: BaseNavControllerFragmentTest<SplashFragment>() {

    @Test
    fun showsLogo() {
        launchFragment(SplashFragment::class.java)

        onView(withId(R.id.logo)).check(matches(isDisplayed()))
    }

    @Test
    fun navigatesToPermissionsFlow() {
        launchFragment(SplashFragment::class.java)

        verify(mockNavController).navigate(ArgumentMatchers.argThat { directions: NavDirections ->
            directions.actionId == R.id.action_splashFragment_to_permissions_flow
        })
    }
}