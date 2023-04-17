package com.example.sportbooking_owner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking_owner.DTO.Courts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView

class CourtListActivity : AppCompatActivity() {
    lateinit var addBtn:FloatingActionButton
    lateinit var scanQrBtn:ImageButton
    lateinit var nav_bar: NavigationBarView
    companion object{
        var adapter:CustomAdapter?=null
        var courtList=ArrayList<Courts>()
        val REQUEST_CAMERA_PERMISSION = 200
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_court_list)

        courtList=SignIn.listCourt
        //nav bar handle
        nav_bar=findViewById(R.id.bottomNavigationView)
        navBarHandle(nav_bar)
        adapter=CustomAdapter(this,courtList)
        addBtn=findViewById(R.id.addCourtBtn)
        addBtn.setOnClickListener{
            startActivity(Intent(this,NewInfoActivity::class.java))
        }
        val courtRv=findViewById<RecyclerView>(R.id.CourtRv)
        courtRv.adapter=adapter
        courtRv.layoutManager=LinearLayoutManager(this)
        adapter!!.onItemClick={idx, view ->
            //var intent=Intent(this,UpdateCourtActivity::class.java)

            //startActivity(intent)
            showMenu(this,view, R.menu.popup_menu, idx)
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
        scanQrBtn = findViewById(R.id.scanQrBtn)
        scanQrBtn.setOnClickListener {
            if(checkCameraPermission(this)){
                val intent = Intent(this, ScanQrActivity::class.java)
                startActivity(intent)
            }
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, ScanQrActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "We cannot scan QR code without using camera. Please grant permission!", Toast.LENGTH_SHORT).show()

            }
        }
    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_home
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home-> true
                R.id.item_user->{
                    startActivity(Intent(this,OwnerTabActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}

private fun showMenu(context:Activity ,v: View, @MenuRes menuRes: Int, pos:Int) {
    val popup = PopupMenu(context!!, v)
    popup.menuInflater.inflate(menuRes, popup.menu)

    popup.setOnMenuItemClickListener {
        if(it.toString().contains("Modify",true)){
            var intent=Intent(context,UpdateCourtActivity::class.java)
            intent.putExtra("pos",pos)
            context.startActivity(intent)
        }else{
            var intent=Intent(context,ViewBookingActivity::class.java)
            intent.putExtra("pos",pos)
            context.startActivity(intent)
        }
        true
    }
    popup.setOnDismissListener {
        // Respond to popup being dismissed.
    }
    // Show the popup menu.
    popup.show()
}
fun checkCameraPermission(context: Activity):Boolean{
    val vt = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    if(vt != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(
            context,
            arrayOf(android.Manifest.permission.CAMERA),
            200)
        return false
    }else{
        return true
    }
}

class CustomAdapter(private val context:Activity, private val dataSet: ArrayList<Courts>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    var onItemClick:((Int, View)->Unit)?=null

   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courtName: TextView
        val courtType:TextView
        val image:ImageView
       val typeImage:ImageView
       val courtLocation:TextView

        init {
            // Define click listener for the ViewHolder's View.
            courtName = view.findViewById(R.id.courtNameTv)
            courtLocation = view.findViewById(R.id.courtLocationTv)
            courtType = view.findViewById(R.id.typeSportTv)
            typeImage = view.findViewById(R.id.typeSportIv)
            image=view.findViewById(R.id.courtImage)
            view.setOnClickListener {

                onItemClick?.invoke(adapterPosition, it)
            }
        }
    }

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
        viewHolder.courtType.text = "Type sport: " + dataSet[position].Type
        viewHolder.courtLocation.text = dataSet[position].location?.addressName
        val drawableID =  context.resources.getIdentifier("${dataSet[position].Type.lowercase()}_icon","drawable",context.packageName)
        viewHolder.typeImage.setImageDrawable(context.resources.getDrawable(drawableID))
        if(dataSet[position].bitmapArrayList.size>0)
        viewHolder.image.setImageBitmap(dataSet[position].bitmapArrayList[0])


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}