package com.example.sportbooking_owner

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class CourtListActivity : AppCompatActivity() {
    lateinit var addBtn:FloatingActionButton
    companion object{
        var adapter:CustomAdapter?=null
        var courtList=ArrayList<Courts>()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_court_list)


        courtList=SignIn.listCourt
        adapter=CustomAdapter(courtList)
        addBtn=findViewById(R.id.addCourtBtn)
        addBtn.setOnClickListener{
            startActivity(Intent(this,NewInfoActivity::class.java))
        }
        val courtRv=findViewById<RecyclerView>(R.id.CourtRv)
        courtRv.adapter=adapter
        courtRv.layoutManager=LinearLayoutManager(this)
        adapter!!.onItemClick={court ->
            var intent=Intent(this,UpdateCourtActivity::class.java)
            intent.putExtra("pos", adapter!!.click_position)
            startActivity(intent)

        }
        //Search adapter
        var searchView=findViewById<AutoCompleteTextView>(R.id.SearchView)
        val item=courtList!!.map {  it.Name }
        val adapterSearchview=ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,item)
        searchView.setAdapter(adapterSearchview)
        searchView!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                courtTemp.clear()
//                val search=searchView.text.toString()
//                for(i in 0 until courtList!!.size){
//                    if(courtList!![i].Name.contains(search, ignoreCase = true)){
//                        courtTemp.add(courtList!![i])
//                    }
//                }
//                adapter!!.notifyDataSetChanged()

            }
        })
    }

}
class CustomAdapter(private val dataSet: ArrayList<Courts>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    var onItemClick:((Courts)->Unit)?=null
    var click_position:Int=0
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courtName: TextView
        val courtType:TextView
        val image:ImageView

        init {
            // Define click listener for the ViewHolder's View.
            courtName = view.findViewById(R.id.CourtNameTV)
            courtType = view.findViewById(R.id.CourtTypeTV)
            image=view.findViewById(R.id.CourtImageView)
            view.setOnClickListener {
                click_position=adapterPosition
                onItemClick?.invoke(dataSet[adapterPosition])
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.court_container, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.courtName.text = dataSet[position].Name
        viewHolder.courtType.text = dataSet[position].Type
        if(dataSet[position].bitmapArrayList.size>0)
        viewHolder.image.setImageBitmap(dataSet[position].bitmapArrayList[0])


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}