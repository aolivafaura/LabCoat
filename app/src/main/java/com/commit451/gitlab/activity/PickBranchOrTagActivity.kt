package com.commit451.gitlab.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.commit451.gitlab.R
import com.commit451.gitlab.adapter.PickBranchOrTagPagerAdapter
import com.commit451.gitlab.model.Ref
import kotlinx.android.synthetic.main.activity_pick_branch_or_tag.*

/**
 * Intermediate activity when deep linking to another activity and things need to load
 */
class PickBranchOrTagActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_PROJECT_ID = "project_id"
        private const val EXTRA_CURRENT_REF = "current_ref"

        const val EXTRA_REF = "ref"

        fun newIntent(context: Context, projectId: Long, currentRef: Ref?): Intent {
            val intent = Intent(context, PickBranchOrTagActivity::class.java)
            intent.putExtra(EXTRA_PROJECT_ID, projectId)
            intent.putExtra(EXTRA_CURRENT_REF, currentRef)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pick_branch_or_tag)
        val projectId = intent.getLongExtra(EXTRA_PROJECT_ID, -1)
        val currentRef = intent.getParcelableExtra<Ref>(EXTRA_CURRENT_REF)
        viewPager.adapter = PickBranchOrTagPagerAdapter(this, supportFragmentManager, projectId, currentRef)
        tabLayout.setupWithViewPager(viewPager)
        if (currentRef != null) {
            val position = if (currentRef.type == Ref.TYPE_BRANCH) 0 else 1
            tabLayout.getTabAt(position)!!.select()
            viewPager.currentItem = position
        }
        root.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.do_nothing, R.anim.fade_out)
    }
}
