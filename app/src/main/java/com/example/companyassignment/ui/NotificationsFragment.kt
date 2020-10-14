package com.example.companyassignment.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.companyassignment.R
import com.example.companyassignment.adapter.MainAdapter
import com.example.companyassignment.util.Utils
import com.example.companyassignment.viewmodel.HomeViewModel
import com.example.companyassignment.viewmodel.NotificationsViewModel
import io.supercharge.shimmerlayout.ShimmerLayout

class NotificationsFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var adapter : MainAdapter
    private val TAG = "HomeFragment"
    private lateinit var shimmerTextView : ShimmerLayout
    private lateinit var successconstraintlayout : ConstraintLayout
    private lateinit var errorconstratintlayout : ConstraintLayout
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        shimmerTextView = root.findViewById(R.id.shimmer_text)
        successconstraintlayout = root.findViewById(R.id.successconstraintlayout)
        errorconstratintlayout = root.findViewById(R.id.errorconstratintlayout)
        recyclerView = root.findViewById(R.id.recyclerView)
        val btnLoad = root.findViewById<Button>(R.id.btnReload)
        setViewModel()
        setRecyclerView()
        shimmerTextView.visibility = View.VISIBLE
        shimmerTextView.startShimmerAnimation()
        btnLoad.setOnClickListener {
            setViewModel()
            setRecyclerView()
            shimmerTextView.visibility = View.VISIBLE
            shimmerTextView.startShimmerAnimation()
        }
        return root
    }

    private fun setRecyclerView(){
        adapter = MainAdapter(requireContext(),homeViewModel.listData.value?: emptyList())
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setViewModel(){
        homeViewModel.successfulLiveData.observe(viewLifecycleOwner, Observer {
                toList ->
            toList?.let {
                Log.e(TAG, "data Updated = $it")
                shimmerTextView.visibility = View.GONE
                shimmerTextView.stopShimmerAnimation()
                adapter.update(it)
            }
        })

        homeViewModel.failureLiveData.observe(viewLifecycleOwner, Observer {
                isFailed->
            isFailed?.let {
                successconstraintlayout.visibility = View.GONE
                errorconstratintlayout.visibility = View.VISIBLE
            }
        })
        if (Utils.isNetworkAvailable(requireContext())) {
            successconstraintlayout.visibility = View.VISIBLE
            errorconstratintlayout.visibility = View.GONE
            homeViewModel.getTodoList()
        } else {
            successconstraintlayout.visibility = View.GONE
            errorconstratintlayout.visibility = View.VISIBLE
        }
    }
}