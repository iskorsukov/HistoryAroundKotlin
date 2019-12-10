package my.projects.historyaroundkotlin.presentation.view.main.detail.content

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import my.projects.historyaroundkotlin.R
import my.projects.historyaroundkotlin.model.detail.ArticleDetails
import my.projects.historyaroundkotlin.presentation.argument.ArticleDetailsArgument
import my.projects.historyaroundkotlin.presentation.view.base.BaseNavControllerFragmentTest
import my.projects.historyaroundkotlin.presentation.viewstate.main.detail.ArticleDetailsViewData
import org.junit.Test

class DetailFragmentTest: BaseNavControllerFragmentTest<DetailFragment>() {

    private fun getDetailsArguments(): Bundle {
        val details = ArticleDetails(
            1L,
            "Title",
            "Extract",
            null,
            1.0 to 2.0,
            "url"
        )

        val args = Bundle()
        args.putSerializable("detailsArgument", ArticleDetailsArgument(ArticleDetailsViewData(details, false)))

        return args
    }

    @Test
    fun showsDetails() {
        launchFragment(DetailFragment::class.java, getDetailsArguments())

        assertDetailsShown()
    }

    private fun assertDetailsShown() {
        onView(withId(R.id.articleDetailsTitle)).check(matches(withText("Title")))
        onView(withId(R.id.articleDetailsExtract)).check(matches(withText("Extract")))
    }
}