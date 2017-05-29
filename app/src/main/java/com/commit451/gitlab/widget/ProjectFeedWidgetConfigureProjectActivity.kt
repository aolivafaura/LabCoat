package com.commit451.gitlab.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.commit451.gitlab.R
import com.commit451.gitlab.activity.BaseActivity
import com.commit451.gitlab.adapter.ProjectPagerAdapter
import com.commit451.gitlab.api.GitLabFactory
import com.commit451.gitlab.api.GitLabService
import com.commit451.gitlab.api.OkHttpClientFactory
import com.commit451.gitlab.extension.getParcelerParcelable
import com.commit451.gitlab.extension.putParcelParcelableExtra
import com.commit451.gitlab.fragment.ProjectsFragment
import com.commit451.gitlab.model.Account
import com.commit451.gitlab.model.api.Project

/**
 * You chose your account, now choose your project!
 */
class ProjectFeedWidgetConfigureProjectActivity : BaseActivity(), ProjectsFragment.Listener {

    companion object {

        val EXTRA_PROJECT = "project"
        val EXTRA_ACCOUNT = "account"

        fun newIntent(context: Context, account: Account): Intent {
            val intent = Intent(context, ProjectFeedWidgetConfigureProjectActivity::class.java)
            intent.putParcelParcelableExtra(EXTRA_ACCOUNT, account)
            return intent
        }
    }

    @BindView(R.id.tabs)
    lateinit var tabLayout: TabLayout
    @BindView(R.id.pager)
    lateinit var viewPager: ViewPager

    lateinit var gitLabInstance: GitLabService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_feed_widget_configure)
        ButterKnife.bind(this)

        val account = intent.getParcelerParcelable<Account>(EXTRA_ACCOUNT)!!
        gitLabInstance = GitLabFactory.create(account, OkHttpClientFactory.create(account, false).build())

        viewPager.adapter = ProjectPagerAdapter(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onProjectClicked(project: Project) {
        val data = Intent()
        data.putParcelParcelableExtra(EXTRA_PROJECT, project)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun getGitLab(): GitLabService {
        return gitLabInstance
    }
}
