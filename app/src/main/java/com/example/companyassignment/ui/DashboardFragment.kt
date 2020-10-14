package com.example.companyassignment.ui

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companyassignment.R
import com.example.companyassignment.adapter.RepoAdapter
import com.example.companyassignment.repository.DashboardRepository
import com.example.companyassignment.util.Utils
import com.example.companyassignment.viewmodel.DashboardViewModel
import com.example.companyassignment.viewmodel.ViewModelFactory
import com.example.pagingexample.model.RepoSearchResult
import com.google.android.material.textfield.TextInputLayout


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val adapter = RepoAdapter()
    lateinit var emptyList : TextView
    lateinit var list : RecyclerView
    lateinit var searchRepo : AppCompatEditText
    lateinit var inputLayout : TextInputLayout
    lateinit var progressBar : ProgressBar

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = ViewModelFactory(DashboardRepository())
        dashboardViewModel = ViewModelProvider(this,viewModelFactory).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        emptyList = root.findViewById(R.id.emptyList)
        list = root.findViewById(R.id.list)
        searchRepo = root.findViewById(R.id.search_repo)
        inputLayout = root.findViewById(R.id.input_layout)
        progressBar = root.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        list.addItemDecoration(decoration)
        setupScrollListener()
        initAdapter()

        val query = savedInstanceState?.getString(Utils.LAST_SEARCH_QUERY) ?: Utils.DEFAULT_QUERY
        if (dashboardViewModel.repoResult.value == null) {
            dashboardViewModel.searchRepo(query)
        }
        initSearch(query)

        return root
    }
    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            list.visibility = View.GONE
            progressBar.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            list.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun initAdapter() {
        list.adapter = adapter
        dashboardViewModel.repoResult.observe(viewLifecycleOwner, Observer {
                result ->
            when(result){
                is RepoSearchResult.Success ->{
                    showEmptyList(result.data.isEmpty())
                    adapter.submitList(result.data)
                }
                is RepoSearchResult.Error ->{
                    Toast.makeText(requireContext(), "\uD83D\uDE28 Wooops $result.message}", Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun setupScrollListener() {
        val layoutManager = list.layoutManager as LinearLayoutManager
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                dashboardViewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }


    private fun initSearch(query: String) {
        searchRepo.setText(query)

        searchRepo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }
    private fun updateRepoListFromInput() {
        searchRepo.text!!.trim().let {
            if (it.isNotEmpty()) {
                list.scrollToPosition(0)
                dashboardViewModel.searchRepo(it.toString())
            }
        }
    }
}