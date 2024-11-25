package com.recover.deleted.messages.chat.recovery.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.recover.deleted.messages.chat.recovery.R
import com.recover.deleted.messages.chat.recovery.adapters.StatusAdapter
import com.recover.deleted.messages.chat.recovery.base.BaseActivity
import com.recover.deleted.messages.chat.recovery.data.WhatsAppStatusRepository
import com.recover.deleted.messages.chat.recovery.databinding.ActivityStatusBinding
import com.recover.deleted.messages.chat.recovery.model.StatusModel
import com.recover.deleted.messages.chat.recovery.viewModel.StatusViewModel
import com.recover.deleted.messages.chat.recovery.viewModel.StatusViewModelFactory

class StatusActivity : BaseActivity() {
    private lateinit var binding: ActivityStatusBinding
    private lateinit var emptyLay: RelativeLayout
    private lateinit var statusAdapter: StatusAdapter

    private val viewModel: StatusViewModel by viewModels {
        StatusViewModelFactory(WhatsAppStatusRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        emptyLay = binding.root.findViewById(R.id.emptyLayout)
        setHeader(getString(R.string.status))
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observeStatuses()
    }

    private fun setupRecyclerView() {
        statusAdapter = StatusAdapter(emptyList(),this)

        binding.statusRecycler.apply {
            layoutManager = GridLayoutManager(this@StatusActivity, 3) // 3 columns
            adapter = statusAdapter
        }
    }

    // Observe the LiveData from ViewModel
    private fun observeStatuses() {
        viewModel.getStatuses().observe(this) { statuses ->
            updateStatuses(statuses)
        }
    }

    // Update RecyclerView with new data
    private fun updateStatuses(statuses: List<StatusModel>) {
        Log.d("statusList", "updateStatuses: "+statuses.size)
         statuses.forEach { status ->
            Log.d("statusList", "Status: ${status.filepath}")
            Log.d("statusList", "Status: ${status.type}")
        }

        statusAdapter = StatusAdapter(statuses, this)
        binding.statusRecycler.adapter = statusAdapter
        if (statuses.isEmpty()) {
            emptyLay.visibility = RelativeLayout.VISIBLE
        } else {
            emptyLay.visibility = RelativeLayout.GONE
        }


    }
}