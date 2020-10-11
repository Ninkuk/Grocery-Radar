package tech.groceryradar.groceryradar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val groups = mutableListOf<Group>()

        groupRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter: GroupRecyclerAdapter = GroupRecyclerAdapter(groups)
        groupRecyclerView.adapter = adapter

        //loads the groups that the user is in
        val QrCodesList = mutableListOf<String>("QR Code") //TODO read QR code from file
        for (code in QrCodesList) {
            val db = FirebaseFirestore.getInstance()
            val dbRef = db.collection("groups").document(code)

            dbRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    var itemsList: MutableList<Items> = mutableListOf()
                    var customItemsList: MutableList<Items> = mutableListOf()
                    var tagsList: MutableList<Tag> = mutableListOf()

                    dbRef.collection("items").get().addOnSuccessListener {
                        itemsList = it.toObjects(Items::class.java)
                    }

                    dbRef.collection("customItems").get().addOnSuccessListener {
                        customItemsList = it.toObjects(Items::class.java)
                    }

                    dbRef.collection("tags").get().addOnSuccessListener {
                        tagsList = it.toObjects(Tag::class.java)
                    }

                    val peopleList: MutableList<String> =
                        snapshot.get("people") as MutableList<String>
                    groups.add(
                        Group(
                            code,
                            snapshot.getString("listName")!!,
                            peopleList,
                            itemsList,
                            customItemsList,
                            tagsList
                        )
                    )
                }
            }

            adapter.notifyDataSetChanged()
        }

        joinListBtn.setOnClickListener {

        }

        createListBtn.setOnClickListener {

        }

    }
}