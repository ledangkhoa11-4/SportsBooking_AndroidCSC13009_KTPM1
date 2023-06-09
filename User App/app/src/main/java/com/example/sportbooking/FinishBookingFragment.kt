package com.example.sportbooking

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.sportbooking.Adapters.MyBookingListViewAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FinishBookingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FinishBookingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_finish_booking, container, false)
        listView = rootView.findViewById(R.id.finishBookingLv)
        listView.divider = null
        adapter = MyBookingListViewAdapter(requireActivity(), MyBookingActivity.finishBooking)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(requireActivity(), DetailBookingHistory::class.java)
            val index = MyBookingActivity.bookingHistories.indexOf(MyBookingActivity.finishBooking[i])
            intent.putExtra("index",index)

            startActivity(intent)
        }
        return rootView
    }

    companion object {
        lateinit var listView: ListView
        var adapter: MyBookingListViewAdapter? = null
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FinishBookingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FinishBookingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}